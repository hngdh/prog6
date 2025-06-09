package io.file;

import exceptions.LogException;
import exceptions.WrongDataException;
import io.input.Builder;
import java.io.IOException;
import java.util.List;
import main_objects.Flat;

/**
 * The {@code CSVReader} class is responsible for reading data from a CSV file and creating {@link
 * Flat} objects. It utilizes the {@link FileProcessor} to validate and process the data extracted
 * from the CSV file.
 */
public class CSVReader {
  private final FileProcessor processor;

  public CSVReader() throws IOException {
    processor = new FileProcessor();
  }

  public Flat loadObj(String str) throws WrongDataException, LogException {
    List<String> flatInfo = processor.checkFlatInfo(str);
    List<String> houseInfo = processor.checkHouseInfo(str);

    return Builder.buildFlat(flatInfo, houseInfo);
  }
}
