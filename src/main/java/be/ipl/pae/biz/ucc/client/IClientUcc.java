package be.ipl.pae.biz.ucc.client;

import be.ipl.pae.biz.client.IClientDto;

import java.util.List;

public interface IClientUcc {

  void register(IClientDto client);

  List<IClientDto> retrieveAllClients();

  /**
   * Retrieve all the clients that arent't linked to a user account in the database.
   *
   * @return listClient containing all the clients in the database that arent't linked to a user
   *         account
   */
  List<IClientDto> retrieveAllClientsWithNoUserAccounts();

  boolean clientExist(IClientDto client, List<IClientDto> listClient);

  List<IClientDto> retrieveClientFiltredBy(String name, String postCode, String city);

  IClientDto findClientById(String idClient);
}
