package be.ipl.pae.biz.ucc.type;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.dal.MockDalServices;
import be.ipl.pae.dal.MockProjectTypeDao;
import be.ipl.pae.dal.dao.type.IProjectTypeDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TypeUccTest {
  private IDalServicesTransactions mockDalServices;
  private IProjectTypeDao projectTypeDao;
  private ProjectTypeUcc projectTypeUcc;

  /**
   * Setup object for tests.
   * 
   */
  @BeforeEach
  public void setUp() {
    mockDalServices = new MockDalServices();
    projectTypeDao = new MockProjectTypeDao();
    projectTypeUcc = new ProjectTypeUcc(mockDalServices, projectTypeDao);

  }

  @Test
  public void testRetrieveAllTypes() {
    assertEquals(projectTypeUcc.retrieveAllTypes().getClass(),
        new ArrayList<IClientDto>().getClass());
  }

  @Test
  public void testRetrieveAllTypesofAQuote() {
    assertAll(
        () -> assertEquals(projectTypeUcc.retrieveAllTypesOfAQuote("1").getClass(),
            new ArrayList<IClientDto>().getClass()),
        () -> assertNull(projectTypeUcc.retrieveAllTypesOfAQuote("null")));
  }

}
