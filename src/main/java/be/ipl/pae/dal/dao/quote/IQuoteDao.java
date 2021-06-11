package be.ipl.pae.dal.dao.quote;

import be.ipl.pae.biz.quote.IQuoteDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface IQuoteDao {

  IQuoteDto insertQuote(IQuoteDto quoteToInsert, List<String> list);

  List<IQuoteDto> findAll(String idClient, String clientName, String minPrice, int maxPrice,
      String date, ArrayList<String> listProject);

  List<IQuoteDto> findClientQuotes(String clientId);

  IQuoteDto findQuotebyId(int quoteId);

  void confirmStartDate(int quoteId, LocalDate dateStartWork, String state);

  void setStatutQuote(int quoteId, String state);

  void setFavoritePicture(int idPicture, int idQuote);

  void setDateQuote(int quoteId, LocalDate date);

  void setDateNull(int idQuote);

}
