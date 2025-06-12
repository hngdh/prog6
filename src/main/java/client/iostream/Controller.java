package client.iostream;

import client.network.ClientNetwork;
import client.read_mode.ModeManager;
import common.command_manager.CommandManager;
import common.data_processors.FormatChecker;
import common.data_processors.input.InputChecker;
import common.data_processors.input.InputReader;
import common.data_processors.input.InputSplitter;
import common.enums.CommandTypes;
import common.exceptions.LogException;
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
    private CommandManager commandManager;
    private ClientNetwork clientNetwork;

    public Controller() {
        Printer.printInfo("Client started");
        Printer.printInfo("Type 'help' for instructions");
    }

    public void prepare() {
        clientNetwork = new ClientNetwork();
        inputReader = new InputReader();
        inputReader.setReader();
        formatChecker = new FormatChecker();

        modeManager = new ModeManager();
        modeManager.init();
        commandManager = new CommandManager();
        commandManager.init();

    }

    public void run() throws LogException {
        while (true) {
            try {
                String input = inputReader.readLine();
                input = input.trim();
                preprocess(input);
                process(input);
                Printer.printInfo("Executed");
            } catch (LogException | WrongInputFormatException e) {
                Printer.printError(e.toString());
                Printer.printCondition("Command couldn't be executed!");
            } catch (IOException e) {
                LogUtil.logClientError(e);
            }
        }
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
        Renderer renderer = new Renderer();
        switch (type) {
            case INPUT_NEEDED -> modeManager.call(renderer, clientNetwork, command, argument);
            case NO_INPUT_NEEDED -> {
                Response response = clientNetwork.respond(new Request(command, argument, null));
                renderer.printResponse(response);
            }
        }
    }
}
