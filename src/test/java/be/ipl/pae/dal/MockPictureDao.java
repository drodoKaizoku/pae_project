package be.ipl.pae.dal;

import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.dal.dao.picture.IPictureDao;
import be.ipl.pae.exceptions.FatalException;

import java.util.ArrayList;
import java.util.List;

public class MockPictureDao implements IPictureDao {

  @Override
  public void insertAfterPicture(List<IPictureDto> list, int idQuote) {
    if (list.get(0).getSource() == null) {
      throw new FatalException();
    }
  }

  @Override
  public void updatePicToVisible(List<String> idPictureList) {
    if (idPictureList.get(0) == null) {
      throw new FatalException();
    }
    // TODO Auto-generated method stub

  }

  @Override
  public IPictureDto getPictureById(int pictureId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IPictureDto> getPicturesBeforeByQuoteId(int quoteId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IPictureDto> getPicturesAfterByQuoteId(int quoteId) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public List<IPictureDto> getAllVisiblePictures() {
    // TODO Auto-generated method stub
    return new ArrayList<IPictureDto>();
  }

  @Override
  public List<IPictureDto> getAllVisiblePicturesOfAType(String typeId) {
    // TODO Auto-generated method stub
    return new ArrayList<IPictureDto>();
  }


}
