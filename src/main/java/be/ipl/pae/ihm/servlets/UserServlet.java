package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.ucc.user.IUserUcc;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import com.owlike.genson.Genson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

  private IFactory factory;
  private IUserUcc userUcc;

  public UserServlet(IUserUcc userUcc, IFactory factory) {
    this.factory = factory;
    this.userUcc = userUcc;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String request = req.getParameter("request");
    switch (request) {
      case "searchUnconfirmedUser":
        searchUnconfirmedUser(req, resp);
        break;
      case "searchUserFiltredBy":
        searchUserFiltredBy(req, resp);
        break;
      case "searchUserNotLinked":
        searchUserNotLinked(req, resp);
        break;
      default:
        break;
    }
  }

  protected void searchUnconfirmedUser(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      // TODO /users => all users & /users/:id => user with id
      List<IUserDto> unconfirmedUsers = userUcc.retrieveAllUnconfirmedUsers();
      String json = "{\"users\":" + new Genson().serialize(unconfirmedUsers) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  protected void searchUserFiltredBy(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String name = req.getParameter("name");
      String city = req.getParameter("city");

      List<IUserDto> listUser = new ArrayList<>();

      listUser = userUcc.retrieveUserFiltredBy(name, city);

      String json = "{\"users\":" + new Genson().serialize(listUser) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  protected void searchUserNotLinked(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {

      List<IUserDto> listUser = new ArrayList<>();
      listUser = userUcc.retrieveUserNotLinked();

      String json = "{\"users\":" + new Genson().serialize(listUser) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (Exception exception) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String idUser = req.getParameter("user");
      String worker = req.getParameter("worker");
      IUserDto userToBeChanged = userUcc.findUserById(idUser);
      String email = userToBeChanged.getEmail();
      boolean resultat = userUcc.setUserAsConfirmed(email);


      if (worker.equals("true")) {
        resultat = userUcc.setUserAsAWorker(email);
      }

      if (resultat) {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_OK);
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
      }

    } catch (FatalException exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    }
  }

  /*
   * private String getRequestUserId(HttpServletRequest req) { String[] urlParts =
   * req.getRequestURI().split("/"); // quotes/:id if (urlParts.length > 2) { return urlParts[2]; //
   * :id } return null; }
   */
  /**
   * Register a new user.
   * 
   * @param req the http request. this request must contain a "user" parameter which contains an
   *        json user
   * @param resp the http response. if the user information who wants to register are invalid the
   *        error code will be put to 400. if the username or the email are already taken the error
   *        code will be put to 400. If the request was successful, then the serialized user will be
   *        send to the outputStream.
   */

  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      System.out.println(req.getParameter("user"));
      IUserDto userToRegister =
          new Genson().deserializeInto(req.getParameter("user"), factory.getEmptyUser());
      if (isValid(userToRegister)) {
        IUserDto user = userUcc.register(userToRegister);
        String json = "{\"user\":" + new Genson().serialize(user) + "}";

        UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
      }
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private boolean isValid(IUserDto user) {
    if (user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getLastName().isEmpty()
        || user.getFirstName().isEmpty() || user.getCity().isEmpty()) {
      return false;
    }

    if (!user.getUsername().matches(UtilsServlets.REGEX_USERNAME)
        || !user.getEmail().matches(UtilsServlets.REGEX_EMAIL)) {
      return false;
    }

    return true;
  }

}
