package common.enums;

/**
 * The {@code CommandTypes} enum represents whether a command requires user data_processors or not.
 * It defines the possible states for data_processors requirements, useful for setting suitable
 * {@link client.read_mode.ReaderMode}.
 */
public enum CommandTypes {
    INPUT_NEEDED,
    NO_INPUT_NEEDED
}
