package be.ipl.pae.biz.ucc.user;

import be.ipl.pae.biz.user.IUser;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.dal.dao.client.IClientDao;
import be.ipl.pae.dal.dao.user.IUserDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.BizException;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

class UserUcc implements IUserUcc {

  private IUserDao userDao;
  private IDalServicesTransactions dalServiceTransaction;
  private IClientDao clientDao;

  public UserUcc(IUserDao userDao, IDalServicesTransactions dalServicesTransactions,
      IClientDao clientDao) {
    this.userDao = userDao;
    this.clientDao = clientDao;
    this.dalServiceTransaction = dalServicesTransactions;
  }

  /**
   * Checks if provided credentials are correct.
   * 
   * @param username the username input
   * @param password the password input
   * @return the user if credentials are valid, else returns null
   * @throws BizException exception to be catched by servlet when request is not successful
   */
  @Override
  public IUserDto connect(String username, String password) {
    try {

      this.dalServiceTransaction.startTransaction();

      IUser user = (IUser) userDao.findByUsername(username);

      if (user == null || !user.checkPassword(password)) {
        throw new BizException(HttpServletResponse.SC_UNAUTHORIZED);
      }

      if (!user.isConfirmed()) {
        throw new BizException(HttpServletResponse.SC_FORBIDDEN);
      }

      user.setHashedPassword(null);
      return user;

    } catch (Exception excpetion) {
      this.dalServiceTransaction.rollBackTransaction();
      throw excpetion;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Register a user if his username and email are unique.
   * 
   * @param user the user to register
   * @return the user if his email and username are unique
   * @throws BizException exception to be catched by servlet when request is not successful
   */
  public IUserDto register(IUserDto user) {
    try {

      this.dalServiceTransaction.startTransaction();
      if (userDao.findByUsername(user.getUsername()) != null
          || userDao.findByEmail(user.getEmail()) != null) {
        throw new BizException(HttpServletResponse.SC_CONFLICT);
      }

      IUser userBiz = (IUser) user;

      userBiz.setConfirmed(false);
      userBiz.setWorker(false);
      userBiz.setRegistrationDate(LocalDate.now());

      // a modif quand on changera userdto (hashpwd) (1)
      userBiz.setHashedPassword(userBiz.hashPassword(userBiz.getHashedPassword()));

      userDao.insert(userBiz);
      IUserDto userToReturn = userDao.findByUsername(userBiz.getUsername());
      // a modif quand on changera userdto (hashpwd) (2)
      userToReturn.setHashedPassword(null);

      return userToReturn;
    } catch (Exception excpetion) {
      this.dalServiceTransaction.rollBackTransaction();
      throw excpetion;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }

  }

  /**
   * Link a client to a user.
   * 
   * @param idUser the id of the user which we want to link
   * @param idClient the is of the client which we want to link
   * @throws BizException if the client is already linked to a user
   */
  public void linkUserToClient(String idUser, String idClient) {
    try {

      this.dalServiceTransaction.startTransaction();
      IUserDto user = this.userDao.findUserLinked(idClient);
      if (user != null) {
        throw new BizException(HttpServletResponse.SC_CONFLICT);
      }
      this.userDao.linkUserToClient(idUser, idClient);

    } catch (BizException exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;

    } finally {
      this.dalServiceTransaction.commitTransaction();
    }


  }

  /**
   * Returns an ArrayList containing every user filtred by name or city.
   * 
   * @param name search string for name
   * @param city search string for city
   * @return listUser a list of the users filtered by name and city
   */
  public List<IUserDto> retrieveUserFiltredBy(String name, String city) {
    try {

      this.dalServiceTransaction.startTransaction();
      List<IUserDto> listUser = userDao.findUsersFiltredBy(name, city);
      return listUser;
    } catch (Exception exception) {
      exception.printStackTrace();
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Retrieve a list of users not linked.
   * 
   * @return listUser a list of user
   */
  public List<IUserDto> retrieveUserNotLinked() {
    try {

      this.dalServiceTransaction.startTransaction();
      List<IUserDto> listUser = userDao.retrieveUserNotLinked();
      return listUser;

    } catch (Exception exception) {
      exception.printStackTrace();
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Returns an ArrayList containing every user in the system.
   *
   * @return the list containing every users
   */
  @Override
  public List<IUserDto> retrieveAllUsers() {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IUserDto> users = userDao.findAll();
      return users;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }

  }

  /**
   * Returns an ArrayList containing every unconfirmed user in the system.
   *
   * @return the list of unconfirmed users
   */
  @Override
  public List<IUserDto> retrieveAllUnconfirmedUsers() {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IUserDto> unconfirmedUsers = userDao.findEveryUnconfirmedUsers();
      return unconfirmedUsers;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Sets a user's account as a confirmed account.
   *
   * @param email the id of the user
   * @return true if the operation was successful, else false
   */
  @Override
  public boolean setUserAsConfirmed(String email) {
    try {
      this.dalServiceTransaction.startTransaction();
      boolean result = userDao.confirm(email);
      return result;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Sets a user's account as a worker account.
   *
   * @param email the id of the user
   * @return true if the operation was successful, else false
   */
  @Override
  public boolean setUserAsAWorker(String email) {
    if (this.userDao.findByEmail(email) == null) {
      throw new BizException(HttpServletResponse.SC_NOT_FOUND);
    }
    try {
      this.dalServiceTransaction.startTransaction();
      boolean result = userDao.setAsWorker(email);
      return result;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Find a user with the corresponding userId.
   *
   * @param userId the id of the user
   * @return user the user with corresponding id if it exists
   */
  @Override
  public IUser findUserById(String userId) {
    try {
      this.dalServiceTransaction.startTransaction();
      IUser user = (IUser) userDao.findById(userId);
      return user;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Links a client to a user account.
   *
   * @param clientId is the Id of the client to be linked
   * @param userEmail is the email of the user account
   * @return true if the user's account was successfully linked to the client, else false
   */
  @Override
  public boolean linkClientToUser(String clientId, String userEmail) {
    if (this.userDao.findByEmail(userEmail) == null
        || this.clientDao.findClientFiltredById(clientId) == null) {
      throw new BizException(HttpServletResponse.SC_NOT_FOUND);
    }
    try {
      this.dalServiceTransaction.startTransaction();
      boolean result = userDao.linkClientToUser(clientId, userEmail);
      return result;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }
}
