package be.ipl.pae.exceptions;

@SuppressWarnings("serial")
public class FatalException extends RuntimeException {

  public FatalException() {
    super();
  }

  public FatalException(String msg) {
    super(msg);
  }
}
