package common.data_processors.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * The {@code InputReader} class provides a convenient way to read data_processors from either a
 * data_processors or the standard data_processors (console). It uses an {@link InputStreamReader}
 * to read characters from the data_processors stream and provides a method to read a line of text.
 */
public class InputReader {
    private InputStreamReader reader;

    public InputReader() {
    }

    public void setReader(String fileName) throws IOException {
        reader = new InputStreamReader(new FileInputStream(fileName));
    }

    public void setReader() {
        reader = new InputStreamReader(System.in);
    }

    public String readLine() throws IOException {
        StringBuilder currentLine = new StringBuilder();
        int character;
        while ((character = reader.read()) != -1) {
            char currentChar = (char) character;
            if (currentChar == '\n') {
                break;
            } else {
                currentLine.append(currentChar);
            }
        }
        return currentLine.toString();
    }

    public LinkedList<String> readLines() throws IOException {
        LinkedList<String> scriptLines = new LinkedList<>();
        String line;
        while (!(line = readLine()).isEmpty()) {
            scriptLines.add(line.trim());
        }
        return scriptLines;
    }
}
