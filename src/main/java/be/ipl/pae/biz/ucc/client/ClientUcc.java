package be.ipl.pae.biz.ucc.client;

import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.dal.dao.client.IClientDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.BizException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class ClientUcc implements IClientUcc {

  private IDalServicesTransactions dalServiceTransaction;
  private IClientDao clientDao;

  /**
   * Constructor of ClientUcc.
   * 
   * @param dalServiceTransaction object to do transactions
   * @param clientDao object from DAL to communicate with DB
   */
  public ClientUcc(IDalServicesTransactions dalServiceTransaction, IClientDao clientDao) {
    this.dalServiceTransaction = dalServiceTransaction;
    this.clientDao = clientDao;
  }

  /**
   * Retrieve an array of clients.
   * 
   * @param name search string for name
   * @param postCode search string for post code
   * @param city search string for city
   * @return listClient, an ArrayList of clients
   * @throw Exception if error while getting clients from DB
   */
  public List<IClientDto> retrieveClientFiltredBy(String name, String postCode, String city) {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IClientDto> listClient = clientDao.findClientFiltredBy(name, postCode, city);
      return listClient;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Register the client.
   * 
   * @param client to register
   * @throws BizException if the client already exists
   */
  public void register(IClientDto client) {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IClientDto> listClient = retrieveAllClients();
      if (clientExist(client, listClient)) {
        throw new BizException(HttpServletResponse.SC_CONFLICT);
      }
      clientDao.insert(client);
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Find a client with the corresponding clientid.
   * 
   * @param idClient the id of client
   * @return client the client with corresponding id if it exists
   * @throws BizException if the client is not found in database
   */
  public IClientDto findClientById(String idClient) {
    IClientDto client = null;
    try {
      this.dalServiceTransaction.startTransaction();
      client = this.clientDao.findClientFiltredById(idClient);
      if (client == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      }
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
    return client;
  }

  /**
   * Retrieve all the clients in the database.
   * 
   * @return listClient containing all the clients in the database
   */
  public List<IClientDto> retrieveAllClients() {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IClientDto> listClient = clientDao.findClients();
      return listClient;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  @Override
  /**
   * Retrieve all the clients that arent't linked to a user account in the database.
   *
   * @return listClient containing all the clients in the database that arent't linked to a user
   *         account
   */
  public List<IClientDto> retrieveAllClientsWithNoUserAccounts() {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IClientDto> listClient = clientDao.findClientsWithNoUserAccounts();
      return listClient;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }


  /**
   * Check if the client already exists in the database.
   * 
   * @return true if the client already exists in the database
   */
  public boolean clientExist(IClientDto client, List<IClientDto> listClient) {
    String email = client.getEmail();
    String phoneNumber = client.getPhoneNumber();
    for (IClientDto cl : listClient) {
      if (cl.getPhoneNumber().equals(phoneNumber) && cl.getEmail().equals(email)) {
        return true;
      }
    }
    return false;
  }

}
