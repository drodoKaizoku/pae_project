package be.ipl.pae.dal;

import be.ipl.pae.biz.quote.IQuoteDto;
import be.ipl.pae.biz.quote.Quote;
import be.ipl.pae.biz.quote.State;
import be.ipl.pae.dal.dao.quote.IQuoteDao;
import be.ipl.pae.exceptions.FatalException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockQuoteDao implements IQuoteDao {

  @Override
  public IQuoteDto insertQuote(IQuoteDto quoteToInsert, List<String> list) {
    if (quoteToInsert == null) {
      throw new FatalException();
    }
    // TODO Auto-generated method stub
    return quoteToInsert;
  }

  @Override
  public List<IQuoteDto> findClientQuotes(String clientId) {
    // TODO Auto-generated method stub
    if (clientId == null) {
      throw new FatalException();
    }
    List<IQuoteDto> list = new ArrayList<IQuoteDto>();
    if (!clientId.equals("0")) {
      IQuoteDto quote = new Quote(1, LocalDate.now(), Integer.parseInt(clientId), null, null, null,
          null, 1578, LocalDate.now(), null);
      list.add(quote);
    }
    return list;
  }

  @Override
  public IQuoteDto findQuotebyId(int quoteId) {
    if (quoteId == 0) {
      return null;
    }
    if (quoteId == 1) {
      Quote quoteBizExceptionSetQuoteState =
          new Quote(quoteId, null, 0, null, null, null, null, 0, null, null);
      quoteBizExceptionSetQuoteState.setState(State.ORDER_CONFIRMED);
      return quoteBizExceptionSetQuoteState;
    }
    if (quoteId == 12) {
      return new Quote();
    }
    Quote quoteFatalExceptionSetQuoteState =
        new Quote(quoteId, null, 0, null, null, null, null, 0, LocalDate.now(), null);
    quoteFatalExceptionSetQuoteState.setState(State.DATE_CONFIRMED);
    return quoteFatalExceptionSetQuoteState;
  }



  @Override
  public void setStatutQuote(int quoteId, String state) {
    if (quoteId == 2) {
      throw new FatalException();
    }

  }

  @Override
  public void confirmStartDate(int quoteId, LocalDate dateStartWork, String state) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setFavoritePicture(int idPicture, int idQuote) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setDateQuote(int quoteId, LocalDate date) {
    // TODO Auto-generated method stub

  }


  @Override
  public List<IQuoteDto> findAll(String idClient, String clientName, String minPrice, int maxPrice,
      String date, ArrayList<String> listProject) {
    if (minPrice == null || date == null || listProject == null) {
      throw new FatalException();
    }
    List<IQuoteDto> list = new ArrayList<IQuoteDto>();
    list.add(new Quote(1, LocalDate.now(), 1, null, null, null, null, 1578, LocalDate.now(), null));
    return list;
  }

  @Override
  public void setDateNull(int idQuote) {
    // TODO Auto-generated method stub

  }



}
