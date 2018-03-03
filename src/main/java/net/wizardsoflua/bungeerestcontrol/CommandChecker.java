package net.wizardsoflua.bungeerestcontrol;

import java.util.regex.Pattern;

public class CommandChecker {

  private final Pattern whitelistPattern;
  private final Pattern blacklistPattern;

  public CommandChecker(String commandWhitelist, String commandBlacklist) {
    whitelistPattern = toPattern(commandWhitelist);
    blacklistPattern = toPattern(commandBlacklist);
  }

  public boolean isAllowed(String command) {
    if (!whitelistPattern.matcher(command).matches()) {
      return false;
    }
    if (blacklistPattern.matcher(command).matches()) {
      return false;
    }
    return true;
  }

  private Pattern toPattern(String list) {
    if (list == null || list.trim().isEmpty()) {
      return Pattern.compile("");
    }
    if ("*".equals(list.trim())) {
      return Pattern.compile(".*");
    }
    return Pattern.compile("^(" + String.join("|", list.split(" *, *")) + ")( .*)?$");
  }


}
