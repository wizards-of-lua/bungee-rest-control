package net.wizardsoflua.bungeerestcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Command sender representing the REST-Interface.
 */
public class RestCommandSender implements CommandSender {

  private static final String NAME = "REST-Interface";
  public static final RestCommandSender INSTANCE = new RestCommandSender();

  private List<String> receivedMessages = new ArrayList<>();

  public RestCommandSender() {}

  public List<String> getReceivedMessages() {
    return receivedMessages;
  }

  @Override
  public void sendMessage(String message) {
    receivedMessages.add(message);
  }

  @Override
  public void sendMessages(String... messages) {
    for (String message : messages) {
      sendMessage(message);
    }
  }

  @Override
  public void sendMessage(BaseComponent... message) {
    sendMessage(BaseComponent.toLegacyText(message));
  }

  @Override
  public void sendMessage(BaseComponent message) {
    sendMessage(message.toLegacyText());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Collection<String> getGroups() {
    return Collections.emptySet();
  }

  @Override
  public void addGroups(String... groups) {
    throw new UnsupportedOperationException(NAME + " may not have groups");
  }

  @Override
  public void removeGroups(String... groups) {
    throw new UnsupportedOperationException(NAME + " may not have groups");
  }

  @Override
  public boolean hasPermission(String permission) {
    return true;
  }

  @Override
  public void setPermission(String permission, boolean value) {
    throw new UnsupportedOperationException(NAME + " has all permissions");
  }

  @Override
  public Collection<String> getPermissions() {
    return Collections.emptySet();
  }
}
