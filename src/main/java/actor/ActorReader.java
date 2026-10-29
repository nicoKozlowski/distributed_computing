package actor;

import fpinjava.Result;
import inout.Input;
import tuple.Tuple;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


class ActorReader extends AbstractActor<String> implements Input {

    private final BlockingQueue<String> messages;
    private final long timeout;

    public ActorReader(String id, long timeout) {
        super(id, Type.SERIAL);
        this.messages = new ArrayBlockingQueue<>(10);
        this.timeout = timeout;
    }

    @Override
    public Result<Tuple<String, Input>> readLine() {
        try {
            String message = messages.poll(timeout, TimeUnit.MILLISECONDS);

            if (message == null) {
                return Result.failure("Timeout");
            }

            return Result.success(Tuple.tuple(message, this));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.failure(e);
        }
    }

    @Override
    public void onReceive(String message, Result<Actor<String>> sender) {
        messages.offer(message);
    }
    
    @Override
    public void shutdownInput() {
        this.shutdown();
    }

    public static ActorReader actorReader(String id, long timeout) {
        return new ActorReader(id, timeout);
    }
}
