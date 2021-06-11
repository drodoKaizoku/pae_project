package be.ipl.pae.biz.ucc.client;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.client.Client;
import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.dal.MockClientDao;
import be.ipl.pae.dal.MockDalServices;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ClientUccTest {
  private IDalServicesTransactions mockDalServices;
  private MockClientDao mockClientDao;
  private ClientUcc clientUcc;
  private Client clientOk;
  private Client clientKo;
  private Client clientDb;
  private Client clientVide;
  private List<IClientDto> listClient;

  /**
   * Setup object for tests.
   * 
   * @throws Exception setup exceptions
   */
  @BeforeEach
  public void setUp() throws Exception {
    mockClientDao = new MockClientDao();
    mockDalServices = new MockDalServices();
    clientUcc = new ClientUcc(mockDalServices, mockClientDao);
    listClient = new ArrayList<IClientDto>();
    clientOk = new Client(1, "ClientOkLastName", "ClientOkFirstName", "ClientOkStreet",
        "ClientOkSreetNumber", "ClientOkPostBox", "ClientOkCity", "ClientOkPostCode",
        "ClientOkEmail", "ClientOkPhoneNumber");
    clientKo = new Client(2, "ClientKoLastName", "ClientKoFirstName", "ClientKoStreet",
        "ClientKoSreetNumber", "ClientKoPostBox", "ClientKoCity", "ClientKoPostCode",
        "ClientKoEmail", "ClientKoPhoneNumber");
    clientVide = new Client();
    listClient.add(clientOk);

  }

  @Test
  public void testRegister() {
    assertAll(() -> assertThrows(BizException.class, () -> clientUcc.register(clientKo)));
  }

  @Test
  public void testFindClientById() {
    assertAll(() -> assertThrows(FatalException.class, () -> clientUcc.findClientById("test")),
        () -> assertThrows(BizException.class, () -> clientUcc.findClientById("null")),
        () -> assertEquals(clientUcc.findClientById("1"), clientVide));
  }

  @Test
  public void testRetrieveAllClients() {
    assertEquals(clientUcc.retrieveAllClients().getClass(), new ArrayList<IClientDto>().getClass());
  }

  @Test
  public void testRetrieveAllClientsWithNoUserAccounts() {
    assertEquals(clientUcc.retrieveAllClientsWithNoUserAccounts().getClass(),
        new ArrayList<IClientDto>().getClass());
  }

  @Test
  public void testRetrieveClientFiltredBy() {
    assertAll(
        () -> assertEquals(clientUcc.retrieveClientFiltredBy("name", "postCode", "city").getClass(),
            new ArrayList<IClientDto>().getClass()),
        () -> assertNull(clientUcc.retrieveClientFiltredBy("null", "postCode", "city")),
        () -> assertThrows(FatalException.class,
            () -> clientUcc.retrieveClientFiltredBy("name", null, "city")));
  }

  @Test
  public void testClientExist() {
    assertAll(() -> assertTrue(clientUcc.clientExist(clientOk, listClient)),
        () -> assertFalse(clientUcc.clientExist(clientKo, listClient)));
  }
}
