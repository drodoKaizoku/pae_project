package be.ipl.pae.biz.user;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.bcrypt.BCrypt;

import java.time.LocalDate;

public class UserTest {

  private User emptyUser;
  private User fullUser;

  /**
   * Create two user one empty, and a full one.
   * 
   * @throws Exception when fullUser is null
   */
  @BeforeEach
  public void setUp() throws Exception {
    this.emptyUser = new User();
    LocalDate registrationDate = LocalDate.now();
    String hash = BCrypt.gensalt(12);
    String passwordHashed = BCrypt.hashpw("test", hash);

    this.fullUser = new User(0, "testuser", "Naji", "Walid", "test", "test", passwordHashed,
        registrationDate, true, true);

  }

  /**
   * Test with an empty user.
   */
  @Test
  public void testEmptyUser() {
    assertTrue(emptyUser.isEmpty());
  }

  /**
   * Test with an non empty user.
   */
  @Test
  public void testEmptyUser1() {
    assertFalse(fullUser.isEmpty());
  }

  /**
   * Test if the password is checked.
   */
  @Test
  public void testCheckPassword() {
    assertTrue(fullUser.checkPassword("test"));
  }

  /**
   * Test if the password is hashed.
   */
  @Test
  public void testHashPassword() {
    assertEquals(fullUser.hashPassword("test"), fullUser.getHashedPassword());
  }


}
