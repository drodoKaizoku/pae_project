package be.ipl.pae.biz.user;

import org.mindrot.bcrypt.BCrypt;

import java.time.LocalDate;

public class User implements IUser {

  private int idUser;
  private String username;
  private String lastName;
  private String firstName;
  private String city;
  private String email;
  private String hashedPassword;
  private LocalDate registrationDate;
  private boolean worker;
  private boolean confirmed;
  private String client;

  /**
   * User Constructor containing all the fields.
   * 
   * @param idUser the id of the user
   * @param username the login of the user
   * @param lastName the last name of the user
   * @param firstName the first name of the user
   * @param city the city of the user
   * @param email the email of the user
   * @param hashedPassword the hashed password of the user
   * @param registrationDate the registration date of the user
   * @param worker a boolean that tells if the user is a worker
   * @param confirmed a boolean that tells if the user is confirmed
   */

  public User(int idUser, String username, String lastName, String firstName, String city,
      String email, String hashedPassword, LocalDate registrationDate, boolean worker,
      boolean confirmed) {
    super();
    this.idUser = idUser;
    this.username = username;
    this.lastName = lastName;
    this.firstName = firstName;
    this.city = city;
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.registrationDate = registrationDate;
    this.worker = worker;
    this.confirmed = confirmed;
  }

  public User() {}

  public boolean isEmpty() {
    return this.equals(new User());
  }

  /**
   * Hashes the password given in parameter.
   * 
   * @param password the password to be hashed using BCrypt algorithm
   * @return the hashed password
   */
  @Override
  public String hashPassword(String password) {
    return this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
  }

  /**
   * Check if the password is the same as the hashed one.
   * 
   * @param password the password
   * @true if the password is the same as the hashed one
   */
  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, hashedPassword);
  }

  @Override
  public int getIdUser() {
    return idUser;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getHashedPassword() {
    return hashedPassword;
  }

  @Override
  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  @Override
  public boolean isWorker() {
    return worker;
  }

  @Override
  public boolean isConfirmed() {
    return confirmed;
  }

  @Override
  public void setWorker(boolean worker) {
    this.worker = worker;
  }

  @Override
  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  @Override
  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  @Override
  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  @Override
  public String toString() {
    return "User [idUser=" + idUser + ", username=" + username + ", lastName=" + lastName
        + ", firstName=" + firstName + ", city=" + city + ", email=" + email + ", hashedPassword="
        + hashedPassword + ", registrationDate=" + registrationDate + ", worker=" + worker
        + ", confirmed=" + confirmed + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + (confirmed ? 1231 : 1237);
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((hashedPassword == null) ? 0 : hashedPassword.hashCode());
    result = prime * result + idUser;
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    result = prime * result + (worker ? 1231 : 1237);
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
    User other = (User) obj;
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else if (!city.equals(other.city)) {
      return false;
    }
    if (confirmed != other.confirmed) {
      return false;
    }
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!email.equals(other.email)) {
      return false;
    }
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }
    if (hashedPassword == null) {
      if (other.hashedPassword != null) {
        return false;
      }
    } else if (!hashedPassword.equals(other.hashedPassword)) {
      return false;
    }

    if (idUser != other.idUser) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }

    } else if (!lastName.equals(other.lastName)) {
      return false;
    }

    if (registrationDate == null) {
      if (other.registrationDate != null) {
        return false;
      }

    } else if (!registrationDate.equals(other.registrationDate)) {
      return false;
    }

    if (username == null) {
      if (other.username != null) {
        return false;
      }
    } else if (!username.equals(other.username)) {
      return false;
    }

    if (worker != other.worker) {
      return false;
    }

    return true;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

}
