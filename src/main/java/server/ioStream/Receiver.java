package server.ioStream;

import common.exceptions.LogException;
import common.io.LogUtil;
import common.objects.Flat;
import common.packets.Request;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import server.commandManager.CommandManager;
import server.data.CollectionManager;

/**
 * The {@code Receiver} class contains the actual implementation of the commands. It interacts with
 * the {@link CollectionManager} to perform operations on the collection and the {@link
 * CommandManager} to access command information. It handles user requests by delegating to the
 * appropriate methods of the CollectionManager.
 */
public class Receiver {
  private final CollectionManager collectionManager;
  private final CommandManager commandManager;

  public Receiver(CollectionManager collectionManager, CommandManager commandManager) {
    this.commandManager = commandManager;
    this.collectionManager = collectionManager;
  }

  private boolean notEmpty() {
    LinkedList<Flat> collection = collectionManager.getCollection();
    return !collection.isEmpty();
  }

  public void exit() {
    collectionManager.save();
    System.exit(0);
  }

  public void reload() {
    try {
      collectionManager.clear();
      collectionManager.loadData();
    } catch (LogException e) {
      LogUtil.logServerErrorWONotif(e);
    }
  }

  public List<String> ping() {
    List<String> result = new ArrayList<>();
    result.add("//pinged||");
    return result;
  }

  public List<String> help() {
    List<String> result = new ArrayList<>();
    commandManager
        .getClientCommandCollection()
        .forEach((name, command) -> result.add(command.getCommandInfo()));
    return result;
  }

  public void clear() {
    collectionManager.clear();
  }

  public List<String> info() {
    List<String> result = new ArrayList<>();
    result.add("Type of DS: LinkedList");
    result.add("Number of elements: " + collectionManager.getCollection().size());
    return result;
  }

  // All methods using stream API
  public List<String> show() {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      collectionManager
          .getCollection()
          .forEach(
              flat -> {
                result.addAll(flat.getEverything());
                result.add("");
              });
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public List<String> sort() {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      collectionManager.sort();
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public List<String> min_by_coordinates() {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      collectionManager.min_by_coordinates();
      collectionManager
          .getCollection()
          .forEach(
              flat -> {
                result.addAll(flat.getEverything());
                result.add("");
              });
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public void save() {
    collectionManager.save();
  }

  public void add(Request request) {
    collectionManager.add(request);
  }

  public List<String> filter_contains_name(Request request) {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      String name = request.getArgument();
      collectionManager
          .getCollection()
          .forEach(
              flat -> {
                if (flat.getName().contains(name)) {
                  result.addAll(flat.getEverything());
                  result.add("");
                }
              });
      if (result.isEmpty()) result.add("Nothing found");
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public List<String> print_field_ascending_house() {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      LinkedList<Flat> list = new LinkedList<>();
      collectionManager
          .getCollection()
          .forEach(
              flat -> {
                if (flat.getHouse() != null) list.add(flat);
              });
      list.sort(Comparator.comparing(a -> a.getHouse().getName()));
      list.forEach(flat -> result.add(flat.getPropsHouse().toString()));
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public List<String> remove_by_id(Request request) {
    List<String> result = new ArrayList<>();
    int id = Integer.parseInt(request.getArgument());
    if (notEmpty()) {
      int collectionSize = collectionManager.getCollection().size();
      if (id <= collectionSize) {
        collectionManager.remove_by_id(id);
      } else {
        result.add("Index out of bound (maximum " + collectionSize + ")");
      }
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public List<String> remove_first() {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      collectionManager.remove_first();
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public List<String> remove_lower(Request request) {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      collectionManager.remove_lower(request);
    } else {
      result.add("This collection is empty");
    }
    return result;
  }

  public List<String> update(Request request) {
    List<String> result = new ArrayList<>();
    if (notEmpty()) {
      collectionManager.update(request);
    } else {
      result.add("This collection is empty");
      return result;
    }
    if (collectionManager.getCollection().size() < Integer.parseInt(request.getArgument())) {
      result.add("Key doesn't exist");
    }
    return result;
  }
}
