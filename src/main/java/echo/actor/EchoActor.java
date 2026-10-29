package echo.actor;

import actor.AbstractActor;
import actor.Actor;
import fpinjava.Result;

public class EchoActor<T> extends AbstractActor<T> {

    public EchoActor(String id) {
        super(id, Type.SERIAL);
    }
    @Override
    public void onReceive(T message, Result<Actor<T>> sender) {
        sender.forEach(actor -> actor.tell(message));
    }
}
