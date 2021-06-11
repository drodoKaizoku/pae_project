package be.ipl.pae.ihm.servlets;

import be.ipl.pae.biz.user.IUserDto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

class UtilsServlets {

  public static final String REGEX_USERNAME = "^[a-zA-Z0-9_-]{3,20}$";
  public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
  public static final String PHONE_REGEX = "^(\\+[0-9]{5}|0[0-9]{3})((\\s[0-9]{2}){3})$";

  public static final String JWTSECRET;

  static {
    Properties props = new Properties();
    try {
      props.load(new FileInputStream("./prod.properties"));
    } catch (IOException ioException) {
      ioException.printStackTrace();
      System.exit(1);
    }
    JWTSECRET = props.getProperty("token_key");
  }

  public static final JWTVerifier VERIFIER = JWT.require(Algorithm.HMAC256(JWTSECRET)).build();

  public static final String ENCODING = "UTF-8";

  public static final String MIME_JS = "application/javascript";
  public static final String MIME_JSON = "application/json";
  public static final String MIME_HTML = "text/html";
  public static final String MIME_CSS = "text/css";
  public static final String MIME_ICO = "image/x-icon";
  public static final String MIME_PNG = "image/png";
  public static final String MIME_JPEG = "image/jpeg";

  public static void sendResponse(HttpServletResponse resp, int status, String content,
      String mimeType) throws IOException {
    try {
      resp.setContentType(mimeType);
      resp.setCharacterEncoding(ENCODING);
      resp.setStatus(status);
      resp.getWriter().write(content);
    } catch (IOException ioException) {
      byte[] msgBytes = ioException.getMessage().getBytes(ENCODING);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.setContentType(MIME_HTML);
      resp.setContentLength(msgBytes.length);
      resp.setCharacterEncoding(ENCODING);
      resp.getOutputStream().write(msgBytes);
    }
  }

  public static void sendEmptyResponse(HttpServletResponse resp, int status) {
    resp.setStatus(status);
  }

  public static DecodedJWT getDecodedPayload(String token) {
    try {
      if (token == null) {
        return null;
      }
      return VERIFIER.verify(token);
    } catch (JWTVerificationException exception) {
      return null;
    }
  }

  public static boolean checkUserValidity(IUserDto user, boolean needToBeWorker) {

    if (!user.isConfirmed()) {
      return false;
    }
    if (needToBeWorker && !user.isWorker()) {
      return false;
    }
    return true;
  }


}
