package io.quarkiverse.jef.java.embedded.framework.runtime.dev;

import java.util.ArrayList;
import java.util.List;

import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.Board;
import io.quarkiverse.jef.java.embedded.framework.mcu.core.boards.BoardPin;

@SuppressWarnings("unused")
public class BoardHolder {
    private final Board board;

    public BoardHolder(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public List<List<String>> getRecords() {
        List<List<String>> result = new ArrayList<>();
        int count = board.getPinCount();
        for (int i = 1; i <= count; i += 2) {
            writePinsLine(i, result);
        }
        return result;
    }

    private void writePinsLine(int i, List<List<String>> root) {
        BoardPin pin = board.getPin(i);
        List<String> line = new ArrayList<>();
        line.add(pinToString(pin.getCpuPinNumber()));
        line.add(pin.getName());
        line.add(pin.getPinFunctionName());
        line.add(getPinInOut(pin.getPinInfo()));
        line.add(String.valueOf(pin.getPinNumber()));

        pin = board.getPin(i + 1);
        line.add(String.valueOf(pin.getPinNumber()));
        line.add(getPinInOut(pin.getPinInfo()));
        line.add(pin.getPinFunctionName());
        line.add(pin.getName());
        line.add(pinToString(pin.getCpuPinNumber()));

        root.add(line);
    }

    private String getPinInOut(BoardPin.BoardPinInfo pinInfo) {
        if (pinInfo.isDummyPin())
            return "-";
        if (pinInfo.isActiveLow())
            return "0";
        return "1";

    }

    private static String pinToString(int pin) {
        return pin == -1 ? "-" : Integer.toString(pin);
    }

}
