package net.wizardsoflua.bungeerestcontrol;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;
import net.freeutils.httpserver.HTTPServer.VirtualHost;
import net.md_5.bungee.api.ProxyServer;

public class RestInterface {

  public interface Context {
    RestConfig getRestConfig();
  }

  private final Logger logger;
  private Context context;
  private HTTPServer server;
  private String apiToken;
  private CommandChecker commandChecker;

  public RestInterface(Logger logger, Context context) {
    this.logger = logger;
    this.context = context;
  }

  public void start() throws IOException {
    logger.info("Starting REST-Interface");

    RestConfig restConfig = context.getRestConfig();
    apiToken = restConfig.getApiToken();
    checkNotNull(apiToken, "Missing apiToken! Please configure apiToken in plugin config file!");
    int port = restConfig.getPort();
    boolean secure = restConfig.isHttps();
    String hostname = restConfig.getHostname();
    checkNotNull(hostname, "Missing hostname! Please configure hostname in plugin config file!");

    String commandWhitelist = context.getRestConfig().getCommandWhitelist();
    String commandBlacklist = context.getRestConfig().getCommandBlacklist();
    commandChecker = new CommandChecker(commandWhitelist, commandBlacklist);

    final String keystore = restConfig.getKeyStore();
    String keystorePassword = restConfig.getKeyStorePassword();
    String keyPassword = restConfig.getKeyPassword();
    String protocol;
    if (secure) {
      protocol = "https";
      checkNotNull(keystore,
          "Missing keystore! Please configure path to keystore file in plugin config file!");
      checkNotNull(keystorePassword,
          "Missing keystorePassword! Please configure password for accessing the keystore file in plugin config file!");
      checkNotNull(keyPassword,
          "Missing keyPassword! Please configure password for accessing the key inside the keystore file in plugin config file!");
    } else {
      protocol = "http";
    }

    server = new HTTPServer(port) {
      protected ServerSocket createServerSocket() throws IOException {
        ServerSocketFactory factory =
            this.secure
                ? createSSLServerSocketFactory(keystore, keystorePassword.toCharArray(),
                    keyPassword.toCharArray())
                : ServerSocketFactory.getDefault();
        ServerSocket serv = factory.createServerSocket();
        serv.setReuseAddress(true);
        serv.bind(new InetSocketAddress(port));
        return serv;
      }
    };
    server.setSecure(secure);

    VirtualHost host = new VirtualHost(hostname);
    server.addVirtualHost(host);

    host.addContexts(new RestHandlers());

    logger.info("Starting REST-Interface at " + protocol + "://" + hostname + ":" + port);
    server.start();
  }

  public void stop() {
    if (server != null) {
      server.stop();
    }
  }

  private SSLServerSocketFactory createSSLServerSocketFactory(String keystore,
      char[] keystorePassword, char[] keyPassword) {
    try {
      SSLContext sslContext = SSLContext.getInstance("TLS");
      KeyStore ks = KeyStore.getInstance("JKS");
      try (InputStream in = new FileInputStream(new File(keystore))) {
        ks.load(in, keystorePassword);
      }
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, keyPassword);
      sslContext.init(kmf.getKeyManagers(), null, null);

      return sslContext.getServerSocketFactory();
    } catch (UnrecoverableKeyException | KeyManagementException | NoSuchAlgorithmException
        | KeyStoreException | CertificateException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  class RestHandlers {
    public RestHandlers() {}

    @HTTPServer.Context(value = "/execute", methods = {"POST"})
    public int execute(Request req, Response resp) throws IOException {
      try {
        logger.info("Received " + req.getMethod() + " " + req.getPath());
        String authorization = req.getHeaders().get("Authorization");
        if (!isAuthorized(authorization)) {
          logger.log(Level.WARNING, "Request is not authorized.");
          resp.sendError(401, "Missing correct authorization token in request header!");
          return 0;
        }
        String command = toString(req.getBody(), StandardCharsets.UTF_8);
        logger.info("Command: " + command);

        if (!isAllowed(command)) {
          logger.log(Level.WARNING, "Command is forbidden.");
          resp.sendError(403, "Command is forbidden!");
          return 0;
        }


        RestCommandSender sender = new RestCommandSender();
        ProxyServer.getInstance().getPluginManager().dispatchCommand(sender, command);

        return sendResponse(req, resp, sender.getReceivedMessages());
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Error handling POST: " + req.getPath(), e);
        resp.sendError(500, "Couldn't process POST method! e=" + e.getMessage()
            + "\n See latest.log for more info!");
        return 0;
      }
    }

    private boolean isAuthorized(String authorization) {
      if (authorization != null && authorization.startsWith("token ")) {
        String token = authorization.substring("token ".length());
        return apiToken.equals(token);
      }
      return false;
    }

    private boolean isAllowed(String command) {
      return commandChecker.isAllowed(command);
    }

    private String toString(InputStream in, Charset charset) throws IOException {
      try (BufferedReader buffer = new BufferedReader(new InputStreamReader(in, charset))) {
        return buffer.lines().collect(Collectors.joining("\n"));
      }
    }

    private int sendResponse(Request req, Response resp, List<String> messages) throws IOException {
      String content = String.join("\n", messages.toArray(new String[messages.size()]));
      logger.info("Response: " + content);
      byte[] bytes = content.getBytes(StandardCharsets.UTF_8.name());
      InputStream body = new ByteArrayInputStream(bytes);
      long[] range = null;
      resp.sendHeaders(200, bytes.length, -1L, null, "text/plain", range);
      resp.sendBody(body, bytes.length, null);
      return 0;
    }
  }

}
