package be.ipl.pae.dal.dao.client;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.dal.services.IDalServices;
import be.ipl.pae.exceptions.FatalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements IClientDao {

  private IDalServices dalServices;
  private IFactory factory;

  public ClientDao(IDalServices dalServices, IFactory factory) {
    this.dalServices = dalServices;
    this.factory = factory;
  }

  /**
   * Finds every clients filtred by their name, post code or city.
   * 
   * @param name search string for name
   * @param postCode search string for post code
   * @param city string of the city
   * @return clients ArrayList of clients
   */
  public List<IClientDto> findClientFiltredBy(String name, String postCode, String city) {
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
    ArrayList<IClientDto> clients = new ArrayList<>();

    String query = "SELECT * FROM project.clients WHERE LOWER(lastname) LIKE ? "
        + " AND LOWER(city) LIKE ? AND postcode LIKE ?";

    try (PreparedStatement retrieveClient = dalServices.getPreparedStatement(query)) {
      retrieveClient.setString(1, '%' + name + '%');
      retrieveClient.setString(2, '%' + city + '%');
      retrieveClient.setString(3, '%' + postCode + '%');
      try (ResultSet rs = retrieveClient.executeQuery()) {
        while (rs.next()) {
          clients.add(fillClientDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new FatalException();
    }
    return clients;
  }

  /**
   * Insert a new client in the database.
   * 
   * @param clientToRegister the client to insert in the database
   */
  public void insert(IClientDto clientToRegister) {
    String query = "INSERT INTO project.clients VALUES (DEFAULT,?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement registerClient = dalServices.getPreparedStatement(query)) {
      registerClient.setString(1, clientToRegister.getLastName());
      registerClient.setString(2, clientToRegister.getFirstName());
      registerClient.setString(3, clientToRegister.getEmail());
      registerClient.setString(4, clientToRegister.getStreet());
      registerClient.setString(5, clientToRegister.getStreetNumber());
      registerClient.setString(6, clientToRegister.getPhoneNumber());
      registerClient.setString(7, clientToRegister.getPostCode());
      registerClient.setString(8, clientToRegister.getMailBox());
      registerClient.setString(9, clientToRegister.getCity());
      registerClient.execute();
    } catch (SQLException sqlException) {
      throw new FatalException();
    }

  }

  /**
   * Find all the clients and returns them in a list.
   * 
   * @return clients a list of all the clients in the database
   */
  @Override
  public List<IClientDto> findClients() {
    String query = "SELECT * FROM project.clients";
    List<IClientDto> clients = new ArrayList<IClientDto>();
    try (PreparedStatement clientQuery = dalServices.getPreparedStatement(query)) {
      try (ResultSet rs = clientQuery.executeQuery()) {
        while (rs.next()) {
          clients.add(fillClientDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return clients;
  }

  /**
   * Find all the clients with no user accounts and returns them in a list.
   *
   * @return clients a list of all the clients that aren't linked to a user account in the database
   */
  @Override
  public List<IClientDto> findClientsWithNoUserAccounts() {
    String query = "SELECT * FROM project.clients WHERE id_client "
        + "NOT IN (SELECT client FROM project.users WHERE client IS NOT NULL )";
    List<IClientDto> clients = new ArrayList<IClientDto>();
    try (PreparedStatement clientQuery = dalServices.getPreparedStatement(query)) {

      try (ResultSet rs = clientQuery.executeQuery()) {
        while (rs.next()) {
          clients.add(fillClientDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return clients;
  }

  /**
   * Find a client by his id.
   * 
   * @param idClient the id in integer of the client.
   * @return client
   */
  @Override
  public IClientDto findClientFiltredById(String idClient) {
    String query = "SELECT * FROM project.clients WHERE id_client = ?";
    int idClientParsed = Integer.parseInt(idClient);
    IClientDto client = null;
    try (PreparedStatement clientQuery = dalServices.getPreparedStatement(query)) {
      clientQuery.setInt(1, idClientParsed);
      try (ResultSet rs = clientQuery.executeQuery()) {
        if (rs.next()) {
          client = fillClientDto(rs);
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }

    return client;
  }


  /**
   * Fills a ClientDto with data from the resultSet in parameter.
   * 
   * @param rs a ResultSet of a PreparedStatement
   * @return ClientDto a filled ClientDto with the information from rs
   */
  private IClientDto fillClientDto(ResultSet rs) {
    IClientDto client = factory.getEmptyClient();
    try {
      client.setIdClient(rs.getInt(1));
      client.setLastName(rs.getString(2));
      client.setFirstName(rs.getString(3));
      client.setEmail(rs.getString(4));
      client.setStreetName(rs.getString(5));
      client.setStreetNumber(rs.getString(6));
      client.setPhoneNumber(rs.getString(7));
      client.setPostCode(rs.getString(8));
      client.setPostBox(rs.getString(9));
      client.setCity(rs.getString(10));
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return client;
  }
}
