package inout;

import java.io.PrintWriter;

public class AbstractWriter implements Output {

    PrintWriter writer;

    public AbstractWriter(PrintWriter out) {
        this.writer = out;
    }

    public void print(String s) {
        writer.print(s);
        writer.flush();
    }

    @Override
    public void printLine(String s) {
        writer.println(s);
        writer.flush();
    }

    @Override
    public void shutdownOutput() {
        writer.flush();
        writer.close();
    }
}
