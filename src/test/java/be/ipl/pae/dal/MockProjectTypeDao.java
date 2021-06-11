package be.ipl.pae.dal;

import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.dal.dao.type.IProjectTypeDao;

import java.util.ArrayList;
import java.util.List;

public class MockProjectTypeDao implements IProjectTypeDao {

  @Override
  public List<IProjectTypeDto> findTypes() {
    // TODO Auto-generated method stub
    return new ArrayList<IProjectTypeDto>();
  }

  @Override
  public List<IProjectTypeDto> findTypesOfAQuote(String quoteId) {
    if (quoteId.equals("null")) {
      return null;
    }
    return new ArrayList<IProjectTypeDto>();
  }

  @Override
  public void insertNewType(String type) {
    // TODO Auto-generated method stub

  }

}
