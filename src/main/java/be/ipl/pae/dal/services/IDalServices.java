package be.ipl.pae.dal.services;

import java.sql.PreparedStatement;

public interface IDalServices {

  PreparedStatement getPreparedStatement(String statement);

}
