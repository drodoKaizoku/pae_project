package be.ipl.pae.biz.ucc.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.biz.user.User;
import be.ipl.pae.dal.MockClientDao;
import be.ipl.pae.dal.MockDalServices;
import be.ipl.pae.dal.MockUserDao;
import be.ipl.pae.dal.dao.client.IClientDao;
import be.ipl.pae.dal.dao.user.IUserDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.BizException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UserUccTest {
  private UserUcc userUcc;
  private IUserDao mockUserDao;
  private IClientDao mockClientDao;
  private IDalServicesTransactions mockDalServices;
  private IUserDto userOk;
  private IUserDto userUsernameNull;
  private IUserDto userMailNull;

  /**
   * Setup object for tests.
   * 
   * @throws Exception setup exceptions
   */
  @BeforeEach
  public void setUp() {
    mockUserDao = new MockUserDao();
    mockClientDao = new MockClientDao();
    mockDalServices = new MockDalServices();
    userUcc = new UserUcc(mockUserDao, mockDalServices, mockClientDao);

    userOk = new User(1, "test", "test", "test", "test", "test",
        "$2a$12$HVP8lYGebd/vCxmYGWJWAuyhYPjjQVCXpkrFQdE.45WVeSNIDpSVW", null, false, true);
    userUsernameNull = new User(1, null, "test", "test", "test", "test",
        "$2a$12$HVP8lYGebd/vCxmYGWJWAuyhYPjjQVCXpkrFQdE.45WVeSNIDpSVW", null, false, true);
    userMailNull = new User(1, "test", "test", "test", "test", null,
        "$2a$12$HVP8lYGebd/vCxmYGWJWAuyhYPjjQVCXpkrFQdE.45WVeSNIDpSVW", null, true, true);
  }

  @Test
  public void testConstructor() {
    assertTrue(true);
  }

  @Test
  public void testConnect() {
    assertAll(() -> assertThrows(BizException.class, () -> userUcc.connect(null, "test")),
        () -> assertThrows(BizException.class, () -> userUcc.connect("test", null)),
        () -> assertThrows(BizException.class, () -> userUcc.connect("notConfirmed", "test")),
        () -> assertEquals(null, userUcc.connect("test", "test").getHashedPassword())

    );
  }

  @Test
  public void testRegister() {
    assertAll(() -> assertThrows(BizException.class, () -> userUcc.register(userUsernameNull)),
        () -> assertThrows(BizException.class, () -> userUcc.register(userMailNull)),
        () -> assertThrows(BizException.class, () -> userUcc.register(userUsernameNull)));
  }

  @Test
  public void testRetrieveUserFiltredBy() {
    assertAll(() -> assertEquals(2, userUcc.retrieveUserFiltredBy("2", null).size()));
  }

  @Test
  public void testRetrieveAllUsers() {
    assertTrue(userUcc.retrieveAllUsers().getClass().equals(ArrayList.class));
  }

  @Test
  public void testRetrieveAllUncofirmedUsers() {
    assertTrue(userUcc.retrieveAllUnconfirmedUsers().getClass().equals(ArrayList.class));
  }

  @Test
  public void testLinkUserToClient() {
    assertAll(() -> assertThrows(BizException.class, () -> userUcc.linkUserToClient("1", "test")));
  }

  @Test
  public void testSetUserAsConfirmed() {
    assertAll(() -> assertTrue(userUcc.setUserAsConfirmed("true")));
  }

  @Test
  public void testSetUserAsWorker() {
    assertAll(() -> assertTrue(userUcc.setUserAsAWorker("true")));
  }

  @Test
  public void testFindUserById() {
    assertAll(() -> assertEquals(null, userUcc.findUserById("null")),
        () -> assertEquals(userOk, userUcc.findUserById("ok")));
  }


  @Test
  public void testLinkClientToUser() {
    assertAll(() -> assertTrue(userUcc.linkClientToUser("1", "email")),
        () -> assertThrows(BizException.class, () -> userUcc.linkClientToUser("1", null)));
  }


  @Test
  public void testRetrieveUserNotLinked() {
    assertTrue(userUcc.retrieveUserNotLinked().getClass().equals(ArrayList.class));
  }
}
