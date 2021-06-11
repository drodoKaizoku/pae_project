package be.ipl.pae.dal.dao.type;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.dal.services.IDalServices;
import be.ipl.pae.exceptions.FatalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectTypeDao implements IProjectTypeDao {

  private IDalServices dalServices;
  private IFactory factory;

  /**
   * Constructor of ProjectTypeDao.
   * 
   * @param dalServices dependency injected via constructor
   * @param factory dependency injected via constructor
   */
  public ProjectTypeDao(IDalServices dalServices, IFactory factory) {

    this.dalServices = dalServices;
    this.factory = factory;
  }

  /**
   * Retrieve all the project types
   * 
   * @return list a list of project types
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public List<IProjectTypeDto> findTypes() {
    String query = "SELECT * FROM project.project_types";
    List<IProjectTypeDto> list = new ArrayList<IProjectTypeDto>();

    try (PreparedStatement projectQuery = dalServices.getPreparedStatement(query)) {
      try (ResultSet rs = projectQuery.executeQuery()) {
        while (rs.next()) {
          list.add(fillTypeDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }

    return list;
  }

  /**
   * Fills a projectType with a provided result set.
   * 
   * @param rs an sql result set containing a quote
   * @return type the filled projectType
   * @throws FatalException if a problem has occurred.
   */
  private IProjectTypeDto fillTypeDto(ResultSet rs) {
    IProjectTypeDto type = factory.getEmptyProjectType();
    try {
      type.setIdProjectType(rs.getInt(1));
      type.setProjetTypeName(rs.getString(2));

    } catch (SQLException sqlException) {
      throw new FatalException();
    }

    return type;
  }

  /**
   * Insert a new project type into the database.
   * 
   * @param type the name of the new type in string
   * @throws FatalException if a problem has occurred.
   */
  public void insertNewType(String type) {
    String query = "INSERT INTO project.project_types VALUES(DEFAULT,?)";

    try (PreparedStatement insertType = dalServices.getPreparedStatement(query)) {
      insertType.setString(1, type);
      insertType.execute();
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Retrieve all the project types of a quote
   * 
   * @param quoteId the quote from which one we want the project types
   * @return types list of project types
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public List<IProjectTypeDto> findTypesOfAQuote(String quoteId) {
    List<IProjectTypeDto> types = new ArrayList<>();
    int id = Integer.parseInt(quoteId);
    String query = "SELECT * FROM project.project_types t ,project.project_quote q "
        + "WHERE t.id_project_type = q.id_project_type AND q.id_quote = ?";
    try (PreparedStatement projectQuery = dalServices.getPreparedStatement(query)) {
      projectQuery.setInt(1, id);
      try (ResultSet rs = projectQuery.executeQuery()) {
        while (rs.next()) {
          types.add(fillTypeDto(rs));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return types;
  }



}
