package be.ipl.pae.biz.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.ipl.pae.biz.Factory;
import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.client.Client;
import be.ipl.pae.biz.quote.Quote;
import be.ipl.pae.biz.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FactoryTest {

  IFactory factory;
  User user;
  Quote quote;
  Client client;

  /**
   * Create a new factory, and empty user, client, quote.
   *
   * @throws Exception when user is null
   */
  @BeforeEach
  public void setUp() throws Exception {
    factory = new Factory();
    user = new User();
    quote = new Quote();
    client = new Client();
  }

  /**
   * Test if the factory user is empty.
   */
  @Test
  public void testEmptyUser() {
    assertEquals(user, factory.getEmptyUser());
  }

  /**
   * Test if the factory client is empty.
   */
  @Test
  public void testEmptyClient() {
    assertEquals(client, factory.getEmptyClient());
  }

  /**
   * Test if the factory quote is empty.
   */
  @Test
  public void testEmptyQuote() {
    assertEquals(quote, factory.getEmptyQuote());
  }

}
