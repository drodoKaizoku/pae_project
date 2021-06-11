package be.ipl.pae.main;

import be.ipl.pae.ihm.servlets.ClientServlet;
import be.ipl.pae.ihm.servlets.LoginServlet;
import be.ipl.pae.ihm.servlets.PictureServlet;
import be.ipl.pae.ihm.servlets.QuoteServlet;
import be.ipl.pae.ihm.servlets.RootServlet;
import be.ipl.pae.ihm.servlets.SearchClientServlet;
import be.ipl.pae.ihm.servlets.TypeServlet;
import be.ipl.pae.ihm.servlets.UserServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

  /**
   * Starts the web application, creates a server on port 8080 and adds the servlets.
   * 
   * @param args application arguments
   * @throws Exception exceptions thrown by java
   */
  public static void main(String[] args) throws Exception {
    WebAppContext context = new WebAppContext();
    context.setContextPath("/");
    context.setResourceBase("public");
    context.setInitParameter("cacheControl", "no-store,no-cache,must-revalidate");
    context.setMaxFormContentSize(6000000);
    context.setMaxFormKeys(6000000);
    InjectionService dependance = new InjectionService("prod.properties");

    RootServlet rootServlet = (RootServlet) dependance.getImplementingInstance(RootServlet.class);
    LoginServlet loginServlet =
        (LoginServlet) dependance.getImplementingInstance(LoginServlet.class);
    UserServlet userServlet = (UserServlet) dependance.getImplementingInstance(UserServlet.class);
    QuoteServlet quoteServlet =
        (QuoteServlet) dependance.getImplementingInstance(QuoteServlet.class);
    ClientServlet clientServlet =
        (ClientServlet) dependance.getImplementingInstance(ClientServlet.class);
    TypeServlet typeServlet = (TypeServlet) dependance.getImplementingInstance(TypeServlet.class);
    SearchClientServlet searchClient =
        (SearchClientServlet) dependance.getImplementingInstance(SearchClientServlet.class);

    PictureServlet pictureServlet =
        (PictureServlet) dependance.getImplementingInstance(PictureServlet.class);

    context.addServlet(new ServletHolder(loginServlet), "/login");
    context.addServlet(new ServletHolder(userServlet), "/users");
    context.addServlet(new ServletHolder(quoteServlet), "/quotes");
    context.addServlet(new ServletHolder(clientServlet), "/clients");
    context.addServlet(new ServletHolder(typeServlet), "/types");
    context.addServlet(new ServletHolder(searchClient), "/searchClients");
    context.addServlet(new ServletHolder(pictureServlet), "/pictures");
    context.addServlet(new ServletHolder(rootServlet), "/");

    Server server = new Server(8080);
    server.setHandler(context);
    server.start();
  }
}
