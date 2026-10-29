package echo.actor;

import actor.Actor;
import actor.AskStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public  class EchoActorTest {

	@ParameterizedTest
	@CsvSource({"'Hallo'",
							"'HalloHallo'"})
	public void test(String s) throws InterruptedException {

	Actor<String> echo = new EchoActor<String>("echo");

    assertEquals(s, AskStream.ask(echo, s, 1000).head());

	}
}
