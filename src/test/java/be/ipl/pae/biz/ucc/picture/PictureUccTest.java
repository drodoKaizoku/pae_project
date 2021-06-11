package be.ipl.pae.biz.ucc.picture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.biz.pictures.Picture;
import be.ipl.pae.dal.MockDalServices;
import be.ipl.pae.dal.MockPictureDao;
import be.ipl.pae.dal.MockQuoteDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PictureUccTest {
  private PictureUcc pictureUcc;
  private IDalServicesTransactions mockDalServices;
  private MockPictureDao mockPictureDao;
  private MockQuoteDao mockQuoteDao;
  private IPictureDto pictureVide;

  /**
   * Set up objects for tests.
   */
  @BeforeEach
  public void setUp() {
    mockDalServices = new MockDalServices();
    mockPictureDao = new MockPictureDao();
    mockQuoteDao = new MockQuoteDao();
    pictureUcc = new PictureUcc(mockPictureDao, mockDalServices, mockQuoteDao);
    pictureVide = new Picture();
  }

  @Test
  public void testInsertAfterPicture() {
    ArrayList<IPictureDto> list = new ArrayList<IPictureDto>();
    list.add(pictureVide);
    assertThrows(FatalException.class, () -> pictureUcc.insertAfterPicture(list, 1));
  }

  @Test
  public void testSetPictureToVisible() {
    ArrayList<String> list = new ArrayList<String>();
    list.add(null);
    assertThrows(FatalException.class, () -> pictureUcc.setPictureToVisible(list, 1));
    list.remove(0);
    list.add("1");
    assertThrows(FatalException.class, () -> pictureUcc.setPictureToVisible(list, 2));
  }

  @Test
  public void testRetrieveEveryVisiblePicture() {
    assertEquals(new ArrayList<IPictureDto>(), pictureUcc.retrieveEveryVisiblePicture());

  }

  @Test
  public void testRetrieveEveryVisiblePictureOfAType() {
    assertEquals(new ArrayList<IPictureDto>(), pictureUcc.retrieveEveryVisiblePictureOfAType("1"));


  }
}
