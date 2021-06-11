package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.biz.ucc.type.IProjectTypeUcc;
import be.ipl.pae.biz.ucc.user.IUserUcc;
import be.ipl.pae.biz.user.IUserDto;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.owlike.genson.Genson;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class TypeServlet extends HttpServlet {

  private IProjectTypeUcc projectType;
  private IUserUcc userUcc;

  /**
   * Constructor of typeServlet.
   * 
   * @param projectType the ucc of projectType depedency injected via constructor
   * @param userUcc the ucc of User depedency injected via constructor
   */
  public TypeServlet(IProjectTypeUcc projectType, IUserUcc userUcc) {

    this.projectType = projectType;
    this.userUcc = userUcc;
  }


  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      List<IProjectTypeDto> listType = projectType.retrieveAllTypes();

      String json = "{\"types\":" + new Genson().serialize(listType) + "}";
      UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }


  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      DecodedJWT payload = UtilsServlets.getDecodedPayload(req.getHeader("Authorization"));

      if (payload != null) {
        String idUser = payload.getClaim("idUser").asString();
        IUserDto userLogged = this.userUcc.findUserById(idUser);
        if (UtilsServlets.checkUserValidity(userLogged, true)) {
          String type = req.getParameter("type");
          if (!type.equals("")) {
            this.projectType.insertNewType(type);
            UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_OK);
          } else {
            UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
          }
        } else {
          UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_FORBIDDEN);
      }


    } catch (Exception exception) {
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
