package be.ipl.pae.biz.pictures;

public interface IPictureDto {

  int getIdPicture();

  void setIdPicture(int idPicture);

  String getSource();

  void setSource(String source);

  boolean isVisible();

  void setVisible(boolean visible);

  int getProjectType();

  void setProjectType(int projectType);

  void setQuote(int quote);

  int getQuote();

}
