package be.ipl.pae.dal.dao.user;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.dal.services.IDalServices;
import be.ipl.pae.exceptions.FatalException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class UserDao implements IUserDao {

  private IDalServices dalservices;
  private IFactory factory;

  public UserDao(IFactory factory, IDalServices dalservices) {
    this.factory = factory;
    this.dalservices = dalservices;
  }

  /**
   * Finds the user which username corresponds to the username in parameter.
   *
   * @param username a String of the username
   * @return UserDto a UserDto completed or null if no user with this username exists
   */
  @Override
  public IUserDto findByUsername(String username) {
    IUserDto user = null;
    String query = "SELECT * FROM project.users WHERE username = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = fillUserDto(rs);
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return user;
  }


  /**
   * Finds the user which email corresponds to the email in parameter.
   *
   * @param email a String of the email
   * @return UserDto a UserDto completed or null if no user with this email exists
   */
  @Override
  public IUserDto findByEmail(String email) {
    IUserDto user = null;
    String query = "SELECT * FROM project.users WHERE email = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = fillUserDto(rs);
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return user;
  }

  /**
   * Finds the user which id corresponds to the userID in parameter.
   *
   * @param userId the id of the user
   * @return UserDto a UserDto completed or null if no user with this id exists
   */
  @Override
  public IUserDto findById(String userId) {
    IUserDto user = null;
    int id = Integer.parseInt(userId);
    String query = "SELECT * FROM project.users WHERE id_user = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = fillUserDto(rs);
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return user;
  }


  /**
   * Fills a userDto with data from the resultSet in parameter.
   *
   * @param rs a ResultSet of a prepardStatement
   * @return UserDto a filled UserDto with the information from rs
   */
  private IUserDto fillUserDto(ResultSet rs) {
    IUserDto user = factory.getEmptyUser();
    try {
      user.setIdUser(rs.getInt(1));
      user.setUsername(rs.getString(2));
      user.setLastName(rs.getString(3));
      user.setFirstName(rs.getString(4));
      user.setEmail(rs.getString(5));
      user.setCity(rs.getString(6));
      user.setHashedPassword(rs.getString(7));
      user.setWorker(rs.getBoolean(8));
      user.setClient(rs.getString(9));
      user.setConfirmed(rs.getBoolean(10));
      user.setRegistrationDate(rs.getDate(11).toLocalDate());
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return user;
  }

  /**
   * Finds all users and returns them in a list.
   *
   * @return users a list of all users in the database
   */
  public List<IUserDto> findAll() {
    ArrayList<IUserDto> users = new ArrayList<>();
    String query = "SELECT * FROM project.users";
    try (PreparedStatement retrieveUser = dalservices.getPreparedStatement(query)) {
      try (ResultSet rs = retrieveUser.executeQuery()) {
        while (rs.next()) {
          users.add(fillUserDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return users;
  }

  /**
   * Finds every users who isn't linked to a client.
   *
   * @return users, ArrayList of users
   * @throws FatalException if a problem occurred
   */

  public List<IUserDto> retrieveUserNotLinked() {
    ArrayList<IUserDto> users = new ArrayList<>();
    String query = "SELECT * FROM project.users WHERE client is null";
    try (PreparedStatement retrieveUser = dalservices.getPreparedStatement(query)) {
      try (ResultSet rs = retrieveUser.executeQuery()) {
        while (rs.next()) {
          users.add(fillUserDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return users;
  }

  /**
   * Finds every confirmed users filtred by their name or city.
   * 
   * @param name search string for name
   * @param city search string for city
   * @return users, ArrayList of users
   */
  public List<IUserDto> findUsersFiltredBy(String name, String city) {
    if (name == null) {
      name = "";
    } else {
      name = name.toLowerCase();
    }
    if (city == null) {
      city = "";
    } else {
      city = city.toLowerCase();
    }
    ArrayList<IUserDto> users = new ArrayList<>();
    String query = "SELECT * FROM project.users WHERE LOWER(lastname) LIKE ?"
        + " AND LOWER(city) LIKE ? AND confirmed=true ORDER BY registration_date DESC";
    try (PreparedStatement retrieveUser = dalservices.getPreparedStatement(query)) {
      retrieveUser.setString(1, '%' + name + '%');
      retrieveUser.setString(2, '%' + city + '%');

      try (ResultSet rs = retrieveUser.executeQuery()) {
        while (rs.next()) {
          users.add(fillUserDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return users;
  }


  /**
   * Finds every unconfirmed users and returns them in a list.
   *
   * @return users a list of unconfirmed users in the database
   * @throws FatalException if a problem has occurred.
   */
  @Override public List<IUserDto> findEveryUnconfirmedUsers() {
    List<IUserDto> users = new ArrayList<>();
    String query =
        "SELECT * FROM project.users  WHERE confirmed = false ORDER BY registration_date DESC";
    try (PreparedStatement retrieveUser = dalservices.getPreparedStatement(query)) {
      try (ResultSet rs = retrieveUser.executeQuery()) {
        while (rs.next()) {
          users.add(fillUserDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return users;
  }

  /**
   * Finds the user who is linked to the specified client.
   *
   * @param idClient the id of the client
   * @return the user who is linked to the specified client
   * @throws FatalException if a problem occurred
   */

  public IUserDto findUserLinked(String idClient) {

    IUserDto user = null;
    int id = Integer.parseInt(idClient);
    String query = "SELECT * FROM project.users WHERE client = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = fillUserDto(rs);
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return user;
  }



  /**
   * Insert a new user in the database.
   *
   * @param userToRegister the user to insert in the database
   * @throws FatalException if a problem has occurred.
   */
  public void insert(IUserDto userToRegister) {
    Date dateInscription = Date.valueOf(userToRegister.getRegistrationDate());
    String query = "INSERT INTO project.users VALUES (DEFAULT,?,?,?,?,?,?,?,NULL,?,?)";
    try (PreparedStatement registerUser = dalservices.getPreparedStatement(query)) {
      registerUser.setString(1, userToRegister.getUsername());
      registerUser.setString(2, userToRegister.getLastName());
      registerUser.setString(3, userToRegister.getFirstName());
      registerUser.setString(4, userToRegister.getEmail());
      registerUser.setString(5, userToRegister.getCity());
      registerUser.setString(6, userToRegister.getHashedPassword());
      registerUser.setBoolean(7, userToRegister.isWorker());
      registerUser.setBoolean(8, userToRegister.isConfirmed());
      registerUser.setDate(9, dateInscription);
      registerUser.execute();
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Confirms the inscription of the user account wich has the email in parameter.
   * 
   * @param email the user's email
   * @return true if the account was confirmed, else false
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public boolean confirm(String email) {
    String query = "UPDATE project.users SET confirmed = true WHERE email =?";
    try (PreparedStatement confirmedUser = dalservices.getPreparedStatement(query)) {
      confirmedUser.setString(1, email);
      confirmedUser.execute();
      return true;
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Set the user's account as a worker.
   * 
   * @param email of the user's account
   * @return true if the user's account was successfully upgraded to worker account, else false
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public boolean setAsWorker(String email) {
    String query = "UPDATE project.users SET worker = true WHERE email = ?";
    try (PreparedStatement workerUser = dalservices.getPreparedStatement(query)) {
      workerUser.setString(1, email);
      workerUser.execute();
      return true;
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Links a client to a user account.
   *
   * @param client is the Id of the client to be linked
   * @param userEmail is the email of the user account
   * @return true if the user's account was successfully linked to the client, else false
   * @throws FatalException if a problem has occurred.
   */

  @Override
  public boolean linkClientToUser(String client, String userEmail) {
    int clientId = Integer.parseInt(client);
    String query = "UPDATE project.users SET client = ?  WHERE email = ?";
    try (PreparedStatement user = dalservices.getPreparedStatement(query)) {
      user.setInt(1, clientId);
      user.setString(2, userEmail);
      user.execute();
      return true;
    } catch (SQLException sqlException) {
      throw new FatalException();
    }

  }

  /**
   * Links a user to a client
   * 
   * @param idUser the user that we want to link
   * @param idClient the client to link
   * @return true if the user has been linked
   * @throws FatalException if a problem has occurred.
   */
  public boolean linkUserToClient(String idUser, String idClient) {
    int clientId = Integer.parseInt(idClient);
    int userId = Integer.parseInt(idUser);
    String query = "UPDATE project.users SET client = ?  WHERE id_user = ?";
    try (PreparedStatement user = dalservices.getPreparedStatement(query)) {
      user.setInt(1, clientId);
      user.setInt(2, userId);
      user.execute();
      return true;
    } catch (SQLException sqlException) {
      throw new FatalException();
    }

  }
}
