package common.enums;

/**
 * The {@code CommandTypes} enum represents whether a command requires user processors or not. It defines
 * the possible states for processors requirements, useful for setting suitable {@link
 * read_mode.ReaderMode}.
 */
public enum CommandTypes {
    INPUT_NEEDED,
    NO_INPUT_NEEDED
}
