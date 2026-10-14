package actor;

import fpinjava.Result;

public interface MessageProcessor<T> {

    void process(T t, Result<Actor<T>> sender);
}
