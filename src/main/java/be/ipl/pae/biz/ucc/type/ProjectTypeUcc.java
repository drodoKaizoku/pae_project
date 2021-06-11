package be.ipl.pae.biz.ucc.type;

import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.dal.dao.type.IProjectTypeDao;
import be.ipl.pae.dal.services.IDalServicesTransactions;

import java.util.List;

public class ProjectTypeUcc implements IProjectTypeUcc {

  private IDalServicesTransactions dalServiceTransaction;
  private IProjectTypeDao projectTypeDao;

  /**
   * Constructor of ProjectTypeUcc.
   * 
   * @param dalServiceTransaction dependency injected via contructor
   * @param projectTypeDao dependency injected via contructor
   */
  public ProjectTypeUcc(IDalServicesTransactions dalServiceTransaction,
      IProjectTypeDao projectTypeDao) {
    this.dalServiceTransaction = dalServiceTransaction;
    this.projectTypeDao = projectTypeDao;
  }

  /**
   * Retrieve all the project types from the database.
   * 
   * @return list list of project types
   */
  @Override
  public List<IProjectTypeDto> retrieveAllTypes() {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IProjectTypeDto> list = projectTypeDao.findTypes();
      return list;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Retrieve all the project types of quote.
   * 
   * @param quoteId the quote from which one we want the project types
   * @return list list of project types
   */
  @Override
  public List<IProjectTypeDto> retrieveAllTypesOfAQuote(String quoteId) {
    try {
      this.dalServiceTransaction.startTransaction();
      List<IProjectTypeDto> list = projectTypeDao.findTypesOfAQuote(quoteId);
      return list;
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }
  }

  /**
   * Insert a new project type into the database.
   * 
   * @param type name of the project type to insert
   */
  @Override
  public void insertNewType(String type) {
    try {
      this.dalServiceTransaction.startTransaction();
      this.projectTypeDao.insertNewType(type);
    } catch (Exception exception) {
      this.dalServiceTransaction.rollBackTransaction();
      throw exception;
    } finally {
      this.dalServiceTransaction.commitTransaction();
    }

  }

}
