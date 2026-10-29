package inout;

public interface InputOutput extends Input,Output {
    default void close() {
        shutdownInput();
        shutdownOutput();
    }
}