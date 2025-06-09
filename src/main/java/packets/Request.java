package packets;

import main_objects.Flat;

/**
 * The {@code Request} class represents a request containing an argument and a {@link Flat} object.
 * It is used to encapsulate the data needed for various operations on the collection of flats.
 */
public class Request {
  private final String argument;
  private final Flat flat;

  public Request(String argument, Flat flat) {
    this.argument = argument;
    this.flat = flat;
  }

  public String getArgument() {
    return argument;
  }

  public Flat getFlat() {
    return flat;
  }
}
