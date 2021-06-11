package be.ipl.pae.biz.type;

public class ProjectType implements IProjectTypeDto {

  private int idProjectType;
  private String projectTypeName;

  /**
   * Constructor of project type.
   * 
   * @param idProjectType the project type id
   * @param projectTypeName the project type name
   */
  public ProjectType(int idProjectType, String projectTypeName) {
    this.idProjectType = idProjectType;
    this.projectTypeName = projectTypeName;
  }

  public ProjectType() {}

  @Override
  public int getIdProjectType() {
    return idProjectType;
  }

  @Override
  public String getProjectTypeName() {
    return projectTypeName;
  }

  @Override
  public void setIdProjectType(int idProjectType) {
    this.idProjectType = idProjectType;
  }

  @Override
  public void setProjetTypeName(String projetTypeName) {
    this.projectTypeName = projetTypeName;
  }

}
