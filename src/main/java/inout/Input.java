package inout;

import fpinjava.Result;
import tuple.Tuple;
import stream.Stream;


public interface Input {

    Result<Tuple<String, Input>> readLine();

    default Result<Tuple<Integer, Input>> readInt(){
        // Ihre Aufgabe

        return null;
    }

    static Result<Integer> readIntMaybe(String s){
        return Result.of(()->Integer.parseInt(s));
    }

    default Stream<String> readLines() {
        return Stream.<String,Input>unfold(this, Input::readLine);
    }

    default Stream<Integer> readInts() {
        return Stream.<Integer,Input>unfold(this, Input::readInt);
    }

    void shutdownInput();

}
