package net.wizardsoflua.bungeerestcontrol;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeRestControlPlugin extends Plugin {
  private RestInterface restInterface;

  @Override
  public void onEnable() {
    RestConfig restConfig = loadRestConfig();

    restInterface = new RestInterface(getLogger(), new RestInterface.Context() {
      @Override
      public RestConfig getRestConfig() {
        return restConfig;
      }
    });
    try {
      restInterface.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onDisable() {
    super.onDisable();
    if (restInterface != null) {
      restInterface.stop();
      restInterface = null;
    }
  }

  private RestConfig loadRestConfig() {
    Configuration pluginConfig = loadPluginConfig();
    RestConfig result = new RestConfig();
    result.setHostname(pluginConfig.getString("hostname"));
    result.setHttps(pluginConfig.getBoolean("https"));
    result.setPort(pluginConfig.getInt("port"));
    result.setKeyStore(pluginConfig.getString("keyStore"));
    result.setKeyStorePassword(pluginConfig.getString("keyStorePassword"));
    result.setKeyPassword(pluginConfig.getString("keyPassword"));
    result.setApiToken(pluginConfig.getString("apiToken"));
    result.setCommandWhitelist(pluginConfig.getString("whitelist"));
    result.setCommandBlacklist(pluginConfig.getString("blacklist"));
    return result;
  }

  private void saveRestConfig(RestConfig config) {
    Configuration cfg = new Configuration();
    cfg.set("hostname", config.getHostname());
    cfg.set("https", config.isHttps());
    cfg.set("port", config.getPort());
    cfg.set("keyStore", config.getKeyStore());
    cfg.set("keyStorePassword", config.getKeyStorePassword());
    cfg.set("keyPassword", config.getKeyPassword());
    cfg.set("apiToken", config.getApiToken());
    cfg.set("whitelist", config.getCommandWhitelist());
    cfg.set("blacklist", config.getCommandBlacklist());
    savePluginConfig(cfg);
  }

  private Configuration loadPluginConfig() {
    try {
      File configFile = new File(getDataFolder(), "config.yml");
      if (!configFile.exists()) {
        saveRestConfig(new RestConfig());
      }
      return ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  private void savePluginConfig(Configuration cfg) {
    try {
      File configFile = new File(getDataFolder(), "config.yml");
      if (!configFile.exists()) {
        configFile.getParentFile().mkdirs();
        configFile.createNewFile();
      }
      ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, configFile);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

}
