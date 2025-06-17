package server.dataProcessors;

import common.dataProcessors.ObjBuilder;
import common.exceptions.LogException;
import common.exceptions.WrongDataException;
import common.objects.Flat;
import java.io.IOException;
import java.util.List;

/**
 * The {@code CSVReader} class is responsible for reading data from a CSV dataProcessors and
 * creating {@link Flat} objects. It utilizes the {@link FileProcessor} to validate and process the
 * data extracted from the CSV dataProcessors.
 */
public class CSVReader {
  private final FileProcessor processor;

  public CSVReader() throws IOException {
    processor = new FileProcessor();
  }

  public Flat loadObj(String str) throws WrongDataException, LogException {
    List<String> flatInfo = processor.checkFlatInfo(str);
    List<String> houseInfo = processor.checkHouseInfo(str);

    return ObjBuilder.buildFlat(flatInfo, houseInfo);
  }
}
