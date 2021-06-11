package be.ipl.pae.biz.ucc.quote;

import be.ipl.pae.biz.quote.IQuoteDto;
import be.ipl.pae.biz.quote.State;
import be.ipl.pae.dal.dao.quote.IQuoteDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.BizException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class QuoteUcc implements IQuoteUcc {
  private IQuoteDao quoteDao;
  private IDalServicesTransactions dalServiceTransaction;


  /**
   * Constructor of QuoteUcc, All the fields are not initialized inside the class in order to reduce
   * dependencies.
   * 
   */
  public QuoteUcc(IQuoteDao quoteDao, IDalServicesTransactions dalServiceTransaction) {
    this.quoteDao = quoteDao;
    this.dalServiceTransaction = dalServiceTransaction;
  }

  /**
   * Insert a new quote.
   * 
   * @param quote object quote to insert
   * @param listType list of type id to insert
   * @return quoteToInsert the quote who has been inserted
   */
  @Override
  public IQuoteDto introduceQuote(IQuoteDto quote, List<String> listType) {
    try {
      this.dalServiceTransaction.startTransaction();
      IQuoteDto quoteToInsert = quoteDao.insertQuote(quote, listType);
      return quoteToInsert;
    } catch (Exception exc) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exc;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Retrieve a list of the quotes filtered.
   * 
   * @param idClient the id of client
   * @param client the name of the client
   * @param minPrice the minimum price of the quotes to search by default equals 0
   * @param maxPrice maximum price of the quotes to search
   * @param date the quotes dates to search
   * @param listProject list of type to search
   * @return listOfQuote the list of quotes who has been found
   */
  @Override
  public List<IQuoteDto> getAllQuotes(String idClient, String client, String minPrice, int maxPrice,
      String date, ArrayList<String> listProject) {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IQuoteDto> listOfQuote =
          quoteDao.findAll(idClient, client, minPrice, maxPrice, date, listProject);
      return listOfQuote;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Retrieve a list of quote from a client.
   * 
   * @param idClient the id of the client
   * @return listOfQuotes a list of quotes
   */
  @Override
  public List<IQuoteDto> getClientQuotes(String idClient) {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IQuoteDto> listOfQuotes = quoteDao.findClientQuotes(idClient);
      return listOfQuotes;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  @Override
  public void confirmDateStartWork(int quoteId, LocalDate dateDebut, String state) {
    try {

      this.dalServiceTransaction.startTransaction();
      if (quoteDao.findQuotebyId(quoteId) == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      } else {
        quoteDao.confirmStartDate(quoteId, dateDebut, state);
      }
    } catch (Exception excpetion) {
      this.dalServiceTransaction.rollBackTransaction();
      throw excpetion;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Set the state of quote with the corresponding quoteId.
   *
   * @param idQuote the id of quote
   * @param state the new state of quote
   * @throws BizException if quote is not found in database
   */
  public void setQuoteState(int idQuote, String state) {
    try {
      this.dalServiceTransaction.startTransaction();
      IQuoteDto quote = quoteDao.findQuotebyId(idQuote);

      if (quote == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      }
      if (quote.getState().getValue().equals(State.ORDER_CONFIRMED.getValue())
          && quote.getDateStartWork() == null) {

        throw new BizException(HttpServletResponse.SC_FORBIDDEN);
      }
      this.quoteDao.setStatutQuote(idQuote, state);
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Retrieve a quote by his id.
   * 
   * @param idQuote the id of the quote we are looking for
   * @return list a list of one quote
   */
  @Override
  public List<IQuoteDto> getQuote(int idQuote) {
    // TODO Auto-generated method stub
    try {
      this.dalServiceTransaction.startTransaction();
      IQuoteDto quote = this.quoteDao.findQuotebyId(idQuote);
      List<IQuoteDto> list = new ArrayList<>();
      list.add(quote);
      return list;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Add a favorite picture to a quote.
   * 
   * @param idPicture the id of the picture
   * @param idQuote the id of the quote
   */
  @Override
  public void addFavoritePicture(int idPicture, int idQuote) {
    try {
      this.dalServiceTransaction.startTransaction();
      if (this.quoteDao.findQuotebyId(idQuote) == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      }
      this.quoteDao.setFavoritePicture(idPicture, idQuote);

    } catch (Exception exc) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exc;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Modify the start date of work, of a quote.
   * 
   * @param idQuote the quote which is going to be modified
   * @param date the new date to be inserted
   */
  @Override
  public void setDateStartWork(int idQuote, LocalDate date) {
    try {
      this.dalServiceTransaction.startTransaction();
      if (this.quoteDao.findQuotebyId(idQuote) == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      }
      this.quoteDao.setDateQuote(idQuote, date);

    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Set the date of a quote to null.
   * 
   * @param idQuote the quote which is going to be modified
   */
  @Override
  public void setDateNull(int idQuote) {
    try {
      this.dalServiceTransaction.startTransaction();
      if (this.quoteDao.findQuotebyId(idQuote) == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      }
      this.quoteDao.setDateNull(idQuote);

    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }
}
