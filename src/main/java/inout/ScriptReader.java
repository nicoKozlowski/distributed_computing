package inout;

import fpinjava.Result;
import list.List;
import tuple.Tuple;

public class ScriptReader implements Input {

    private final List<String> lines;

    public ScriptReader(List<String> lines) {
        this.lines = lines;
    }

    public ScriptReader(String... lines) {
        this.lines = List.list(String.join("\n", lines).split("\n"));
    }

    public Result<Tuple<String, Input>> readLine() {

        return lines.isEmpty()
                ? Result.failure("Not enough entries in Script")
                : Result.success(Tuple.tuple(lines.headOption().getOrElse(""),
                                                new ScriptReader(lines.drop(1))));
    }

    @Override
    public Result<Tuple<Integer, Input>> readInt() {
        if (lines.isEmpty()) {
            return Result.failure("Not enough entries in Script");
        } else {
            try {
                return Result.success(Tuple.tuple(Integer.parseInt(lines.headOption().getOrElse("")), new ScriptReader(lines.drop(1))));
            } catch (Exception e) {
                return Result.failure(e);
            }
        }
    }

    public List<String> toList() {
        return this.lines;
    }

    public void shutdownInput() {

    }
}
