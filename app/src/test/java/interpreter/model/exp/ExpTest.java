package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyExceptions;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void testArithmeticExpressions() throws MyExceptions {
        IExp add = new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(3)));
        assertEquals(new IntValue(8), add.eval(symTable));

        IExp sub = new ArithExp(ArithOp.SUB, new ValueExp(new IntValue(10)), new ValueExp(new IntValue(4)));
        assertEquals(new IntValue(6), sub.eval(symTable));

        IExp mul = new ArithExp(ArithOp.MUL, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3)));
        assertEquals(new IntValue(6), mul.eval(symTable));

        IExp div = new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(10)), new ValueExp(new IntValue(2)));
        assertEquals(new IntValue(5), div.eval(symTable));
    }

    @Test
    void testDivisionByZero() {
        IExp divZero = new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(0)));
        var ex = assertThrows(ExpressionException.class, () -> divZero.eval(symTable));
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void testNonIntegerOperand() {
        IExp badArith = new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(5)), new ValueExp(new BoolValue(true)));
        assertThrows(ExpressionException.class, () -> badArith.eval(symTable));
    }

    @Test
    void testLogicExpressions() throws MyExceptions {
        IExp andExp = new LogicExp(LogicOp.AND, new ValueExp(new BoolValue(true)), new ValueExp(new BoolValue(false)));
        assertEquals(new BoolValue(false), andExp.eval(symTable));

        IExp orExp = new LogicExp(LogicOp.OR, new ValueExp(new BoolValue(true)), new ValueExp(new BoolValue(false)));
        assertEquals(new BoolValue(true), orExp.eval(symTable));
    }

    @Test
    void testNonBooleanOperand() {
        IExp badLogic = new LogicExp(LogicOp.AND, new ValueExp(new BoolValue(true)), new ValueExp(new IntValue(1)));
        assertThrows(ExpressionException.class, () -> badLogic.eval(symTable));
    }

    @Test
    void testUndefinedVariable() {
        IExp varExp = new VarExp("x");
        var ex = assertThrows(ExpressionException.class, () -> varExp.eval(symTable));
        assertEquals("variable x is not defined", ex.getMessage());
    }
}