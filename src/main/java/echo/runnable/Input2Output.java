package echo.runnable;

import inout.Input;
import inout.Output;
import tuple.Tuple;
import fpinjava.Result;

public class Input2Output implements Input, Output {

    private Input in;
    private Output out;

    public Input2Output(Input in, Output out) {
        this.in = in;
        this.out = out;
    }

    static Runnable input2output(Input in, Output out) {

        return () -> {
            in.readLine().<Runnable>map(tuple -> () -> {
                        out.print(tuple.fst);
                        input2output(tuple.snd, out).run();
                    })
                    .getOrElse(out::shutdownOutput)
                    .run();
        };
    }

    public Result<Tuple<String, Input>> readLine() {
        return this.in.readLine();
    }

    public void shutdownInput() {
        this.in.shutdownInput();
    }

    public void print(String s) {
        this.out.print(s);
    }

    public void shutdownOutput() {
        this.out.shutdownOutput();
    }
}
