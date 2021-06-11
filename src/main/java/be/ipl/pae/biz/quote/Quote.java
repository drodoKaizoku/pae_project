package be.ipl.pae.biz.quote;

import be.ipl.pae.biz.client.Client;
import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.biz.pictures.IPictureDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Quote implements IQuote {

  private int idQuote;
  private LocalDate dateQuote;
  private int client;
  private String workPeriod;
  private IPictureDto favoritePicture;
  private List<IPictureDto> picturesBefore;
  private List<IPictureDto> picturesAfter;
  private State state;
  private ArrayList<String> listType;

  private double fullAmount;
  private LocalDate dateStartWork;
  private IClientDto objectClient;

  /**
   * Constructor, creates a quote with given parameters.
   * 
   * @param idQuote the quote's id
   * @param dateQuote the quote's date
   * @param workPeriod the quote's work perdiod
   * @param fullAmount the quote's full amount
   * @param dateStartWork the quote's date when work started
   */
  public Quote(int idQuote, LocalDate dateQuote, int client, IPictureDto favoritePicture,
      List<IPictureDto> picturesBefore, List<IPictureDto> picturesAfter, String workPeriod,
      double fullAmount, LocalDate dateStartWork, Client clientObject) {
    this.idQuote = idQuote;
    this.dateQuote = dateQuote;
    this.client = client;
    this.favoritePicture = favoritePicture;
    this.picturesBefore = picturesBefore;
    this.picturesAfter = picturesAfter;
    this.workPeriod = workPeriod;
    this.fullAmount = fullAmount;
    this.dateStartWork = dateStartWork;
    this.setObjectClient(clientObject);
  }

  /**
   * Empty Constructor of quote, only sets state to OK.
   * 
   */
  public Quote() {

  }

  @Override
  public int getIdQuote() {
    return idQuote;
  }

  @Override
  public void setIdQuote(int idQuote) {
    this.idQuote = idQuote;
  }

  @Override
  public LocalDate getDateQuote() {
    return dateQuote;
  }

  @Override
  public void setDateQuote(LocalDate dateQuote) {
    this.dateQuote = dateQuote;
  }

  @Override
  public int getClient() {
    return this.client;
  }

  @Override
  public void setClient(int client) {
    this.client = client;
  }

  @Override
  public String getWorkPeriod() {
    return workPeriod;
  }

  @Override
  public void setWorkPeriod(String workPeriod) {
    this.workPeriod = workPeriod;
  }

  @Override
  public double getFullAmount() {
    return fullAmount;
  }

  @Override
  public void setFullAmount(double fullAmount) {
    this.fullAmount = fullAmount;
  }

  @Override
  public LocalDate getDateStartWork() {
    return dateStartWork;
  }

  @Override
  public void setDateStartWork(LocalDate dateStartWork) {
    this.dateStartWork = dateStartWork;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((dateQuote == null) ? 0 : dateQuote.hashCode());
    result = prime * result + ((dateStartWork == null) ? 0 : dateStartWork.hashCode());
    long temp;
    temp = Double.doubleToLongBits(fullAmount);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + idQuote;
    result = prime * result + ((workPeriod == null) ? 0 : workPeriod.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Quote other = (Quote) obj;
    if (dateQuote == null) {
      if (other.dateQuote != null) {
        return false;
      }
    } else {
      if (!dateQuote.equals(other.dateQuote)) {
        return false;
      }
    }
    if (dateStartWork == null) {
      if (other.dateStartWork != null) {
        return false;
      }
    } else {
      if (!dateStartWork.equals(other.dateStartWork)) {
        return false;
      }
    }
    if (Double.doubleToLongBits(fullAmount) != Double.doubleToLongBits(other.fullAmount)) {
      return false;
    }
    if (idQuote != other.idQuote) {
      return false;
    }
    if (workPeriod == null) {
      if (other.workPeriod != null) {
        return false;
      }
    } else {
      if (!workPeriod.equals(other.workPeriod)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return "Quote [idQuote=" + idQuote + ", dateQuote=" + dateQuote + ", client=" + client
        + ", workPeriod=" + workPeriod + ", favoritePicture=" + favoritePicture
        + ", picturesBefore=" + picturesBefore + ", picturesAfter=" + picturesAfter + ", state="
        + state + ", fullAmount=" + fullAmount + ", dateStartWork=" + dateStartWork + "]";
  }

  public IPictureDto getFavoritePicture() {
    return favoritePicture;
  }

  public void setFavoritePicture(IPictureDto favoritePicture) {
    this.favoritePicture = favoritePicture;
  }

  public List<IPictureDto> getPicturesBefore() {
    return picturesBefore;
  }

  public void setPicturesBefore(List<IPictureDto> picturesBefore) {
    this.picturesBefore = picturesBefore;
  }

  public List<IPictureDto> getPicturesAfter() {
    return picturesAfter;
  }

  public void setPicturesAfter(List<IPictureDto> picturesAfter) {
    this.picturesAfter = picturesAfter;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public IClientDto getObjectClient() {
    return objectClient;
  }

  public void setObjectClient(IClientDto objectClient) {
    this.objectClient = objectClient;
  }

  public ArrayList<String> getListType() {
    return listType;
  }

  public void setListType(ArrayList<String> listType) {
    this.listType = listType;
  }

}
