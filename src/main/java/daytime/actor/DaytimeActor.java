package daytime.actor;

import actor.AbstractActor;
import actor.Actor;
import fpinjava.Result;

import java.time.LocalTime;

public class DaytimeActor extends AbstractActor<String> {

    public DaytimeActor(String id) {
        super(id, Type.SERIAL);
    }

    @Override
    public void onReceive(String message, Result<Actor<String>> sender) {
        sender.forEach(actor -> actor.tell(LocalTime.now().toString()));
    }
}
