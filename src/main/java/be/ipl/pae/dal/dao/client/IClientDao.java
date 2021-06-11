package be.ipl.pae.dal.dao.client;

import be.ipl.pae.biz.client.IClientDto;

import java.util.List;

public interface IClientDao {

  void insert(IClientDto customerToRegister);

  List<IClientDto> findClients();

  List<IClientDto> findClientsWithNoUserAccounts();

  List<IClientDto> findClientFiltredBy(String name, String postCode, String city);

  IClientDto findClientFiltredById(String idClient);


}
