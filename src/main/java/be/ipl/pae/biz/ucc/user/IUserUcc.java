package be.ipl.pae.biz.ucc.user;

import be.ipl.pae.biz.user.IUser;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.exceptions.BizException;

import java.util.List;

public interface IUserUcc {

  IUserDto connect(String username, String password) throws BizException;

  IUserDto register(IUserDto user) throws BizException;

  List<IUserDto> retrieveAllUsers();

  List<IUserDto> retrieveAllUnconfirmedUsers();

  boolean setUserAsConfirmed(String email);

  boolean setUserAsAWorker(String email);

  IUser findUserById(String userId);

  List<IUserDto> retrieveUserFiltredBy(String name, String city);

  boolean linkClientToUser(String clientId, String userEmail);

  void linkUserToClient(String idUser, String idClient) throws BizException;

  List<IUserDto> retrieveUserNotLinked();

}
