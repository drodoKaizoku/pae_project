package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.biz.ucc.client.IClientUcc;
import be.ipl.pae.biz.ucc.user.IUserUcc;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.owlike.genson.Genson;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SearchClientServlet extends HttpServlet {

  private IClientUcc clientUcc;
  private IUserUcc userUcc;

  public SearchClientServlet(IClientUcc clientUcc, IUserUcc userUcc) {
    this.userUcc = userUcc;
    this.clientUcc = clientUcc;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String request = req.getParameter("request");
    switch (request) {
      case "findClientById":
        searchClientById(req, resp);
        break;
      case "findClientFiltred":
        searchClientFiltred(req, resp);
        break;
      default:
        break;
    }
  }

  /**
   * Search a client filtred by his id.
   * 
   * @param req http request,
   * @param resp http response,
   */
  public void searchClientById(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String idClient = req.getParameter("idClient");
    IClientDto client;
    try {

      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          client = this.clientUcc.findClientById(idClient);
          String json = "{\"clients\":" + new Genson().serialize(client) + "}";
          UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json,
              UtilsServlets.MIME_JSON);
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    } catch (FatalException fatalException) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  protected void searchClientFiltred(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String name = req.getParameter("name");
          String city = req.getParameter("city");
          String postCode = req.getParameter("postCode");

          if (name == null) {
            name = "";
          }
          if (city == null) {
            city = "";
          }
          if (postCode == null) {
            postCode = "";
          }
          List<IClientDto> listClient = clientUcc.retrieveClientFiltredBy(name, postCode, city);
          String json = "{\"clients\":" + new Genson().serialize(listClient) + "}";
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
}
