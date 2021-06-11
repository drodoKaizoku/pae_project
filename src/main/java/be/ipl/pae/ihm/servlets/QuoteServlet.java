package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.biz.quote.IQuoteDto;
import be.ipl.pae.biz.quote.State;
import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.biz.ucc.quote.IQuoteUcc;
import be.ipl.pae.biz.ucc.type.IProjectTypeUcc;
import be.ipl.pae.biz.ucc.user.IUserUcc;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class QuoteServlet extends HttpServlet {

  private IQuoteUcc quoteUcc;
  private IFactory factory;
  private IUserUcc userUcc;
  private IProjectTypeUcc projectTypeUcc;

  private Genson gensonPicture = new GensonBuilder().withConverter(new Converter<IPictureDto>() {

    @Override
    public void serialize(IPictureDto picture, ObjectWriter writer, Context ctx) throws Exception {
      writer.beginObject();
      writer.writeNumber("idPicture", picture.getIdPicture()).writeString("source",
          picture.getSource());
      writer.writeBoolean("visible", picture.isVisible());
      if (picture.getProjectType() >= 0) {
        writer.writeNumber("projectType", picture.getProjectType());
      } else {
        writer.writeString("projectType", null);
      }
      writer.endObject();
    }

    @Override
    public IPictureDto deserialize(ObjectReader reader, Context ctx) throws Exception {
      // TODO Auto-generated method stub
      return null;
    }

  }, IPictureDto.class).create();

  private Genson gensonQuote = new GensonBuilder().withConverter(new Converter<IQuoteDto>() {

    @Override
    public void serialize(IQuoteDto quote, ObjectWriter writer, Context ctx) throws Exception {
      writer.beginObject();
      writer.writeNumber("idQuote", quote.getIdQuote())
          .writeString("dateQuote",
              quote.getDateQuote().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
          .writeNumber("client", quote.getClient())
          .writeString("workPeriod", quote.getWorkPeriod());
      if (quote.getFavoritePicture() != null) {
        writer.writeString("favoritePicture", gensonPicture.serialize(quote.getFavoritePicture()));
      } else {
        writer.writeString("favoritePicture", null);
      }
      writer
          .writeString("picturesBefore",
              gensonPicture.serialize(quote.getPicturesBefore(),
                  new GenericType<List<IPictureDto>>() {}))
          .writeString("picturesAfter",
              gensonPicture.serialize(quote.getPicturesAfter(),
                  new GenericType<List<IPictureDto>>() {}))
          .writeString("state", quote.getState().getValue())
          .writeNumber("fullAmount", quote.getFullAmount());
      if (quote.getDateStartWork() != null) {
        writer.writeString("dateStartWork",
            quote.getDateStartWork().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
      } else {
        writer.writeString("dateStartWork", null);
      }
      if (quote.getObjectClient() != null) {
        writer.writeString("objectClient", new Genson().serialize(quote.getObjectClient()));
      } else {
        writer.writeString("objectClient", null);
      }
      if (quote.getListType() != null) {
        writer.writeString("listType",
            new Genson().serialize(quote.getListType(), new GenericType<ArrayList<String>>() {}));
      }

      writer.endObject();
    }

    @Override
    public IQuoteDto deserialize(ObjectReader reader, Context ctx) throws Exception {
      return null;
    }

  }, IQuoteDto.class).create();

  /**
   * Constructor of QuoteServlet.
   * 
   * @param quoteUcc dependency injected via constructor
   * @param projectTypeUcc dependency injected via constructor
   * @param factory dependency injected via constructor
   */
  public QuoteServlet(IQuoteUcc quoteUcc, IFactory factory, IUserUcc userUcc,
      IProjectTypeUcc projectTypeUcc) {
    this.quoteUcc = quoteUcc;
    this.factory = factory;
    this.userUcc = userUcc;
    this.projectTypeUcc = projectTypeUcc;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String request = req.getParameter("request");
    switch (request) {
      case "searchQuoteFiltredBy":
        searchQuoteFiltredBy(req, resp);
        break;
      case "searchQuotebyId":
        searchQuotebyId(req, resp);
        break;
      case "getTypesQuote":
        getQuoteTypes(req, resp);
        break;
      case "getQuotesByClient":
        getQuotesByClient(req, resp);
        break;
      default:
        break;
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String request = req.getParameter("request");
    switch (request) {
      case "addQuoteStartWork":
        addQuoteStartWork(req, resp);
        break;
      case "setStateQuote":
        setStateQuote(req, resp);
        break;
      case "deleteStartWork":
        deleteStartWork(req, resp);
        break;
      default:
        break;
    }
  }

  /**
   * Get quotes of a client
   * 
   * @param req the http request.
   * @param resp the http response.
   */
  public void getQuotesByClient(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));
      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto user = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(user, false)) {
          String idClient;
          if (req.getParameter("idClient") != null) {
            idClient = req.getParameter("idClient");
          } else {
            idClient = payload.getClaim("idClient").asString();
          }
          List<IQuoteDto> listQuote = new ArrayList<>();

          listQuote = quoteUcc.getClientQuotes(idClient);

          String json = "{\"quotes\":"
              + gensonQuote.serialize(listQuote, new GenericType<List<IQuoteDto>>() {}) + "}";

          UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json,
              UtilsServlets.MIME_JSON);
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Search a quote filtred by the client's id, client's name, price, date
   * 
   * @param req the http request.
   * @param resp the http response.
   */
  public void searchQuoteFiltredBy(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {

      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto user = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(user, true)) {
          String arrayList = req.getParameter("listType");
          ArrayList<String> listProject = new ArrayList<>();
          if (Objects.nonNull(arrayList)) {
            listProject =
                new Genson().deserialize(arrayList, new GenericType<ArrayList<String>>() {});
          }
          int maximumPrice = 0;
          String maxPrice = req.getParameter("maxPrice");
          if (Objects.isNull(maxPrice) || maxPrice.equals("")) {
            maximumPrice = Integer.MAX_VALUE;
          }
          String minPrice = req.getParameter("minPrice");
          if (Objects.isNull(minPrice) || minPrice.equals("")) {
            minPrice = "0";
          }
          if (Objects.nonNull(maxPrice) && !maxPrice.equals("")) {
            maximumPrice = Integer.parseInt(maxPrice);
          }

          String quoteDate = req.getParameter("quoteDate");
          if (Objects.isNull(quoteDate) || quoteDate.equals("undefined")) {
            quoteDate = "";
          }
          List<IQuoteDto> listQuote = new ArrayList<>();

          String idClient = payload.getClaim("idClient").asString();
          String clientName = req.getParameter("clientName");


          listQuote = quoteUcc.getAllQuotes(idClient, clientName, minPrice, maximumPrice, quoteDate,
              listProject);

          String json = "{\"quotes\":"
              + gensonQuote.serialize(listQuote, new GenericType<List<IQuoteDto>>() {}) + "}";

          UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json,
              UtilsServlets.MIME_JSON);
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }

      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Search a quote by its id
   * 
   * @param req the http request.
   * @param resp the http response.
   */
  public void searchQuotebyId(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));
      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto user = this.userUcc.findUserById(idUser);

        if (UtilsServlets.checkUserValidity(user, false)) {
          String idQuote = req.getParameter("idQuote");
          int idQuoteParsed = Integer.parseInt(idQuote);
          List<IQuoteDto> quote = this.quoteUcc.getQuote(idQuoteParsed);

          String json = "{\"quotes\":"
              + gensonQuote.serialize(quote, new GenericType<List<IQuoteDto>>() {}) + "}";
          UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json,
              UtilsServlets.MIME_JSON);
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }


    } catch (FatalException exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve all types of a quote.
   * 
   * @param req the http request
   * @param resp the http response
   */
  public void getQuoteTypes(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String idQuote = req.getParameter("idQuote");

      List<IProjectTypeDto> listType = new ArrayList<>();
      listType = this.projectTypeUcc.retrieveAllTypesOfAQuote(idQuote);

      String json = "{\"types\":" + new Genson().serialize(listType) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (FatalException exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }



  @SuppressWarnings("unchecked")
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String user = req.getParameter("user");
          Map<String, Object> mapQuote =
              new Genson().deserialize(req.getParameter("quote"), Map.class);

          List<String> listType = (List<String>) mapQuote.get("listType");
          String workPeriod = mapQuote.get("workPeriod").toString();
          String dateQuote = mapQuote.get("dateQuote").toString();
          LocalDate dateQuoteToInsert =
              LocalDate.parse(dateQuote.subSequence(5, dateQuote.length()));

          if (!user.equals("")) {
            this.userUcc.linkUserToClient(user, mapQuote.get("client").toString());
          }

          IQuoteDto quoteToInsert = factory.getEmptyQuote();

          double fullAmount = Double.parseDouble(mapQuote.get("fullAmount").toString());
          int client = Integer.parseInt(mapQuote.get("client").toString());

          quoteToInsert.setWorkPeriod(workPeriod);
          quoteToInsert.setDateQuote(dateQuoteToInsert);
          quoteToInsert.setFullAmount(fullAmount);
          quoteToInsert.setClient(client);
          quoteToInsert.setState(State.INTRODUCED);

          List<String> listPicturesBefore = (List<String>) mapQuote.get("picturesBefore");
          ArrayList<IPictureDto> picturesBefore = new ArrayList<>();
          for (String pic : listPicturesBefore) {
            IPictureDto picture = factory.getEmptyPicture();
            picture.setSource(pic);
            picturesBefore.add(picture);
          }
          quoteToInsert.setPicturesBefore(picturesBefore);

          if (isValid(quoteToInsert)) {

            IQuoteDto quote = quoteUcc.introduceQuote(quoteToInsert, listType);
            String json = "{\"quote\":" + new Genson().serialize(quote) + "}";
            UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json,
                UtilsServlets.MIME_JSON);
          } else {
            UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
          }
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }

    } catch (FatalException exception) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    }
  }


  protected void addQuoteStartWork(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {

      String idQuote = req.getParameter("idQuote");
      String date = req.getParameter("date");
      int idQuoteParsed = Integer.parseInt(idQuote);
      LocalDate dateParsed = LocalDate.parse(date);

      if (!date.equals("")) {
        this.quoteUcc.setDateStartWork(idQuoteParsed, dateParsed);
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_OK);
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
      }
    } catch (FatalException exception) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    }

  }

  protected void deleteStartWork(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {

      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String idQuote = req.getParameter("idQuote");
          int idQuoteParsed = Integer.parseInt(idQuote);
          this.quoteUcc.setDateNull(idQuoteParsed);
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_OK);
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());

    } catch (FatalException exception) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  protected void setStateQuote(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String idQuote = req.getParameter("idQuote");
          String state = req.getParameter("state");
          int idQuoteParsed = Integer.parseInt(idQuote);
          switch (state) {
            case "Order confirmed":
              state = State.ORDER_CONFIRMED.getValue();
              break;
            case "Cancelled":
              state = State.CANCELLED.getValue();
              break;
            case "Bill mid-work sent":
              state = State.MID_BILL_SENT.getValue();
              break;
            case "Bill end-work sent":
              state = State.END_BILL_SENT.getValue();
              break;
            case "Visible":
              state = State.VISIBLE.getValue();
              break;
            case "Closed":
              state = State.CLOSED.getValue();
              break;
            case "Date start of work confirmed":
              state = State.DATE_CONFIRMED.getValue();
              break;
            default:

          }

          this.quoteUcc.setQuoteState(idQuoteParsed, state);
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_OK);
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }


    } catch (FatalException exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    }
  }

  private boolean isValid(IQuoteDto quote) {
    if (quote.getWorkPeriod().isEmpty() || quote.getDateQuote() == null
        || quote.getFullAmount() < 0) {
      return false;
    }
    return true;
  }

}
