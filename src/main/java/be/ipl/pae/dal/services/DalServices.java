package be.ipl.pae.dal.services;

import be.ipl.pae.exceptions.FatalException;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

class DalServices implements IDalServices, IDalServicesTransactions {

  private static ThreadLocal<Connection> connection = new ThreadLocal<Connection>();
  private static BasicDataSource pool;

  public DalServices() {
    Properties props = new Properties();
    try {
      props.load(new FileInputStream("./prod.properties"));
    } catch (IOException exception) {
      throw new FatalException();
    }

    String driver = props.getProperty("driver");
    try {
      Class.forName(driver);
    } catch (ClassNotFoundException exception) {
      throw new FatalException();
    }
    pool = new BasicDataSource();
    String url = props.getProperty("db_url");
    String login = props.getProperty("db_login");
    String password = props.getProperty("db_password");
    pool.setDriverClassName(driver);
    pool.setUsername(login);
    pool.setUrl(url);
    pool.setPassword(password);
  }

  @Override
  public PreparedStatement getPreparedStatement(String statement) {
    PreparedStatement ps = null;
    try {
      ps = connection.get().prepareStatement(statement);
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
    return ps;
  }


  public void startTransaction() {
    try {
      if (connection.get() == null) {
        connection.set(pool.getConnection());
      }
      connection.get().setAutoCommit(false);
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  public void commitTransaction() {
    try {
      if (connection.get() != null) {
        connection.get().commit();
        connection.get().close();
        connection.set(null);
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

  public void rollBackTransaction() {
    try {
      if (connection.get() != null) {
        connection.get().rollback();
        connection.get().close();
        connection.set(null);
      }
    } catch (SQLException sqlException) {
      throw new FatalException();
    }
  }

}
