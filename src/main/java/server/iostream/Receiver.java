package server.iostream;

import common.command_manager.CommandManager;
import common.exceptions.LogException;
import common.io.Printer;
import common.objects.Flat;
import common.packets.Request;
import server.data.CollectionManager;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * The {@code Receiver} class contains the actual implementation of the commands. It interacts with
 * the {@link CollectionManager} to perform operations on the collection and the {@link
 * CommandManager} to access command information. It handles user requests by delegating to the
 * appropriate methods of the CollectionManager.
 */
public class Receiver {
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    String programState;

    public Receiver(CollectionManager collectionManager, CommandManager commandManager) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        programState = "start";
    }

    private boolean notEmpty() {
        LinkedList<Flat> collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            Printer.printResult("The collection is empty.");
            return false;
        }
        return true;
    }

    public void exit() {
        programState = "stop";
    }

    public void start() throws LogException {
        if (Objects.equals(programState, "stop")) {
            programState = "start";
            collectionManager.reload();
        } else Printer.printResult("Program already started");
    }

    public void help() {
        commandManager
                .getCommandCollection()
                .forEach((name, command) -> Printer.printResult(command.getCommandInfo()));
    }

    public void clear() {
        collectionManager.clear();
    }

    public void info() {
        Printer.printResult("Type of DS: LinkedList");
        Printer.printResult("Number of elements: " + collectionManager.getCollection().size());
    }

    public void show() {
        if (notEmpty()) {
            collectionManager.getCollection().forEach(Flat::printEverything);
        }
    }

    public void sort() {
        collectionManager.sort();
    }

    public void execute_script() {
        Printer.printCondition("File being executed...");
    }

    public void min_by_coordinates() {
        collectionManager.min_by_coordinates();
    }

    public void save() throws LogException {
        collectionManager.save();
    }

    public void add(Request request) {
        collectionManager.add(request);
    }

    public void filter_contains_name(Request request) {
        if (notEmpty()) {
            String name = request.getArgument();
            for (Flat flat : collectionManager.getCollection()) {
                if (flat.getName().contains(name)) flat.printEverything();
            }
        }
    }

    public void print_field_ascending_house() {
        if (notEmpty()) {
            LinkedList<Flat> list = new LinkedList<>();
            for (Flat flat : collectionManager.getCollection())
                if (flat.getHouse() != null) list.add(flat);
            list.sort(Comparator.comparing(a -> a.getHouse().getName()));
            for (Flat flat : list) flat.printHouse();
        }
    }

    public void remove_by_id(Request request) {
        collectionManager.remove_by_id(request);
    }

    public void remove_first() {
        collectionManager.remove_first();
    }

    public void remove_lower(Request request) {
        collectionManager.remove_lower(request);
    }

    public void update(Request request) {
        collectionManager.update(request);
    }
}
