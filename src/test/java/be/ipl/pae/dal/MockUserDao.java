package be.ipl.pae.dal;

import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.biz.user.User;
import be.ipl.pae.dal.dao.user.IUserDao;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements IUserDao {

  User userOk;

  public MockUserDao() {
    userOk = new User(1, "test", "test", "test", "test", "test",
        "$2a$12$HVP8lYGebd/vCxmYGWJWAuyhYPjjQVCXpkrFQdE.45WVeSNIDpSVW", null, false, true);
  }

  @Override
  public IUserDto findByUsername(String username) {
    // TODO Auto-generated method stub
    if (username == null) {
      return null;
    }
    if (username.equals("notConfirmed")) {
      return new User(1, username, "test", "test", "test", "test",
          "$2a$12$HVP8lYGebd/vCxmYGWJWAuyhYPjjQVCXpkrFQdE.45WVeSNIDpSVW", null, false, false);
    }
    return userOk;
  }

  @Override
  public IUserDto findByEmail(String email) {
    if (email == null) {
      return null;
    }
    return userOk;
  }

  @Override
  public IUserDto findById(String userId) {
    if (userId.equals("null")) {
      return null;
    }
    return new User(1, "test", "test", "test", "test", "test",
        "$2a$12$HVP8lYGebd/vCxmYGWJWAuyhYPjjQVCXpkrFQdE.45WVeSNIDpSVW", null, false, true);

  }

  @Override
  public List<IUserDto> findAll() {
    // TODO Auto-generated method stub
    return new ArrayList<IUserDto>();
  }

  @Override
  public List<IUserDto> findEveryUnconfirmedUsers() {
    // TODO Auto-generated method stub
    return new ArrayList<IUserDto>();
  }

  @Override
  public void insert(IUserDto userToRegister) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean confirm(String userId) {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean setAsWorker(String userId) {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public List<IUserDto> findUsersFiltredBy(String name, String city) {
    List<IUserDto> list = new ArrayList<IUserDto>();
    if (name.equals("2")) {
      list.add(new User());
      list.add(new User());
    }
    return list;
  }

  @Override
  public boolean linkClientToUser(String client, String userEmail) {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean linkUserToClient(String idUser, String idClient) {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IUserDto findUserLinked(String idClient) {
    if (idClient.equals("null")) {
      return null;
    }
    return new User();
  }

  @Override
  public List<IUserDto> retrieveUserNotLinked() {
    // TODO Auto-generated method stub
    return new ArrayList<IUserDto>();
  }

}
