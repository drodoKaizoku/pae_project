package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.biz.ucc.picture.IPictureUcc;
import be.ipl.pae.biz.ucc.quote.IQuoteUcc;
import be.ipl.pae.biz.ucc.user.IUserUcc;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PictureServlet extends HttpServlet {
  private IFactory factory;
  private IPictureUcc pictureUcc;
  private IQuoteUcc quoteUcc;
  private IUserUcc userUcc;

  /**
   * Constructor of PictureServlet.
   *
   * @param factory dependency injected via constructor
   * @param pictureUcc dependency injected via constructor
   * @param quoteUcc dependency injected via constructor
   */
  public PictureServlet(IFactory factory, IPictureUcc pictureUcc, IQuoteUcc quoteUcc,
      IUserUcc userUcc) {

    this.factory = factory;
    this.pictureUcc = pictureUcc;
    this.quoteUcc = quoteUcc;
    this.userUcc = userUcc;
  }

  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String request = req.getParameter("request");

    switch (request) {
      case "addFavoritePicture":
        addFavoritePicture(req, resp);
        break;
      case "SetPictureVisible":
        setPictureVisible(req, resp);
        break;
      default:
        break;
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {

      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();

        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String idQuote = req.getParameter("idQuote");
          String picturesAfter = req.getParameter("picturesAfter");
          int idQuoteParsed = Integer.parseInt(idQuote);

          List<Map<String, Object>> listmap = new Genson().deserialize(picturesAfter,
              new GenericType<List<Map<String, Object>>>() {});
          List<IPictureDto> listPicture = new ArrayList<>();
          for (Map<String, Object> m : listmap) {
            IPictureDto pictureToAdd = this.factory.getEmptyPicture();
            pictureToAdd.setProjectType((Integer) Integer.parseInt((String) m.get("typeProject")));
            pictureToAdd.setSource((String) m.get("picture"));
            pictureToAdd.setVisible((Boolean) m.get("isChecked"));
            listPicture.add(pictureToAdd);
          }
          this.pictureUcc.insertAfterPicture(listPicture, idQuoteParsed);
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


  protected void addFavoritePicture(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String idPicture = req.getParameter("idPicture");
          String idQuote = req.getParameter("idQuote");

          int idPictureParsed = Integer.parseInt(idPicture);
          int idQuoteParsed = Integer.parseInt(idQuote);

          this.quoteUcc.addFavoritePicture(idPictureParsed, idQuoteParsed);
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

  protected void setPictureVisible(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();

        IUserDto userLogged = this.userUcc.findUserById(idUser);

        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String listPicture = req.getParameter("listOfPicture");
          String idQuote = req.getParameter("idQuote");
          List<String> listPic =
              new Genson().deserialize(listPicture, new GenericType<List<String>>() {});
          this.pictureUcc.setPictureToVisible(listPic, Integer.parseInt(idQuote));
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

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String request = req.getParameter("request");

    switch (request) {
      case "picturesOfAType":
        getRandomPicturesOfAType(req, resp);
        break;
      case "randomPictures":
        getRandomPictures(req, resp);
        break;
      default:
        break;
    }


  }

  protected void getRandomPicturesOfAType(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String type = req.getParameter("type");
    try {
      List<IPictureDto> pictures = pictureUcc.retrieveEveryVisiblePictureOfAType(type);

      String json = "{\"pictures\":" + new Genson().serialize(pictures) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

  }

  protected void getRandomPictures(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      List<IPictureDto> pictures = pictureUcc.retrieveEveryVisiblePicture();
      String json = "{\"pictures\":" + new Genson().serialize(pictures) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

  }



}
