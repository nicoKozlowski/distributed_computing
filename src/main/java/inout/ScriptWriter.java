package inout;

import list.List;

public class ScriptWriter implements Output {

    private final StringBuilder output;

    public ScriptWriter() {
        this.output = new StringBuilder();
    }

    public void print(String s) {
        this.output.append(s);
    }

    @Override
    public void printLine(String s) {
        this.output.append(s).append("\n");
    }

    public List<String> toList() {

        return output.length() == 0
                ? List.list()
                : List.list(output.toString().split("\n"));
    }

    public void shutdownOutput() {

    }
}
