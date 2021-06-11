package be.ipl.pae.dal.dao.type;

import be.ipl.pae.biz.type.IProjectTypeDto;

import java.util.List;


public interface IProjectTypeDao {

  List<IProjectTypeDto> findTypes();

  List<IProjectTypeDto> findTypesOfAQuote(String quoteId);

  void insertNewType(String type);
}
