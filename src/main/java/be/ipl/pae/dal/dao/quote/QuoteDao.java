package be.ipl.pae.dal.dao.quote;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.biz.quote.IQuoteDto;
import be.ipl.pae.biz.quote.State;
import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.dal.dao.picture.IPictureDao;
import be.ipl.pae.dal.dao.type.IProjectTypeDao;
import be.ipl.pae.dal.services.IDalServices;
import be.ipl.pae.exceptions.FatalException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuoteDao implements IQuoteDao {
  private IDalServices dalservices;
  private IFactory factory;
  private IPictureDao pictureDao;
  private IProjectTypeDao projectTypeDao;


  /**
   * Constructor of QuoteDao.
   * 
   * @param dalservices dependency injected via constructor
   * @param factory dependency injected via constructor
   * @param pictureDao dependency injected via constructor
   */
  public QuoteDao(IDalServices dalservices, IFactory factory, IPictureDao pictureDao,
      IProjectTypeDao projectTypeDao) {
    this.dalservices = dalservices;
    this.factory = factory;
    this.pictureDao = pictureDao;
    this.projectTypeDao = projectTypeDao;

  }

  /**
   * Change the value of a picture of a quote to favorite.
   *
   * @param idPicture the new favorite picture
   * @param idQuote the quote to be modified
   */
  public void setFavoritePicture(int idPicture, int idQuote) {
    String query = "UPDATE project.quotes SET favorite_picture = ?  WHERE id_quote = ? ";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, idPicture);
      ps.setInt(2, idQuote);
      ps.execute();
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Insert a new quote from the database.
   * 
   * @param quoteToRegister the quote to insert in the database
   */
  @Override
  public IQuoteDto insertQuote(IQuoteDto quoteToRegister, List<String> listType) {
    String queryQuote =
        "INSERT INTO project.quotes VALUES (DEFAULT,?,?,?,NULL,?,?,?) RETURNING id_quote";
    String queryPicturesBefore = "INSERT INTO project.pictures VALUES (DEFAULT,?,?,?,?)";
    String queryType = "INSERT INTO project.project_quote VALUES (?,?)";

    try (PreparedStatement registerQuote = dalservices.getPreparedStatement(queryQuote)) {
      registerQuote.setDate(1, Date.valueOf(quoteToRegister.getDateQuote()));
      registerQuote.setInt(2, quoteToRegister.getClient());
      registerQuote.setString(3, quoteToRegister.getWorkPeriod());
      registerQuote.setString(4, quoteToRegister.getState().getValue());
      registerQuote.setDouble(5, quoteToRegister.getFullAmount());
      registerQuote.setNull(6, java.sql.Types.DATE);

      ResultSet res = registerQuote.executeQuery();
      if (res.next()) {
        quoteToRegister.setIdQuote(res.getInt(1));
      }

    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new FatalException();
    }

    for (String l : listType) {
      int id = Integer.parseInt(l);
      try (PreparedStatement registerProject = dalservices.getPreparedStatement(queryType)) {
        registerProject.setInt(1, quoteToRegister.getIdQuote());
        registerProject.setInt(2, id);
        registerProject.execute();
      } catch (SQLException sqlException) {
        throw new FatalException();
      }

    }
    // PEUT ETRE A MODIF ET METTRE DANS UN PICTURE DAO ET FAIRE APPEL DANS L'UCC QUOTE
    try (PreparedStatement insertPictureBefore =
        dalservices.getPreparedStatement(queryPicturesBefore)) {
      for (IPictureDto pic : quoteToRegister.getPicturesBefore()) {
        insertPictureBefore.setString(1, pic.getSource());
        insertPictureBefore.setInt(2, quoteToRegister.getIdQuote());
        insertPictureBefore.setBoolean(3, false);
        insertPictureBefore.setNull(4, java.sql.Types.INTEGER);
        insertPictureBefore.execute();
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }

    return quoteToRegister; // TODO return inserted quote
  }

  /**
   * Finds all quotes.
   * 
   * @param idClient the id of client
   * @param clientName the name of the client
   * @param minPrice the minimum price of the quotes to search by default equals 0
   * @param maxPrice maximum price of the quotes to search
   * @param date the quotes dates to search
   * @param listProject list of type to search
   * @return quotes a list of all the quotes
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public List<IQuoteDto> findAll(String idClient, String clientName, String minPrice, int maxPrice,
      String date, ArrayList<String> listProject) {
    ArrayList<IQuoteDto> quotes = new ArrayList<>();
    int minimumPrice = Integer.parseInt(minPrice);
    int idClientParsed;
    String stringReturned = " ";
    String header = " ";
    String join = "";
    String sameQuote = "";
    String addIdClient = "";

    if (clientName == null) {
      clientName = "";
    }

    if (Objects.nonNull(idClient)) {
      idClientParsed = Integer.parseInt(idClient);
      addIdClient = "AND q.client = " + idClientParsed;
    }

    if (!listProject.isEmpty()) {
      join = "AND  pr1.id_quote= q.id_quote ";
      for (int i = 1; i <= listProject.size(); i++) {
        if (i + 1 <= listProject.size()) {
          sameQuote += " AND pr1.id_quote = pr" + (i + 1) + ".id_quote";
        }
        stringReturned += " AND pr" + i + ".id_project_type = ? ";
        header += " ,project.project_quote pr" + i + " ";
      }
    }

    String query = "SELECT q.*, cl.id_client, cl.lastname, cl.firstname "
        + "FROM project.quotes q, project.clients cl" + header
        + "WHERE (LOWER(cl.lastname) LIKE ? OR LOWER(cl.firstname) LIKE ?)" + addIdClient
        + " AND cl.id_client = q.client AND (q.full_amount >= ? AND q.full_amount <= ? ) ";
    if (!date.isEmpty()) {
      query += " AND q.date_quote = ?" + stringReturned + " GROUP BY q.id_quote, cl.id_client";
    } else {
      query += join += stringReturned + sameQuote + " GROUP BY q.id_quote, cl.id_client";
    }

    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {

      ps.setString(1, '%' + clientName.toLowerCase() + '%');
      ps.setString(2, '%' + clientName.toLowerCase() + '%');
      ps.setInt(3, minimumPrice);
      ps.setInt(4, maxPrice);
      int nbOfArgQuery = 5;
      if (!date.isEmpty()) {
        ps.setDate(5, Date.valueOf(date));
        nbOfArgQuery = 6;
      }
      if (!listProject.isEmpty()) {

        for (String l : listProject) {
          ps.setInt(nbOfArgQuery, Integer.parseInt(l));
          nbOfArgQuery++;
        }
      }
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          quotes.add(fillQuoteDto(rs, false));
        }
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new FatalException();
    }
    return quotes;
  }


  /**
   * Fills a quote with a provided result set.
   * 
   * @param rs an sql result set containing a quote
   * @param getPicture a boolean which say if we want the pictures
   * @return quote the filled quote
   * @throws FatalException if a problem has occurred.
   */
  private IQuoteDto fillQuoteDto(ResultSet rs, boolean getPicture) {
    IQuoteDto quote = factory.getEmptyQuote();
    IClientDto client = factory.getEmptyClient();
    try {

      ArrayList<String> listTypeString = new ArrayList<>();

      quote.setIdQuote(rs.getInt(1));
      quote.setDateQuote(rs.getDate(2).toLocalDate());
      quote.setClient(rs.getInt(3));
      quote.setWorkPeriod(rs.getString(4));
      quote.setFavoritePicture(pictureDao.getPictureById(rs.getInt(5)));
      quote.setState(State.fromString(rs.getString(6)));
      quote.setFullAmount(rs.getDouble(7));
      if (rs.getDate(8) == null) {
        quote.setDateStartWork(null);
      } else {
        quote.setDateStartWork(rs.getDate(8).toLocalDate());
      }

      if (getPicture) {
        quote.setPicturesBefore(pictureDao.getPicturesBeforeByQuoteId(rs.getInt(1)));
        quote.setPicturesAfter(pictureDao.getPicturesAfterByQuoteId(rs.getInt(1)));
      }
      client.setIdClient(rs.getInt(9));
      client.setLastName(rs.getString(10));
      client.setFirstName(rs.getString(11));
      List<IProjectTypeDto> listTypeDto = new ArrayList<>();
      listTypeDto = this.projectTypeDao.findTypesOfAQuote(String.valueOf(rs.getInt(1)));
      for (IProjectTypeDto type : listTypeDto) {
        listTypeString.add(type.getProjectTypeName());
      }

      quote.setListType(listTypeString);
      quote.setObjectClient(client);

    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      throw new FatalException();
    }
    return quote;
  }

  /**
   * Finds all quotes of a client.
   * 
   * @param clientId the id of the client we want to get the quotes from
   * @return clientQuotes an list containing the quotes of the client
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public List<IQuoteDto> findClientQuotes(String clientId) {
    if (clientId == null) {
      throw new FatalException();
    }
    int id = Integer.parseInt(clientId);
    ArrayList<IQuoteDto> clientQuotes = new ArrayList<>();
    String query = "SELECT q.*, cl.id_client, cl.lastname, cl.firstname FROM project.quotes q, "
        + "project.clients cl WHERE client = ? AND q.client = cl.id_client";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          clientQuotes.add(fillQuoteDto(rs, false));
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return clientQuotes;
  }


  /**
   * Finds the quote with the corresponding id.
   * 
   * @param quoteId the id of the quote to be found
   * @return quote the quote with the corresponding id if it exists
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public IQuoteDto findQuotebyId(int quoteId) {
    IQuoteDto quote = null;
    String query = "SELECT  q.*, cl.* FROM project.quotes q, project.clients cl "
        + " WHERE id_quote = ? AND q.client = cl.id_client";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setInt(1, quoteId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          quote = fillQuoteDto(rs, true);
        }
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return quote;
  }

  /**
   * Confirms the quote's dateStartWork.
   * 
   * @param quoteId the id of the quote
   * @param dateStartWork the date to be inserted
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public void confirmStartDate(int quoteId, LocalDate dateStartWork, String state) {
    String query = "UPDATE project.quotes SET date_start_work = ?, status = ? WHERE id_quote = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setDate(1, Date.valueOf(dateStartWork));
      ps.setString(2, state);
      ps.setInt(3, quoteId);
      ps.execute();
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Set the state of the quote.
   * 
   * @param quoteId the quote to modify
   * @param state the new state
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public void setStatutQuote(int quoteId, String state) {
    String query = "UPDATE project.quotes SET status = ? WHERE id_quote = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setString(1, state);
      ps.setInt(2, quoteId);
      ps.execute();
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Set the start date of a quote.
   * 
   * @param quoteId the quote to modify
   * @param date the new date
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public void setDateQuote(int quoteId, LocalDate date) {
    String query = "UPDATE project.quotes SET date_start_work = ? WHERE id_quote = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setDate(1, Date.valueOf(date));
      ps.setInt(2, quoteId);
      ps.execute();
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  /**
   * Delete the start date of work of a quote.
   * 
   * @param idQuote the quote to modify
   * @throws FatalException if a problem has occurred.
   */
  @Override
  public void setDateNull(int idQuote) {
    String query = "UPDATE project.quotes SET date_start_work = ? WHERE id_quote = ?";
    try (PreparedStatement ps = dalservices.getPreparedStatement(query)) {
      ps.setNull(1, java.sql.Types.DATE);
      ps.setInt(2, idQuote);
      ps.execute();
    } catch (Exception exception) {
      throw new FatalException();
    }
  }
}
