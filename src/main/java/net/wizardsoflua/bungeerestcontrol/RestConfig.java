package net.wizardsoflua.bungeerestcontrol;

import java.util.UUID;

public class RestConfig {
  private int port = 60001;
  private boolean https = false;
  private String hostname = "127.0.0.1";
  private String keyStore;
  private String keyStorePassword;
  private String keyPassword;
  private String apiToken = UUID.randomUUID().toString();
  private String commandWhitelist = "*";
  private String commandBlacklist = "";

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public boolean isHttps() {
    return https;
  }

  public void setHttps(boolean https) {
    this.https = https;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getKeyStore() {
    return keyStore;
  }

  public void setKeyStore(String keyStore) {
    this.keyStore = keyStore;
  }

  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }

  public String getKeyPassword() {
    return keyPassword;
  }

  public void setKeyPassword(String keyPassword) {
    this.keyPassword = keyPassword;
  }

  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(String apiToken) {
    this.apiToken = apiToken;
  }

  public String getCommandWhitelist() {
    return commandWhitelist;
  }

  public void setCommandWhitelist(String commandWhitelist) {
    this.commandWhitelist = commandWhitelist;
  }

  public String getCommandBlacklist() {
    return commandBlacklist;
  }

  public void setCommandBlacklist(String commandBlacklist) {
    this.commandBlacklist = commandBlacklist;
  }

}
