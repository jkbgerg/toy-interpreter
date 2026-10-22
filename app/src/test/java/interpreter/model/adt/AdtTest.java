package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ADTTest {

    @Test
    void testStackLIFOAndEmptyPop() throws ADTException {
        IStack<String> stack = new MyStack<>();
        assertTrue(stack.isEmpty());

        stack.push("first");
        stack.push("second");
        assertFalse(stack.isEmpty());

        // LIFO order check
        assertEquals("second", stack.pop());
        assertEquals("first", stack.pop());
        assertTrue(stack.isEmpty());

        // Pop on empty stack throws ADTException
        assertThrows(ADTException.class, stack::pop);
    }

    @Test
    void testDictionaryOperations() throws ADTException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        assertFalse(dict.isDefined("x"));

        dict.put("x", 10);
        assertTrue(dict.isDefined("x"));
        assertEquals(10, dict.lookup("x"));

        dict.update("x", 20);
        assertEquals(20, dict.lookup("x"));

        // Lookup or update of a missing key throws ADTException
        assertThrows(ADTException.class, () -> dict.lookup("y"));
        assertThrows(ADTException.class, () -> dict.update("y", 30));
    }

    @Test
    void testListOperations() {
        IList<String> list = new MyList<>();
        assertEquals(0, list.size());

        list.add("apple");
        list.add("banana");
        assertEquals(2, list.size());
        assertEquals("apple", list.get(0));
        assertEquals("banana", list.get(1));
    }
}