package be.ipl.pae.biz.quote;

import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.biz.pictures.IPictureDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface IQuoteDto {

  int getIdQuote();

  void setIdQuote(int idQuote);

  LocalDate getDateQuote();

  void setDateQuote(LocalDate dateQuote);

  int getClient();

  void setClient(int client);

  String getWorkPeriod();

  void setWorkPeriod(String workPeriod);

  double getFullAmount();

  void setFullAmount(double fullAmount);

  LocalDate getDateStartWork();

  void setDateStartWork(LocalDate dateStartWork);

  IPictureDto getFavoritePicture();

  void setFavoritePicture(IPictureDto favoritePicture);

  List<IPictureDto> getPicturesBefore();

  void setPicturesBefore(List<IPictureDto> picturesBefore);

  List<IPictureDto> getPicturesAfter();

  void setPicturesAfter(List<IPictureDto> picturesAfter);

  State getState();

  void setState(State introduced);

  IClientDto getObjectClient();

  void setObjectClient(IClientDto objectClient);

  ArrayList<String> getListType();

  void setListType(ArrayList<String> listType);
}
