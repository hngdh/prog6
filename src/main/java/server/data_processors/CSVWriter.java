package server.data_processors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The {@code CSVWriter} class is responsible for writing data to a CSV processors. It takes a list of
 * {@link StringBuilder} objects, representing the data to be written, and writes them to the
 * specified processors.
 */
public class CSVWriter {
    private final BufferedWriter bufferedWriter;

    public CSVWriter(String fileName) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(fileName));
    }

    public void writeLines(List<StringBuilder> flat) throws IOException {
        for (int i = 0; i < flat.size() - 1; i++) {
            bufferedWriter.write(flat.get(i).append(",").toString());
        }
        bufferedWriter.write(flat.get(flat.size() - 1).toString() + "\n");
        bufferedWriter.flush();
    }
}
