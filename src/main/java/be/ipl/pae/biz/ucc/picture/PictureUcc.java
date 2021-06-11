package be.ipl.pae.biz.ucc.picture;

import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.biz.quote.State;
import be.ipl.pae.dal.dao.picture.IPictureDao;
import be.ipl.pae.dal.dao.quote.IQuoteDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;
import be.ipl.pae.exceptions.BizException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class PictureUcc implements IPictureUcc {

  private IPictureDao pictureDao;
  private IQuoteDao quoteDao;
  private IDalServicesTransactions dalServiceTransaction;

  /**
   * Constructor of PictureUcc.
   *
   * @param pictureDao object from DAL to communicate with DB
   * @param dalServiceTransaction object to do transactions
   */
  public PictureUcc(IPictureDao pictureDao, IDalServicesTransactions dalServiceTransaction,
      IQuoteDao quoteDao) {

    this.pictureDao = pictureDao;
    this.quoteDao = quoteDao;
    this.dalServiceTransaction = dalServiceTransaction;
  }

  /**
   * Insert a list of pictures into the database.
   * 
   * @param list of pictures to add
   * @param idQuote the quote picture
   * @throws bizException if no idQuote have been found
   */
  @Override
  public void insertAfterPicture(List<IPictureDto> list, int idQuote) {
    try {
      this.dalServiceTransaction.startTransaction();
      if (this.quoteDao.findQuotebyId(idQuote) == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      }
      this.pictureDao.insertAfterPicture(list, idQuote);
    } catch (Exception exc) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exc;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }

  }

  /**
   * Set a list of picture to visible and set the state of the quote to visible in the database.
   * 
   * @param list of id picture which are the pictures to set
   * @param idQuote the quote to set the state
   * @throws bizException if no quote have been found
   */
  @Override
  public void setPictureToVisible(List<String> list, int idQuote) {
    try {
      this.dalServiceTransaction.startTransaction();
      if (this.quoteDao.findQuotebyId(idQuote) == null) {
        throw new BizException(HttpServletResponse.SC_NOT_FOUND);
      }
      this.pictureDao.updatePicToVisible(list);
      this.quoteDao.setStatutQuote(idQuote, State.VISIBLE.getValue());
    } catch (Exception exc) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exc;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  @Override
  public List<IPictureDto> retrieveEveryVisiblePicture() {
    try {
      dalServiceTransaction.startTransaction();
      List<IPictureDto> pictures = pictureDao.getAllVisiblePictures();
      return pictures;
    } catch (Exception exception) {
      dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  @Override
  public List<IPictureDto> retrieveEveryVisiblePictureOfAType(String typeId) {
    try {
      dalServiceTransaction.startTransaction();
      List<IPictureDto> pictures = pictureDao.getAllVisiblePicturesOfAType(typeId);
      return pictures;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }

  }


}
