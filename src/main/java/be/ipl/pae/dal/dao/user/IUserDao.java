package be.ipl.pae.dal.dao.user;

import be.ipl.pae.biz.user.IUserDto;

import java.util.List;

public interface IUserDao {

  IUserDto findByUsername(String username);

  IUserDto findByEmail(String email);

  IUserDto findById(String userId);

  List<IUserDto> findAll();

  List<IUserDto> findEveryUnconfirmedUsers();

  void insert(IUserDto userToRegister);

  boolean confirm(String userId);

  boolean setAsWorker(String userId);

  List<IUserDto> findUsersFiltredBy(String name, String city);

  boolean linkClientToUser(String client, String userEmail);

  boolean linkUserToClient(String idUser, String idClient);

  IUserDto findUserLinked(String idClient);

  List<IUserDto> retrieveUserNotLinked();
}
