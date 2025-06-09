package main_objects;

import exceptions.LogException;
import exceptions.WrongDataException;
import exceptions.WrongKeyException;
import io.LogUtil;
import io.Printer;
import io.file.CSVReader;
import io.file.CSVWriter;
import io.input.InputChecker;
import io.input.InputReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import packets.Request;

/**
 * The {@code CollectionManager} class manages a collection of {@link Flat} objects stored in a
 * {@link LinkedList}. It provides methods for adding, removing, updating, and retrieving elements
 * from the collection. It also handles saving and loading the collection to/from a CSV file.
 */
public class CollectionManager {
  private final LinkedList<Flat> collection = new LinkedList<>();
  private final String fileName;

  public CollectionManager(String fileName) {
    this.fileName = fileName;
  }

  private boolean notEmpty() {
    if (collection.isEmpty()) {
      Printer.printResult("The collection is empty.");
      return false;
    }
    return true;
  }

  public LinkedList<Flat> getCollection() {
    return collection;
  }

  public void reload() throws LogException {
    collection.clear();
    loadData();
  }

  public void clear() {
    while (!collection.isEmpty()) collection.removeFirst();
  }

  public void sort() {
    if (notEmpty()) {
      collection.sort(Comparator.comparing(Flat::getName));
    }
  }

  public void min_by_coordinates() {
    if (notEmpty()) {
      collection.sort(
          Comparator.comparing(
              a ->
                  Float.parseFloat(a.getCoordinates().getX())
                      + Float.parseFloat(a.getCoordinates().getY())));
    }
  }

  public void save() throws LogException {
    try {
      CSVWriter writer = new CSVWriter(fileName);
      for (Flat flat : collection) {
        writer.writeLines(flat.getAllFields());
      }
    } catch (IOException e) {
      LogUtil.log(e);
      throw new LogException();
    }
  }

  public void add(Request request) {
    Flat flat = request.getFlat();
    collection.add(flat);
  }

  public void remove_by_id(Request request) {
    if (notEmpty()) {
      int id = Integer.parseInt(request.getArgument());
      collection.remove(id - 1);
    }
  }

  public void remove_first() {
    if (notEmpty()) {
      collection.removeFirst();
    }
  }

  public void remove_lower(Request request) {
    if (notEmpty()) {
      Flat compareFlat = request.getFlat();
      LinkedList<Flat> tempList = new LinkedList<>(collection);
      for (Flat flat : tempList) {
        if (flat.toString().compareTo(compareFlat.toString()) < 0) {
          collection.remove(flat);
        }
      }
    }
  }

  public void update(Request request) {
    String key = request.getArgument();
    if (key == null
        || key.isEmpty()
        || !InputChecker.checkInteger(key)
        || Integer.parseInt(key) > collection.size()) {
      try {
        throw new WrongKeyException();
      } catch (WrongKeyException e) {
        Printer.printError(e.toString());
      }
    } else {
      Flat flat = request.getFlat();
      int id = Integer.parseInt(key) - 1;
      flat.setId(Integer.parseInt(key));
      collection.remove(id);
      collection.add(id, flat);
    }
  }

  public void loadData() throws LogException {
    try {
      CSVReader reader = new CSVReader();
      InputReader inputReader = new InputReader();
      inputReader.setReader(fileName);
      String str;
      int counter = 1;
      while ((str = inputReader.readLine()) != null && !str.isEmpty()) {
        try {
          Flat flat = reader.loadObj(str);
          flat.setId(counter);
          collection.add(flat);
        } catch (WrongDataException e) {
          Printer.printError("Can't load flat's information on line " + counter);
        }
        counter++;
      }
    } catch (IOException e) {
      LogUtil.log(e);
      throw new LogException();
    }
    Printer.printResult("Loaded " + collection.size() + " flat(s) from the file.");
  }
}
