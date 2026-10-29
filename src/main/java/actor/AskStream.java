package actor;

import fpinjava.Result;
import inout.Input;
import stream.Stream;

public class AskStream {

    public static Stream<String> ask(Actor<String> actor, String message, long timeout) {
        ActorReaderWriter io = ActorReaderWriter.actorReaderWriter("ask", actor, timeout);
        actor.tell(message, Result.success(io));

        return Stream.unfold(io, Input::readLine);
    }
}
