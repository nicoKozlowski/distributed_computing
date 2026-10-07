package echo.runnable;

import inout.ConsoleReader;
import inout.ConsoleWriter;

public class Main {
    public static void main(String[] args) {

        ConsoleReader reader = ConsoleReader.stdin();
        ConsoleWriter writer = ConsoleWriter.stdout();

        Runnable echoProgram = Input2Output.input2output(reader, writer);

        echoProgram.run();
    }
}