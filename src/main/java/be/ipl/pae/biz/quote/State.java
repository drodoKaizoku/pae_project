package be.ipl.pae.biz.quote;

public enum State {

  INTRODUCED("Introduced"), ORDER_CONFIRMED("Order confirmed"), DATE_CONFIRMED(
      "Date start of work confirmed"), CANCELLED("Cancelled"), MID_BILL_SENT(
          "Bill mid-work sent"), END_BILL_SENT(
              "Bill end-work sent"), VISIBLE("Visible"), CLOSED("Closed");

  private final String value;

  State(String value) {
    this.value = value;
  }

  public String getValue() {

    return this.value;
  }

  /**
   * Get the right state if it doesn't exist return null.
   * 
   * @return the state whose value is the parameter text
   */
  public static State fromString(String text) {
    for (State s : State.values()) {
      if (s.getValue().equals(text)) {
        return s;
      }
    }
    return null;
  }

}
