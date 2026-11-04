package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {

    @Test
    void StringValueDefaultAndEqualAndHash(){
        StringType type = new StringType();
        IValue defaultVal = type.defaultValue();
        assertEquals(new StringValue(""), defaultVal);

        StringValue val1 = new StringValue("test");
        StringValue val2 = new StringValue("test");
        StringValue val3 = new StringValue("different");

        assertEquals(val1, val2);
        assertEquals(val1.hashCode(), val2.hashCode());
        assertNotEquals(val1, val3);
    }

}