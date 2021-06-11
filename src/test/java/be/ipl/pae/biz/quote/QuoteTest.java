package be.ipl.pae.biz.quote;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class QuoteTest {
  private Quote emptyQuote;
  private Quote fullQuote;
  LocalDate dateOk;
  LocalDate dateKo;
  String workPeriod = "1 month";
  double fullAmount = 530.12;
  int quoteId = 3;

  /**
   * Create two user one empty, and a full one.
   * 
   * @throws Exception when fullUser is null
   */
  @BeforeEach
  public void setUp() throws Exception {
    this.emptyQuote = new Quote();
    dateOk = LocalDate.now();
    dateKo = LocalDate.MAX;
    this.fullQuote =
        new Quote(3, dateOk, 1, null, null, null, workPeriod, fullAmount, dateOk, null);
  }

  /**
   * Test with an empty quote.
   */
  @Test
  public void testEmptyQuote() {
    assertAll(() -> assertTrue(emptyQuote.getDateQuote() == null),
        () -> assertTrue(emptyQuote.getDateStartWork() == null),
        () -> assertTrue(emptyQuote.getFullAmount() == 0),
        () -> assertTrue(emptyQuote.getIdQuote() == 0),
        () -> assertTrue(emptyQuote.getWorkPeriod() == null));
  }

  /**
   * Test with an non empty quote.
   */
  @Test
  public void testEmptyQuote1() {
    assertAll(() -> assertTrue(fullQuote.getDateQuote().equals(dateOk)),
        () -> assertFalse(fullQuote.getDateQuote().equals(dateKo)),
        () -> assertTrue(fullQuote.getDateStartWork().equals(dateOk)),
        () -> assertFalse(fullQuote.getDateStartWork().equals(dateKo)),
        () -> assertTrue(fullQuote.getFullAmount() == fullAmount),
        () -> assertTrue(fullQuote.getIdQuote() == quoteId),
        () -> assertTrue(fullQuote.getWorkPeriod().equals(workPeriod)));
  }
}
