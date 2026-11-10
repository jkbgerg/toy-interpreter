package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyExceptions;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.type.StringType;
import interpreter.model.value.StringValue;
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

    @Test
    void testRelationalExpressions() throws MyExceptions{
        IExp expEq=new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(5)),new ValueExp(new IntValue(5)) );
        assertEquals(new BoolValue(true),expEq.eval(symTable));

        IExp expGe=new RelationalExp(RelOp.GE, new ValueExp(new IntValue(5)),new ValueExp(new IntValue(5)) );
        assertEquals(new BoolValue(true),expGe.eval(symTable));

        IExp expG=new RelationalExp(RelOp.GT, new ValueExp(new IntValue(5)),new ValueExp(new IntValue(2)) );
        assertEquals(new BoolValue(true),expG.eval(symTable));

        IExp expLe=new RelationalExp(RelOp.LE, new ValueExp(new IntValue(5)),new ValueExp(new IntValue(5)) );
        assertEquals(new BoolValue(true),expLe.eval(symTable));

        IExp expL=new RelationalExp(RelOp.LT, new ValueExp(new IntValue(5)),new ValueExp(new IntValue(10)) );
        assertEquals(new BoolValue(true),expL.eval(symTable));

        IExp expNe=new RelationalExp(RelOp.NE, new ValueExp(new IntValue(1)),new ValueExp(new IntValue(5)) );
        assertEquals(new BoolValue(true),expNe.eval(symTable));

        IExp expNotInt=new RelationalExp(RelOp.EQ, new ValueExp(new StringValue("s")),new ValueExp(new IntValue(5)) );

        var ex = assertThrows(ExpressionException.class, () -> expNotInt.eval(symTable));
        assertEquals("first operand is not an integer", ex.getMessage());
    }
}