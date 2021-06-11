package be.ipl.pae.dal;

import be.ipl.pae.biz.client.Client;
import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.dal.dao.client.IClientDao;
import be.ipl.pae.exceptions.FatalException;

import java.util.ArrayList;
import java.util.List;

public class MockClientDao implements IClientDao {

  @Override
  public void insert(IClientDto customerToRegister) {
    if (customerToRegister.getCity() == null || customerToRegister.getEmail() == null
        || customerToRegister.getFirstName() == null || customerToRegister.getLastName() == null
        || customerToRegister.getMailBox() == null || customerToRegister.getPhoneNumber() == null
        || customerToRegister.getPostCode() == null || customerToRegister.getStreet() == null
        || customerToRegister.getStreetNumber() == null) {
      throw new FatalException();
    }
  }

  @Override
  public List<IClientDto> findClients() {
    // TODO Auto-generated method stub
    List<IClientDto> list = new ArrayList<IClientDto>();
    list.add(new Client(2, "ClientKoLastName", "ClientKoFirstName", "ClientKoStreet",
        "ClientKoSreetNumber", "ClientKoPostBox", "ClientKoCity", "ClientKoPostCode",
        "ClientKoEmail", "ClientKoPhoneNumber"));
    return list;
  }

  @Override
  public List<IClientDto> findClientsWithNoUserAccounts() {
    // TODO Auto-generated method stub
    return new ArrayList<IClientDto>();
  }

  @Override
  public List<IClientDto> findClientFiltredBy(String name, String postCode, String city) {
    // TODO Auto-generated method stub
    if (name.equals("null")) {
      return null;
    }
    if (postCode == null) {
      throw new FatalException();
    }

    return new ArrayList<IClientDto>();
  }

  @Override
  public IClientDto findClientFiltredById(String idClient) {
    if (idClient.equals("null")) {
      return null;
    }
    try {
      Integer.parseInt(idClient);
    } catch (Exception exc) {
      throw new FatalException();
    }
    return new Client();
  }

}
