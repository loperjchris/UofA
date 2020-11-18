import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;


class ArrayMapTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	void testPuttingIntoMap() {
		ArrayMap<Integer, String> test = new ArrayMap<Integer, String>();
		test.put(0, "zero");
		test.put(1, "one");
		test.put(2, "two");
		assertEquals(3, test.size());
		assertEquals("zero", test.get(0));
		assertEquals("one", test.get(1));
		assertEquals("two", test.get(2));
		test.put(1, "two");
		assertNotEquals("one", test.get(1));
		assertEquals("two", test.get(1));
	}
	
	@Test
	void testNextWhenNoNextExists() {
		ArrayMap<Integer, Integer> test = new ArrayMap<Integer, Integer>();
		Set<Entry<Integer, Integer>> entry = test.entrySet();
		Iterator<Entry<Integer, Integer>> itr = entry.iterator();
		test.put(1, 1);
		test.put(2, 2);
		test.put(3, 3);
		itr.next();
		itr.next();
		itr.next();
		thrown.expect(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class, () -> itr.next());
	}
	
	@Test
	void testIterator() {
		ArrayMap<String, Character> test = new ArrayMap<String, Character>();
		Set<Entry<String, Character>> entry = test.entrySet();
		Iterator<Entry<String, Character>> itr = entry.iterator();
		test.put("A", 'a');
		test.put("B", 'b');
		assertEquals(2, entry.size());
		itr.next();
		itr.remove();
		assertEquals(1, entry.size());
		itr.next();
		itr.remove();
		assertEquals(0, entry.size());
		test.put("new", 'h');
		test.put("entry", 'h');
		itr.next();
		itr.remove();
		thrown.expect(IllegalStateException.class);
		assertThrows(IllegalStateException.class, () -> itr.remove());
	}
	
	@Test
	void testContains() {
		ArrayMap<String, Character> test = new ArrayMap<String, Character>();
		Set<Entry<String, Character>> entry = test.entrySet();
		Entry<String, Character> entry2 = new AbstractMap.SimpleEntry<String, Character>("test", 't');
		Entry<String, Character> entry3 = new AbstractMap.SimpleEntry<String, Character>("not there", 'n');
		test.put("test", 't');
		test.put("string", 's');
		test.put("number", 'n');
		assertTrue(entry.contains(entry2));
		assertFalse(entry.contains(entry3));
	}
	
	@Test
	void testIncreaseSize() {
		ArrayMap<Integer, Integer> test = new ArrayMap<Integer, Integer>();
		Set<Entry<Integer, Integer>> entry = test.entrySet();
		Iterator<Entry<Integer, Integer>> itr = entry.iterator();
		test.put(1, 1);
		assertTrue(itr.hasNext());
		itr.next();
		assertFalse(itr.hasNext());
		test.put(2, 2);
		test.put(3, 3);
		test.put(4, 4);
		test.put(5, 5);
		test.put(6, 6);
		test.put(7, 7);
		test.put(8, 8);
		test.put(9, 9);
		test.put(10, 10);
		assertEquals(10, test.size());
		Integer i = 2;
		while (itr.hasNext()) {
			Entry<Integer, Integer> next = itr.next();
			assertEquals(next.getValue(), i);
			i++;
		}
		assertFalse(itr.hasNext());
		test.put(11,11);
		assertEquals(11, test.size());
		assertEquals((Integer) 7, test.get(7));
	}

}
