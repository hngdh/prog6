package common.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
    }

    @Override
    public String toString() {
        return "File not found. Only accepted on src/main/resources/client";
    }
}
