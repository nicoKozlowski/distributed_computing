package echo.runnable;

import inout.ScriptReader;
import inout.ScriptWriter;
import list.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public  class Input2OutputTest {

	@ParameterizedTest
	@CsvSource({"'Hallo1\nHallo2'",
							"'HalloHallo\njaja'"})
	public void test(String s) {


		ScriptReader scriptReader = new ScriptReader(s);
		ScriptWriter scriptWriter = new ScriptWriter();

		List<String> originalList = scriptReader.toList();

		Input2Output.input2output(scriptReader, scriptWriter).run();

		assertEquals(originalList,scriptWriter.toList());
	}
}
