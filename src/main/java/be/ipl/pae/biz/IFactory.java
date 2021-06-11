package be.ipl.pae.biz;

import be.ipl.pae.biz.client.IClientDto;
import be.ipl.pae.biz.pictures.IPictureDto;
import be.ipl.pae.biz.quote.IQuoteDto;
import be.ipl.pae.biz.type.IProjectTypeDto;
import be.ipl.pae.biz.user.IUserDto;

public interface IFactory {

  IUserDto getEmptyUser();

  IQuoteDto getEmptyQuote();

  IClientDto getEmptyClient();

  IProjectTypeDto getEmptyProjectType();

  IPictureDto getEmptyPicture();

}
