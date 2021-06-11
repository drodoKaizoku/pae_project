package be.ipl.pae.biz.ucc.quote;

import be.ipl.pae.biz.quote.IQuoteDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface IQuoteUcc {

  IQuoteDto introduceQuote(IQuoteDto quote, List<String> listType);

  List<IQuoteDto> getClientQuotes(String idClient);

  List<IQuoteDto> getQuote(int idQuote);

  List<IQuoteDto> getAllQuotes(String idClient, String client, String minPrice, int maxPrice,
      String date, ArrayList<String> listProject);

  void setQuoteState(int idQuote, String state);

  void confirmDateStartWork(int quoteId, LocalDate dateDebut, String state);

  void addFavoritePicture(int idPicture, int idQuote);

  void setDateStartWork(int idQuote, LocalDate date);

  void setDateNull(int idQuote);

}
