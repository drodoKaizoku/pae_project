package be.ipl.pae.biz.user;

import java.time.LocalDate;

public interface IUserDto {

  int getIdUser();

  String getUsername();

  String getLastName();

  String getFirstName();

  String getCity();

  String getEmail();

  String getHashedPassword();

  LocalDate getRegistrationDate();

  boolean isWorker();

  boolean isConfirmed();

  void setWorker(boolean worker);

  void setConfirmed(boolean confirmed);

  void setIdUser(int idUser);

  void setUsername(String username);

  void setLastName(String lastName);

  void setFirstName(String firstName);

  void setCity(String city);

  void setEmail(String email);

  void setHashedPassword(String hashedPassword);

  void setRegistrationDate(LocalDate registrationDate);
  
  void setClient(String client);
  
  String getClient();

}
