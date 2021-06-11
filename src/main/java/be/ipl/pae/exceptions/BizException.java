package be.ipl.pae.exceptions;

@SuppressWarnings("serial")
public class BizException extends RuntimeException {

  private int code;

  public BizException(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}
