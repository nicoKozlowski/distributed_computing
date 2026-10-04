package inout;

import tuple.Tuple;
import fpinjava.Result;
import java.io.BufferedReader;

public class AbstractReader implements Input {

    protected final BufferedReader reader;

    protected AbstractReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Result<Tuple<String, Input>> readLine() {

        try {
            String s = reader.readLine();
            return s == null
                    ?  Result.empty()
                    : Result.success(Tuple.tuple(s, this));
        } catch(Exception e) {
            return Result.failure(e);
        }
    }

    @Override
    public void shutdownInput() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            throw new IllegalStateException("failed to close input reader", e);
        }
    }
}
