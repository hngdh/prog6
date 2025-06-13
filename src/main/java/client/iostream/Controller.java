package client.iostream;

import client.command_utils.CommandClassifier;
import client.command_utils.FormatChecker;
import client.network.ClientNetwork;
import client.read_mode.ModeManager;
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
    private CommandClassifier commandClassifier;
    private ClientNetwork clientNetwork;
    private boolean isAwake;

    public Controller() {
        Printer.printInfo("Client started");
        Printer.printInfo("Type 'help' for instructions");
    }

    public void prepare() {
        clientNetwork = new ClientNetwork();
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

    public void run() throws LogException {
        while (isAwake) {
            try {
                String input = inputReader.readLine();
                preprocess(input);
            } catch (WrongInputFormatException e) {
                Printer.printError(e.toString());
                Printer.printCondition("Command couldn't be executed!");
            } catch (IOException e) {
                LogUtil.logClientError(e);
            }
        }
    }

    public void preprocess(String input) throws IOException, LogException, WrongInputFormatException {
        if (!InputChecker.checkInput(input)) {
            throw new WrongInputFormatException();
        }
        formatChecker.checkFormat(InputSplitter.getCommand(input), InputSplitter.getArg(input));
        String command = InputSplitter.getCommand(input);
        String argument = InputSplitter.getArg(input);
        CommandTypes type = commandClassifier.getCommandClassifier(command);
        if (!command.equals("exit")) {
            process(command, argument, type);
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

    private void exit() {
        Printer.printInfo("Exiting...");
        System.exit(0);
    }

    public void shutdown(){
        isAwake = false;
        exit();
    }
}
