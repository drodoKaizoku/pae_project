package be.ipl.pae.biz.ucc.picture;

import be.ipl.pae.biz.pictures.IPictureDto;

import java.util.List;

public interface IPictureUcc {

  void insertAfterPicture(List<IPictureDto> list, int idQuote);

  List<IPictureDto> retrieveEveryVisiblePicture();


  List<IPictureDto> retrieveEveryVisiblePictureOfAType(String typeId);

  void setPictureToVisible(List<String> list, int idQuote);

}
