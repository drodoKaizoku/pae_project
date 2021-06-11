package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.ucc.user.IUserUcc;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.owlike.genson.Genson;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

  private IUserUcc userUcc;

  public LoginServlet(IUserUcc userUcc) {
    this.userUcc = userUcc;
  }

  /**
   * Connect the user.
   * 
   * @param req the http request. this request must contain the parameters "username" and
   *        "password".
   * @param resp the http response. if the username and the password are empty, and the username
   *        does not match the proper regex, the error code will be put to 400. If the username does
   *        not exist in the database or, if the password is wrong then the error code will be put
   *        to 401. If the request was successful, then the code will be put to 200 and the
   *        serialized user will be sent to the outputStream.
   * 
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String username = req.getParameter("username");
      String password = req.getParameter("password");
      if (!username.isEmpty() && !password.isEmpty()
          && username.matches(UtilsServlets.REGEX_USERNAME)) {
        IUserDto user = userUcc.connect(username, password);
        String token = JWT.create().withClaim("idUser", ((Integer) user.getIdUser()).toString())
            .withClaim("idClient", user.getClient())
            .sign(Algorithm.HMAC256(UtilsServlets.JWTSECRET));
        System.out.println(token);
        String json =
            "{\"token\":\"" + token + "\", \"user\":" + new Genson().serialize(user) + "}";
        UtilsServlets.sendResponse(resp, HttpServletResponse.SC_OK, json, UtilsServlets.MIME_JSON);
      } else {
        UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
      }
    } catch (BizException bizException) {
      UtilsServlets.sendEmptyResponse(resp, bizException.getCode());
    } catch (FatalException exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

}
