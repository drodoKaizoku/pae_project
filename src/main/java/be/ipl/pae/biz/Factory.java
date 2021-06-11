package be.ipl.pae.biz;

import be.ipl.pae.biz.client.Client;
import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.biz.pictures.Picture;
import be.ipl.pae.biz.quote.IQuoteDto;
import be.ipl.pae.biz.quote.Quote;
import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.biz.type.ProjectType;
import be.ipl.pae.biz.user.IUserDto;
import be.ipl.pae.biz.user.User;

public class Factory implements IFactory {

  @Override
  public IUserDto getEmptyUser() {
    return new User();
  }

  @Override
  public IQuoteDto getEmptyQuote() {
    return new Quote();
  }

  @Override
  public IClientDto getEmptyClient() {
    return new Client();
  }

  public IProjectTypeDto getEmptyProjectType() {
    return new ProjectType();
  }

  @Override
  public IPictureDto getEmptyPicture() {
    return new Picture();
  }

}
