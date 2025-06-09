package server.iostream;

import client.processors.FormatChecker;
import client.processors.input.InputChecker;
import client.processors.input.InputReader;
import client.processors.input.InputSplitter;
import client.read_mode.ModeManager;
import common.command_manager.CommandManager;
import common.enums.CommandTypes;
import common.exceptions.LogException;
import common.exceptions.WrongInputFormatException;
import common.io.LogUtil;
import common.io.Printer;
import common.packets.Request;
import server.data.CollectionManager;

import java.io.IOException;

/**
 * The {@code Handler} class is the main entry point for processing user commands. It initializes
 * and manages various components, including processors reading, command classification, execution, and
 * collection management. It interacts with the user through the console, providing instructions and
 * feedback.
 */
public class Handler {
    private InputReader inputReader;
    private ModeManager modeManager;
    private Invoker invoker;
    private FormatChecker formatChecker;
    private CommandManager commandManager;
    private Receiver receiver;

    public Handler() {
        Printer.printInfo("This program helps you managing flats' information");
        Printer.printInfo("Type 'help' to get full instructions");
        Printer.printInfo("Type 'exit' to exit program");
    }

    public void prepare(String fileName) {
        try {
            inputReader = new InputReader();
            inputReader.setReader();
            formatChecker = new FormatChecker();

            modeManager = new ModeManager();
            modeManager.init();
            commandManager = new CommandManager();
            commandManager.init();
            CollectionManager collectionManager = new CollectionManager(fileName);

            receiver = new Receiver(collectionManager, commandManager);
            invoker = new Invoker(commandManager, receiver);

            collectionManager.loadData();
        } catch (LogException e) {
            Printer.printError(e.toString());
        }
    }

    public void run() throws LogException {
        while (receiver.programState.equals("start")) {
            try {
                String input = inputReader.readLine();
                input = input.trim();
                preprocess(input);
                process(input);
                Printer.printInfo("Executed.");
            } catch (LogException | WrongInputFormatException e) {
                Printer.printError(e.toString());
                Printer.printCondition("Command couldn't be executed!");
            } catch (IOException e) {
                LogUtil.log(e);
            }
        }
        standby();
    }

    public void standby() throws LogException {
        Printer.printCondition("Program moved to standby mode. Use 'start' to continue program");
        while (receiver.programState.equals("stop")) {
            try {
                String input = inputReader.readLine();
                if (input.equals("start")) {
                    receiver.start();
                    Printer.printCondition("Program started over");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LogException e) {
                throw new LogException();
            }
        }
        run();
    }

    public void preprocess(String input) throws WrongInputFormatException {
        if (!InputChecker.checkInput(input)) {
            throw new WrongInputFormatException();
        }
        formatChecker.checkFormat(InputSplitter.getCommand(input), InputSplitter.getArg(input));
    }

    public void process(String input) throws LogException, IOException {
        String command = InputSplitter.getCommand(input);
        String argument = InputSplitter.getArg(input);
        CommandTypes type = commandManager.getCommand(command).getCommandClassifier();
        switch (type) {
            case INPUT_NEEDED -> modeManager.call(invoker, command, argument);
            case NO_INPUT_NEEDED -> invoker.call(command, new Request(argument, null));
        }
    }
}
