package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.IFactory;
import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.biz.ucc.client.IClientUcc;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import com.owlike.genson.Genson;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ClientServlet extends HttpServlet {

  private IFactory factory;
  private IClientUcc clientUcc;

  public ClientServlet(IFactory factory, IClientUcc clientUcc) {
    this.factory = factory;
    this.clientUcc = clientUcc;
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      List<IClientDto> listClient = clientUcc.retrieveAllClientsWithNoUserAccounts();
      String json = "{\"clients\":" + new Genson().serialize(listClient) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (Exception exception) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Insert a new client.
   * 
   * @param req the http request. this request must contain the parameter "client" which is a json
   *        client.
   * @param resp the http response. if the client information that we want to register are invalid
   *        the error code will be put to 400. If the client is already in the database then the
   *        error code will be put to 409. If the request was succesful then the code will be put to
   *        200.
   * 
   */
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      IClientDto clientToInsert =
          new Genson().deserializeInto(req.getParameter("client"), factory.getEmptyClient());

      if (isValid(clientToInsert)) {
        clientUcc.register(clientToInsert);

        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_OK);
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
      }
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    } catch (FatalException exception) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private boolean isValid(IClientDto client) {
    if (client.getLastName().isEmpty() || client.getFirstName().isEmpty()
        || client.getStreet().isEmpty() || client.getStreetNumber().isEmpty()
        || client.getPostCode().isEmpty() || client.getCity().isEmpty()
        || client.getPostCode().isEmpty() || client.getEmail().isEmpty()
        || client.getPhoneNumber().isEmpty()) {
      return false;
    }

    if (!client.getEmail().matches(UtilsServlets.REGEX_EMAIL)
        || client.getPhoneNumber().matches(UtilsServlets.PHONE_REGEX)) {
      return false;
    }
    return true;
  }

}
