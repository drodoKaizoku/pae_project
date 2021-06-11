package be.ipl.pae.ihm.servlets;

import org.eclipse.jetty.servlet.DefaultServlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RootServlet extends DefaultServlet {

  private static final String PATH_VIEWS = "public/views/";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    try {
      if (!req.getServletPath().equals("/")) {
        super.doGet(req, resp);
      } else {
        respIndex(resp);
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      UtilsServlets.sendEmptyResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void respIndex(HttpServletResponse resp) throws IOException {

    ArrayList<Path> arrayPath = new ArrayList<>();

    arrayPath.add(Paths.get(PATH_VIEWS + "header.html")); // START

    arrayPath.add(Paths.get(PATH_VIEWS + "home.html"));
    arrayPath.add(Paths.get(PATH_VIEWS + "login.html"));
    arrayPath.add(Paths.get(PATH_VIEWS + "register.html"));
    arrayPath.add(Paths.get(PATH_VIEWS + "insert-client.html"));
    arrayPath.add(Paths.get(PATH_VIEWS + "dashboard-client.html"));
    arrayPath.add(Paths.get(PATH_VIEWS + "dashboard-worker.html"));
    arrayPath.add(Paths.get(PATH_VIEWS + "gestion-worker.html"));

    arrayPath.add(Paths.get(PATH_VIEWS + "footer.html")); // END

    ServletOutputStream outputStream = resp.getOutputStream();
    byte[] byteFile = null;
    int length = 0;

    for (Path path : arrayPath) {
      byteFile = Files.readAllBytes(path);
      outputStream.write(byteFile);
      length += byteFile.length;
    }
    resp.setContentLength(length);
  }

}
