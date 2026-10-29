package actor;

import fpinjava.Result;
import inout.Input;
import inout.InputOutput;
import tuple.Tuple;

class ActorReaderWriter extends AbstractActor<String> implements InputOutput {

    private final ActorReader reader;
    private final Actor<String> test;
    long timeout;

    public ActorReaderWriter(String id, Actor<String> test, long timeout) {
        super(id, Type.SERIAL);
        reader = ActorReader.actorReader(id + "-reader", 2000);
        this.test = test;
        this.timeout = timeout;
    }

    @Override
    public void onReceive(String message, Result<Actor<String>> sender) {
        reader.tell(message, sender);
    }

    @Override
    public Result<Tuple<String, Input>> readLine() {
        return reader.readLine();
    }


    @Override
    public void shutdownInput() {
        reader.shutdownInput();
    }

    @Override
    public void print(String s) {
        test.tell(s, Result.success(this));
    }

    @Override
    public void shutdownOutput() {
        test.shutdown();
    }

    static ActorReaderWriter actorReaderWriter(String id, Actor<String> actor, long timeout) {
        return new ActorReaderWriter(id, actor, timeout);
    }
}
