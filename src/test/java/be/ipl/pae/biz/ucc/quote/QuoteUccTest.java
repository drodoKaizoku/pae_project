package be.ipl.pae.biz.ucc.quote;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.quote.IQuoteDto;
import be.ipl.pae.biz.quote.Quote;
import be.ipl.pae.dal.MockDalServices;
import be.ipl.pae.dal.MockQuoteDao;
import be.ipl.pae.dal.dao.quote.IQuoteDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

public class QuoteUccTest {
  private QuoteUcc quoteUcc;
  private IQuoteDao mockQuoteDao;
  private IDalServicesTransactions mockDalServices;
  private IQuoteDto quoteVide;
  private IQuoteDto quoteOk;
  private IQuoteDto quoteKo;

  /**
   * Setup object for tests.
   * 
   * @throws Exception setup exceptions
   */
  @BeforeEach
  public void setUp() {
    mockQuoteDao = new MockQuoteDao();
    mockDalServices = new MockDalServices();
    quoteUcc = new QuoteUcc(mockQuoteDao, mockDalServices);
    quoteVide = new Quote();
    quoteOk = new Quote(1, LocalDate.now(), 1, null, null, null, null, 1578, LocalDate.now(), null);
    quoteKo = new Quote(0, LocalDate.MIN, 0, null, null, null, null, 37, LocalDate.MIN, null);
  }


  @Test
  public void testIntroduceQuote() {
    assertAll(() -> assertThrows(FatalException.class, () -> quoteUcc.introduceQuote(null, null)),
        () -> assertEquals(quoteOk, quoteUcc.introduceQuote(quoteOk, null)),
        () -> assertNotEquals(quoteOk, quoteUcc.introduceQuote(quoteKo, null)));
  }



  @Test
  public void testGetAllQuotes() {
    assertAll(
        () -> assertThrows(FatalException.class,
            () -> quoteUcc.getAllQuotes(String.valueOf(quoteOk.getClient()), "0", null, 0,
                quoteOk.getDateQuote().toString(), new ArrayList<String>())),
        () -> assertThrows(FatalException.class,
            () -> quoteUcc.getAllQuotes(String.valueOf(quoteOk.getClient()), "0", "0",
                (int) quoteOk.getFullAmount(), null, new ArrayList<String>())),
        () -> assertThrows(FatalException.class,
            () -> quoteUcc.getAllQuotes(String.valueOf(quoteOk.getClient()), "0", "0",
                (int) quoteOk.getFullAmount(), quoteOk.getDateQuote().toString(), null)),
        () -> assertEquals(quoteOk,
            quoteUcc.getAllQuotes(String.valueOf(quoteOk.getClient()), "0", "1",
                (int) quoteOk.getFullAmount(), quoteOk.getDateQuote().toString(),
                new ArrayList<String>()).get(0)));
  }

  @Test
  public void testGetClientQuotes() {
    assertAll(() -> assertThrows(FatalException.class, () -> quoteUcc.getClientQuotes(null)),
        () -> assertEquals(new ArrayList<IQuoteDto>(), quoteUcc.getClientQuotes("0")),
        () -> assertEquals(quoteOk,
            quoteUcc.getClientQuotes(String.valueOf(quoteOk.getClient())).get(0)));
  }

  @Test
  public void testConfirmDateStartWork() {
    assertAll(() -> assertThrows(BizException.class, () -> quoteUcc
        .confirmDateStartWork(quoteKo.getClient(), quoteKo.getDateStartWork(), null)));
  }



  @Test
  public void testSetQuoteState() {
    assertAll(() -> assertThrows(BizException.class, () -> quoteUcc.setQuoteState(0, "ok")),
        () -> assertThrows(BizException.class, () -> quoteUcc.setQuoteState(1, "ok")),
        () -> assertThrows(FatalException.class, () -> quoteUcc.setQuoteState(2, ""))

    );
  }

  @Test
  public void testGetQuote() {
    assertAll(() -> assertEquals(quoteVide, quoteUcc.getQuote(12).get(0)));
  }

  @Test
  public void testAddFavoritePicture() {
    assertAll(() -> assertThrows(BizException.class, () -> quoteUcc.addFavoritePicture(1, 0)));
  }

  @Test
  public void testSetDateWork() {
    assertAll(
        () -> assertThrows(BizException.class, () -> quoteUcc.setDateStartWork(0, LocalDate.MAX)));
  }

  @Test
  public void testSetDateNull() {
    assertAll(() -> assertThrows(BizException.class, () -> quoteUcc.setDateNull(0)));
  }


}
