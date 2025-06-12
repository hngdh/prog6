package server.data_processors;

import common.data_processors.input.ObjInputChecker;
import common.enums.FlatDataTypes;
import common.enums.HouseDataTypes;
import common.exceptions.LogException;
import common.exceptions.WrongDataException;
import common.io.LogUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code FileProcessor} class is responsible for validating and processing data read from a CSV
 * data_processors. It checks the format and validity of the data for both {@link common.objects.Flat} and {@link
 * common.objects.House} objects. It uses {@link ObjInputChecker} to perform specific checks on
 * individual data fields.
 */
public class FileProcessor {
    public FileProcessor() {
    }

    public List<String> checkFlatInfo(String str) throws WrongDataException, LogException {
        List<String> args = Arrays.asList(str.split(","));
        if (args.size() != 12) {
            throw new WrongDataException();
        }
        List<Boolean> checks = new LinkedList<>();
        checks.add(ObjInputChecker.checkFlatInput(args.get(0), FlatDataTypes.STRING));
        checks.add(ObjInputChecker.checkFlatInput(args.get(1), FlatDataTypes.COORDINATE_X));
        checks.add(ObjInputChecker.checkFlatInput(args.get(2), FlatDataTypes.COORDINATE_Y));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate.parse(args.get(3), formatter);
        } catch (DateTimeParseException e) {
            LogUtil.logServerError(e);
            throw new WrongDataException();
        }
        checks.add(ObjInputChecker.checkFlatInput(args.get(4), FlatDataTypes.AREA));
        checks.add(ObjInputChecker.checkFlatInput(args.get(5), FlatDataTypes.ROOMS));
        checks.add(ObjInputChecker.checkFlatInput(args.get(6), FlatDataTypes.SPACE));
        checks.add(ObjInputChecker.checkFlatInput(args.get(7), FlatDataTypes.HEATING));
        checks.add(ObjInputChecker.checkFlatInput(args.get(8), FlatDataTypes.TRANSPORT));
        for (Boolean check : checks) {
            if (!check) {
                throw new WrongDataException();
            }
        }
        return args;
    }

    public List<String> checkHouseInfo(String str) throws WrongDataException {
        List<String> args = Arrays.asList(str.split(","));
        if (!(args.get(10).equals("null") || args.get(11).equals("null"))) {
            List<Boolean> checks = new LinkedList<>();
            checks.add(ObjInputChecker.checkHouseInput(args.get(9), HouseDataTypes.STRING));
            checks.add(ObjInputChecker.checkHouseInput(args.get(10), HouseDataTypes.YEAR));
            checks.add(ObjInputChecker.checkHouseInput(args.get(11), HouseDataTypes.LIFTS));
            for (boolean check : checks) {
                if (!check) {
                    throw new WrongDataException();
                }
            }
        }
        List<String> tmp = new LinkedList<>();
        for (int i = 9; i < args.size(); i++) {
            tmp.add(args.get(i));
        }
        return tmp;
    }
}
