package be.ipl.pae.biz.user;

public interface IUser extends IUserDto {

  String hashPassword(String password);

  boolean checkPassword(String password);

}
