package read_mode;

import command_utilities.CommandManager;
import enums.CommandTypes;
import enums.FlatDataTypes;
import enums.HouseDataTypes;
import exceptions.LogException;
import exceptions.WrongCommandException;
import exceptions.WrongFileInputException;
import exceptions.WrongInputException;
import io.LogUtil;
import io.Printer;
import io.input.*;
import iostream.Invoker;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import main_objects.Flat;
import packets.Request;

/**
 * The {@code FileReaderMode} class implements the {@link ReaderMode} interface and provides
 * functionality to read commands and data from a file and execute them.
 */
public class FileReaderMode implements ReaderMode {
  private final CommandManager commandManager;

  public FileReaderMode() {
    commandManager = new CommandManager();
    commandManager.init();
  }

  public Flat build(LinkedList commandList) throws LogException, IOException {
    List<String> flatInfo = new LinkedList<>();
    List<String> houseInfo = new LinkedList<>();

    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.STRING));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.COORDINATE_X));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.COORDINATE_Y));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.DATE));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.AREA));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.ROOMS));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.SPACE));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.HEATING));
    flatInfo.add(getFlatFileInput(commandList, FlatDataTypes.TRANSPORT));
    if (commandList.getFirst().toString().equals("yes")) {
      commandList.removeFirst();
      houseInfo.add(getHouseFileInput(commandList, HouseDataTypes.STRING));
      houseInfo.add(getHouseFileInput(commandList, HouseDataTypes.YEAR));
      houseInfo.add(getHouseFileInput(commandList, HouseDataTypes.LIFTS));
    } else {
      commandList.removeFirst();
      for (int i = 0; i < 3; i++) {
        houseInfo.add("null");
        commandList.removeFirst();
      }
    }
    return Builder.buildFlat(flatInfo, houseInfo);
  }

  public String getFlatFileInput(LinkedList commandList, FlatDataTypes dataType) {
    String str = commandList.getFirst().toString();
    commandList.removeFirst();
    boolean check = ObjInputChecker.checkFlatInput(str, dataType);
    if (check) {
      return str;
    } else {
      throw new WrongInputException();
    }
  }

  public String getHouseFileInput(LinkedList commandList, HouseDataTypes dataType) {
    String str = commandList.getFirst().toString();
    commandList.removeFirst();
    boolean check = ObjInputChecker.checkHouseInput(str, dataType);
    if (check) {
      return str;
    } else {
      throw new WrongInputException();
    }
  }

  public String getCommand(LinkedList commandList) {
    String input = commandList.getFirst().toString();
    commandList.removeFirst();
    if (input == null || input.isEmpty()) return null;
    if (!InputChecker.checkInput(input)) {
      throw new WrongFileInputException();
    }
    if (!commandManager.isCommand(InputSplitter.getCommand(input.toLowerCase())))
      throw new WrongCommandException();
    return input;
  }

  @Override
  public void executeMode(Invoker invoker, String commandName, String currentFile)
      throws LogException {
    try {
      invoker.call("execute_script", new Request(null, null));
      InputReader inputReader = new InputReader();
      inputReader.setReader(currentFile);
      LinkedList commandList = inputReader.readLines();
      while (!commandList.isEmpty()) {
        String input;
        if ((input = getCommand(commandList)) != null) {
          String command = InputSplitter.getCommand(input);
          Printer.printInfo(command);
          if (!command.equals("execute_script")) {
            String argument = InputSplitter.getArg(input);
            CommandTypes type = commandManager.getCommand(command).getCommandClassifier();
            switch (type) {
              case NO_INPUT_NEEDED -> invoker.call(command, new Request(argument, null));
              case INPUT_NEEDED -> {
                Flat flat = build(commandList);
                invoker.call(command, new Request(argument, flat));
              }
            }
          } else {
            Printer.printInfo("Skipped command execute_script");
          }
        }
      }
    } catch (IOException e) {
      LogUtil.log(e);
      throw new LogException();
    }
  }
}
