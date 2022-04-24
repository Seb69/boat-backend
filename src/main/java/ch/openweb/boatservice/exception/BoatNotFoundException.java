package ch.openweb.boatservice.exception;

public class BoatNotFoundException extends RuntimeException {
  public BoatNotFoundException(String s) {
    super(s);
  }
}
