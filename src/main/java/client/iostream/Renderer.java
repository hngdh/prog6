package client.iostream;

import common.io.Printer;
import common.packets.Response;
import java.util.List;

public class Renderer {
  public Renderer() {}

  public void printResponse(Response response) {
    if (response != null) {
      List<String> result = response.getResult();
      if (result != null && !result.isEmpty()) {
        if (result.get(0).equals("//pinged||")) {
          Printer.printResult("pong");
        } else {
          result.forEach(Printer::printResult);
        }
      }
      List<String> notice = response.getNotice();
      if (notice != null) notice.forEach(Printer::printCondition);
    }
  }
}
