package be.ipl.pae.biz.client;

public class Client implements IClientDto {

  private int idClient;
  private String lastName;
  private String firstName;
  private String streetName;
  private String streetNumber;
  private String postBox;
  private String city;
  private String postCode;
  private String email;
  private String phoneNumber;

  /**
   * Client constructor containing all the fields.
   * 
   * @param idClient the id of the client
   * @param lastName the last name of the client
   * @param firstName the first name of the client
   * @param street the street name of the client
   * @param streetNumber the street number of the client
   * @param postBox the post box of the client
   * @param city the city of the client
   * @param postCode the post code of the client
   * @param email the email adress of the client
   * @param phoneNumber the phone number of the client
   */
  public Client(int idClient, String lastName, String firstName, String street, String streetNumber,
      String postBox, String city, String postCode, String email, String phoneNumber) {
    this.idClient = idClient;
    this.lastName = lastName;
    this.firstName = firstName;
    this.streetName = street;
    this.streetNumber = streetNumber;
    this.postBox = postBox;
    this.city = city;
    this.postCode = postCode;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public Client() {

  }


  public int getIdClient() {
    return idClient;
  }


  public String getLastName() {
    return lastName;
  }


  public String getFirstName() {
    return firstName;
  }


  public String getStreet() {
    return streetName;
  }


  public String getStreetNumber() {
    return streetNumber;
  }


  public String getMailBox() {
    return postBox;
  }


  public String getCity() {
    return city;
  }


  public String getPostCode() {
    return postCode;
  }


  public String getEmail() {
    return email;
  }


  public String getPhoneNumber() {
    return phoneNumber;
  }


  public void setIdClient(int idClient) {
    this.idClient = idClient;
  }


  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  public void setStreetName(String street) {
    this.streetName = street;
  }


  public void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }


  public void setPostBox(String mailBox) {
    this.postBox = mailBox;
  }


  public void setCity(String city) {
    this.city = city;
  }


  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }


  public void setEmail(String email) {
    this.email = email;
  }


  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + idClient;
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
    result = prime * result + ((postBox == null) ? 0 : postBox.hashCode());
    result = prime * result + ((postCode == null) ? 0 : postCode.hashCode());
    result = prime * result + ((streetName == null) ? 0 : streetName.hashCode());
    result = prime * result + ((streetNumber == null) ? 0 : streetNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Client other = (Client) obj;
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else {
      if (!city.equals(other.city)) {
        return false;
      }
    }
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else {
      if (!email.equals(other.email)) {
        return false;
      }
    }
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else {
      if (!firstName.equals(other.firstName)) {
        return false;
      }
    }
    if (idClient != other.idClient) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else {
      if (!lastName.equals(other.lastName)) {
        return false;
      }
    }
    if (phoneNumber == null) {
      if (other.phoneNumber != null) {
        return false;
      }
    } else {
      if (!phoneNumber.equals(other.phoneNumber)) {
        return false;
      }
    }
    if (postBox == null) {
      if (other.postBox != null) {
        return false;
      }
    } else {
      if (!postBox.equals(other.postBox)) {
        return false;
      }
    }
    if (postCode == null) {
      if (other.postCode != null) {
        return false;
      }
    } else {
      if (!postCode.equals(other.postCode)) {
        return false;
      }
    }
    if (streetName == null) {
      if (other.streetName != null) {
        return false;
      }
    } else {
      if (!streetName.equals(other.streetName)) {
        return false;
      }
    }
    if (streetNumber == null) {
      if (other.streetNumber != null) {
        return false;
      }
    } else {
      if (!streetNumber.equals(other.streetNumber)) {
        return false;
      }
    }
    return true;
  }



}
