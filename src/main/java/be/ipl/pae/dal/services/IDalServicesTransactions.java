package be.ipl.pae.dal.services;

public interface IDalServicesTransactions {

  void startTransaction();

  void commitTransaction();

  void rollBackTransaction();

}
