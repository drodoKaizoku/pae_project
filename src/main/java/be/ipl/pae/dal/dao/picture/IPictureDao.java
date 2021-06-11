package be.ipl.pae.dal.dao.picture;

import be.ipl.pae.biz.pictures.IPictureDto;

import java.util.List;

public interface IPictureDao {

  IPictureDto getPictureById(int pictureId);

  List<IPictureDto> getPicturesBeforeByQuoteId(int quoteId);

  List<IPictureDto> getPicturesAfterByQuoteId(int quoteId);

  void insertAfterPicture(List<IPictureDto> list, int idQuote);

  List<IPictureDto> getAllVisiblePictures();

  List<IPictureDto> getAllVisiblePicturesOfAType(String typeId);

  void updatePicToVisible(List<String> idPictureList);
}
