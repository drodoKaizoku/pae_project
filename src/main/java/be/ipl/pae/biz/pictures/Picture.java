package be.ipl.pae.biz.pictures;

public class Picture implements IPicture {

  private int idPicture;
  private String source;
  private int quote;
  private boolean visible;
  private int projectType;

  /**
   * Constructor of picture.
   * 
   * @param idPicture id of picture
   * @param source base64 src of picture
   * @param visible bool at true if picture is visible on the site
   * @param projectType the project type of the picture
   */
  public Picture(int idPicture, String source, int quote, boolean visible, int projectType) {
    this.idPicture = idPicture;
    this.source = source;
    this.setQuote(quote);
    this.visible = visible;
    this.projectType = projectType;
  }

  public Picture() {

  }

  public int getIdPicture() {
    return idPicture;
  }

  public void setIdPicture(int idPicture) {
    this.idPicture = idPicture;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public int getProjectType() {
    return projectType;
  }

  public void setProjectType(int projectType) {
    this.projectType = projectType;
  }

  @Override
  public String toString() {
    return "Picture [idPicture=" + idPicture + ", source=" + source + ", visible=" + visible
        + ", projectType=" + projectType + "]";
  }

  public int getQuote() {
    return quote;
  }

  public void setQuote(int quote) {
    this.quote = quote;
  }

}
