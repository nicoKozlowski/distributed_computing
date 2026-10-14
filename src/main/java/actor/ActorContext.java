package actor;

public interface ActorContext<T> {

    void become(MessageProcessor<T> behavior);

    MessageProcessor<T> getBehavior();
}
