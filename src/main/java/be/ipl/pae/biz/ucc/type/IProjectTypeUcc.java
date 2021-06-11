package be.ipl.pae.biz.ucc.type;

import be.ipl.pae.biz.type.IProjectTypeDto;

import java.util.List;


public interface IProjectTypeUcc {

  List<IProjectTypeDto> retrieveAllTypes();

  List<IProjectTypeDto> retrieveAllTypesOfAQuote(String quoteId);

  void insertNewType(String type);
}
