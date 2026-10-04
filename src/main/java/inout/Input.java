package inout;

import fpinjava.Result;
import tuple.Tuple;
import stream.Stream;


public interface Input {

    Result<Tuple<String, Input>> readLine();

    default Result<Tuple<Integer, Input>> readInt(){
        // Ihre Aufgabe

        return readLine().flatMap(tuple1 ->
                readIntMaybe(tuple1.fst).map(num -> Tuple.tuple(num, tuple1.snd)));
    }

    static Result<Integer> readIntMaybe(String s){
        return Result.of(()->Integer.parseInt(s));
    }

    default Stream<String> readLines() {
        return Stream.unfold(this, Input::readLine);
    }

    default Stream<Integer> readInts() {
        return Stream.unfold(this, Input::readInt);
    }

    void shutdownInput();

}
