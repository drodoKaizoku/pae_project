package be.ipl.pae.biz.client;



public interface IClientDto {

  int getIdClient();

  String getLastName();

  String getFirstName();

  String getStreet();

  String getStreetNumber();

  String getMailBox();

  String getCity();

  String getPostCode();

  String getEmail();

  String getPhoneNumber();

  void setIdClient(int idClient);

  void setLastName(String lastName);

  void setFirstName(String firstName);

  void setStreetName(String street);

  void setStreetNumber(String streetNumber);

  void setPostBox(String mailBox);

  void setCity(String city);

  void setPostCode(String postCode);

  void setEmail(String email);

  void setPhoneNumber(String phoneNumber);
}
