package server.data;

import common.dataProcessors.InputReader;
import common.exceptions.LogException;
import common.exceptions.WrongDataException;
import common.io.LogUtil;
import common.io.Printer;
import common.objects.Flat;
import common.packets.Request;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import server.dataProcessors.CSVReader;
import server.dataProcessors.CSVWriter;

/**
 * The {@code CollectionManager} class manages a collection of {@link Flat} objects stored in a
 * {@link LinkedList}. It provides methods for adding, removing, updating, and retrieving elements
 * from the collection. It also handles saving and loading the collection to/from a CSV
 * dataProcessors.
 */
public class CollectionManager {
  private final LinkedList<Flat> collection = new LinkedList<>();
  private final String fileName;

  public CollectionManager(String fileName) {
    this.fileName = fileName;
  }

  public LinkedList<Flat> getCollection() {
    return collection;
  }

  public void clear() {
    //    while (!collection.isEmpty()) collection.removeFirst();
    collection.clear();
  }

  public void sort() {
    collection.sort(Comparator.comparing(Flat::getName));
  }

  public void min_by_coordinates() {
    collection.sort(
        Comparator.comparing(
            a ->
                Float.parseFloat(a.getCoordinates().getX())
                    + Float.parseFloat(a.getCoordinates().getY())));
  }

  public void save() {
    try {
      CSVWriter writer = new CSVWriter(fileName);
      for (Flat flat : collection) {
        writer.writeLines(flat.getAllFields());
      }
    } catch (IOException e) {
      Printer.printError("Program even couldn't be saved properly :)");
    }
  }

  public void add(Request request) {
    Flat flat = request.getFlat();
    flat.setId(collection.size() + 1);
    collection.add(flat);
  }

  public void remove_by_id(int id) {
    collection.remove(id - 1);
  }

  public void remove_first() {
    collection.removeFirst();
  }

  public void remove_lower(Request request) {
    Flat compareFlat = request.getFlat();
    LinkedList<Flat> tempList = new LinkedList<>(collection);
    tempList.forEach(
        flat -> {
          if (flat.toString().compareTo(compareFlat.toString()) < 0) collection.remove(flat);
        });
  }

  public void update(Request request) {
    String key = request.getArgument();
    if (key != null
        && !key.isEmpty()
        && key.matches("-?[0-9]+")
        && Integer.parseInt(key) <= collection.size()) {
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
      LogUtil.logServerError(e);
    }
    Printer.printResult("Loaded " + collection.size() + " flat(s) from file");
  }
}
