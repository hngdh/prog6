package client.ioStream;

import client.commandManager.CommandClassifier;
import client.commandManager.FormatChecker;
import client.network.ClientNetwork;
import client.readMode.ModeManager;
import common.dataProcessors.InputChecker;
import common.dataProcessors.InputReader;
import common.dataProcessors.InputSplitter;
import common.enums.CommandTypes;
import common.exceptions.LogException;
import common.exceptions.WrongCommandException;
import common.exceptions.WrongInputFormatException;
import common.io.LogUtil;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import java.io.IOException;

public class Controller {
  private InputReader inputReader;
  private ModeManager modeManager;
  private FormatChecker formatChecker;
  private CommandClassifier commandClassifier;
  private ClientNetwork clientNetwork;
  private boolean isAwake;

  public Controller() {
    Printer.printInfo("Client started");
    Printer.printInfo("Type 'help' for instructions");
  }

  public void prepare(String port) {
    clientNetwork = new ClientNetwork(port);
    inputReader = new InputReader();
    inputReader.setReader();
    formatChecker = new FormatChecker();
    formatChecker.init();
    modeManager = new ModeManager();
    modeManager.init();
    commandClassifier = new CommandClassifier();
    commandClassifier.init();
    isAwake = true;
  }

  public void run() {
    while (isAwake) {
      try {
        String input = inputReader.readLine();
        preprocess(input);
      } catch (WrongInputFormatException | WrongCommandException e) {
        Printer.printError(e.toString());
        Printer.printCondition("Command couldn't be executed!");
      } catch (IOException e) {
        LogUtil.logClientErrorWONotif(e);
      }
    }
  }

  public void preprocess(String input)
      throws IOException, WrongInputFormatException, WrongCommandException {
    if (!InputChecker.checkInput(input)) throw new WrongCommandException();
    formatChecker.checkFormat(InputSplitter.getCommand(input), InputSplitter.getArg(input));
    String command = InputSplitter.getCommand(input);
    String argument = InputSplitter.getArg(input);
    CommandTypes type = commandClassifier.getCommandClassifier(command);
    if (!command.equals("exit")) {
      try {
        process(command, argument, type);
      } catch (LogException e) {
        Printer.printError(e);
      }
    } else {
      exit();
    }
  }

  public void process(String command, String argument, CommandTypes type)
      throws LogException, IOException {
    Renderer renderer = new Renderer();
    switch (type) {
      case INPUT_NEEDED -> modeManager.call(renderer, clientNetwork, command, argument);
      case NO_INPUT_NEEDED -> {
        Response response = clientNetwork.respond(new Request(command, argument, null));
        renderer.printResponse(response);
      }
    }
  }

  public void exit() {
    Printer.printInfo("Exiting...");
    System.exit(0);
  }
}
