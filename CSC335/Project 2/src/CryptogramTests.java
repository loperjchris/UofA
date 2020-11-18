import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;

class CryptogramTests {

	@Test
	void testQuoteGetting() {
		String quote = Cryptogram.openFile();
		assertTrue(quote instanceof String);
	}
	
	@Test
	void testIsCommand() {
		Commands command = new Commands();
		assertTrue(command.isCommand("REPLACE a BY b"));
		assertTrue(command.isCommand("FREQ"));
		assertFalse(command.isCommand("not a command"));
	}
	
	@Test
	void testCommandProcessing() {
		Commands command = new Commands();
		TextManipulator cypher = new TextManipulator("Hello");
		command.processCommand("A = B", cypher);
		assertEquals((Character) 'B', cypher.getUserMap().get('A'));
		command.processCommand("REPLACE C BY D", cypher);
		assertEquals((Character) 'D', cypher.getUserMap().get('C'));
		assertEquals((Character) ' ', cypher.getUserMap().get('B'));
		command.processCommand("FREQ", cypher);
		command.processCommand("HINT", cypher);
		command.processCommand("HELP", cypher);
		command.processCommand(". = B", cypher);
		assertNotEquals((Character) 'B', cypher.getUserMap().get('.'));
		assertNotEquals((Character) ' ', cypher.getLetterMap().get('A'));
	}
	
	@Test
	void checkAnswerIsFalse() {
		Commands command = new Commands();
		TextManipulator cypher = new TextManipulator("Testing");
		assertFalse(cypher.checkAnswer("Test"));
		assertEquals("       ", cypher.getUserString());
	}
	
	@Test
	void checkAnswerIsTrue() {
		Commands command = new Commands();
		TextManipulator cypher = new TextManipulator("Testing!");
		assertFalse(cypher.checkAnswer("Testing!"));
		assertEquals("       !", cypher.getUserString());
	}
	
	@Test
	void testingPrintCryptsFunctionality() {
		TextManipulator cypher = new TextManipulator("Testing!");
		cypher.printCrypts();
	}
	
	@Test
	void testQuoteLengthExceeds80Characters() {
		Commands command = new Commands();
		TextManipulator cypher = new TextManipulator("Testing a quote length "
				+ "that is going to be greater than eighty characters long so"
				+ " I need a little more.");
	}

}
