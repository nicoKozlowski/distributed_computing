package inout;

import fpinjava.Result;
import tuple.Tuple;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleReader extends AbstractReader {

    protected ConsoleReader(BufferedReader reader) {
        super(reader);
    }


    public Result<Tuple<String, Input>> readLine(String line) {
        System.out.println(line + " ");
        return readLine();
    }

    public Result<Tuple<Integer, Input>> readInt(String line) {
        System.out.println(line + " ");
        return readInt();
    }

    public static ConsoleReader stdin() {
        return new ConsoleReader(new BufferedReader(
                                 new InputStreamReader(System.in)));
    }
}
