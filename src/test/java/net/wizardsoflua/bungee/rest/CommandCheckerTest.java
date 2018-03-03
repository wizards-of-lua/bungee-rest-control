package net.wizardsoflua.bungee.rest;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.wizardsoflua.bungeerestcontrol.CommandChecker;

public class CommandCheckerTest extends Assertions {

  @Test
  public void test_all() {
    // Given
    String commandWhitelist = "*";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("glist");

    // Then
    assertThat(act).isTrue();
  }
  
  @Test
  public void test_none_1() {
    // Given
    String commandWhitelist = "*";
    String commandBlacklist = "*";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("glist");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_none_2() {
    // Given
    String commandWhitelist = "";
    String commandBlacklist = "*";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("glist");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_none_3() {
    // Given
    String commandWhitelist = "";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("glist");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_none_4_1() {
    // Given
    String commandWhitelist = "";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("send");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_none_4_2() {
    // Given
    String commandWhitelist = "";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("send mickkay");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_only_glist_1() {
    // Given
    String commandWhitelist = "glist";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("glist");

    // Then
    assertThat(act).isTrue();
  }
  
  @Test
  public void test_only_glist_2_1() {
    // Given
    String commandWhitelist = "glist";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("send");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_only_glist_2_2() {
    // Given
    String commandWhitelist = "glist";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("send mickkay");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_only_glist_and_send_1() {
    // Given
    String commandWhitelist = "glist,send";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("glist");

    // Then
    assertThat(act).isTrue();
  }
  
  @Test
  public void test_only_glist_and_send_2_1() {
    // Given
    String commandWhitelist = "glist,send";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("send");

    // Then
    assertThat(act).isTrue();
  }
  
  @Test
  public void test_only_glist_and_send_2_2() {
    // Given
    String commandWhitelist = "glist,send";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("send mickkay");

    // Then
    assertThat(act).isTrue();
  }
  
  @Test
  public void test_only_glist_and_send_3_1() {
    // Given
    String commandWhitelist = "glist,send";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("find");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_only_glist_and_send_3_2() {
    // Given
    String commandWhitelist = "glist,send";
    String commandBlacklist = "";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("find mickkay");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_not_find_1_1() {
    // Given
    String commandWhitelist = "*";
    String commandBlacklist = "find";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("find");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_not_find_1_2() {
    // Given
    String commandWhitelist = "*";
    String commandBlacklist = "find";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("find mickkay");

    // Then
    assertThat(act).isFalse();
  }
  
  @Test
  public void test_not_find_2() {
    // Given
    String commandWhitelist = "*";
    String commandBlacklist = "find";
    CommandChecker underTest = new CommandChecker(commandWhitelist, commandBlacklist);

    // When
    boolean act = underTest.isAllowed("glist");

    // Then
    assertThat(act).isTrue();
  }
}
