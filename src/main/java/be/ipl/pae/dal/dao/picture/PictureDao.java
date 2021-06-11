package be.ipl.pae.dal.dao.picture;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.dal.services.IDalServices;
import be.ipl.pae.exceptions.FatalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PictureDao implements IPictureDao {

  private IDalServices dalservices;
  private IFactory factory;

  public PictureDao(IDalServices dalservices, IFactory factory) {
    this.dalservices = dalservices;
    this.factory = factory;
  }

  /**
   * Insert multiple pictures.
   * 
   * @param list list of picture
   * @param idQuote the id of the quote
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public void insertAfterPicture(List<IPictureDto> list, int idQuote) {
    for (IPictureDto l : list) {
      String query = "INSERT INTO project.pictures VALUES (DEFAULT,?,?,?,?)";
      try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
        ps.setString(1, l.getSource());
        ps.setInt(2, idQuote);
        ps.setBoolean(3, l.isVisible());
        ps.setInt(4, l.getProjectType());
        ps.execute();
      } catch (SQLException exception) {
        throw new FatalException();
      }
    }
  }

  /**
   * Update the picture to visible.
   *
   * @param idPictureList list of id picture
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public void updatePicToVisible(List<String> idPictureList) {
    for (String idPicture : idPictureList) {
      String query = "UPDATE project.pictures SET showed = true WHERE id_picture = ?";
      try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
        ps.setInt(1, Integer.parseInt(idPicture));
        ps.execute();
      } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        throw new FatalException();
      }
    }
  }

  /**
   * Find a picture by his id.
   * 
   * @param pictureId the id of the picture
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public IPictureDto getPictureById(int pictureId) {
    IPictureDto picture = null;
    String query = "SELECT * FROM project.pictures WHERE id_picture = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, pictureId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          picture = fillPictureDto(rs);
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return picture;
  }

  /**
   * Fills a picture with a provided result set.
   * 
   * @param rs an sql result set containing a quote
   * @return picture the filled picture
   * @throws FatalException if a problem has occurred.
   */
  private IPictureDto fillPictureDto(ResultSet rs) {
    IPictureDto picture = factory.getEmptyPicture();
    try {
      picture.setIdPicture(rs.getInt(1));
      picture.setSource(rs.getString(2));
      picture.setQuote(rs.getInt(3));
      picture.setVisible(rs.getBoolean(4));
      picture.setProjectType(rs.getInt(5));
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return picture;
  }

  /**
   * Find all the picture which has no project type.
   * 
   * @param quoteId the quote from which one we want the pictures
   * @return a list of pictures
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public List<IPictureDto> getPicturesBeforeByQuoteId(int quoteId) {
    List<IPictureDto> pictures = new ArrayList<IPictureDto>();
    String query = "SELECT * FROM project.pictures WHERE project_type IS NULL AND quote = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, quoteId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          pictures.add(fillPictureDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return pictures;
  }

  /**
   * Find all the picture which has a project type.
   * 
   * @param quoteId the quote from which one we want the pictures
   * @return list of pictures
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public List<IPictureDto> getPicturesAfterByQuoteId(int quoteId) {
    List<IPictureDto> pictures = new ArrayList<IPictureDto>();
    String query = "SELECT * FROM project.pictures WHERE project_type IS NOT NULL AND quote = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, quoteId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          pictures.add(fillPictureDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return pictures;
  }

  /**
   * Find every visible picture.
   *
   * @return list of visible pictures
   * @throws FatalException if a problem has occurred.
   */

  @Override public List<IPictureDto> getAllVisiblePictures() {
    List<IPictureDto> pictures = new ArrayList<IPictureDto>();
    String query = "SELECT * FROM project.pictures WHERE showed = true";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          pictures.add(fillPictureDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new FatalException();
    }

    return pictures;
  }

  /**
   * Find every visible picture a specific type
   *
   * @param typeId id of the specified type of pictures
   * @return list of visible pictures of a specific type
   * @throws FatalException if a problem has occurred.
   */
  @Override public List<IPictureDto> getAllVisiblePicturesOfAType(String typeId) {
    List<IPictureDto> pictures = new ArrayList<IPictureDto>();
    int type = Integer.parseInt(typeId);
    String query = "SELECT * FROM project.pictures WHERE showed = true AND project_type = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, type);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          pictures.add(fillPictureDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new FatalException();
    }

    return pictures;
  }



}
