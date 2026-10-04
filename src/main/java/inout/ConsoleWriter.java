package inout;

import java.io.PrintWriter;

public class ConsoleWriter extends AbstractWriter {

    public ConsoleWriter(PrintWriter out) {
        super(out);
    }

    public static ConsoleWriter stdout() {
        return new ConsoleWriter(new PrintWriter(System.out, true));
    }
}
