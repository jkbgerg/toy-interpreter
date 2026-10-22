package interpreter.model.stmt;

import interpreter.exceptions.MyExceptions;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatementTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
    }

    @Test
    void testNopStmt() throws MyExceptions {
        IStmt nop = new NopStmt();
        PrgState res = nop.execute(state);
        assertEquals(state, res);
    }

    @Test
    void testVarDeclAndRedeclaredError() throws MyExceptions {
        IStmt decl = new VarDeclStmt("v", new IntType());
        decl.execute(state);
        assertTrue(state.getSymTable().isDefined("v"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));

        // Redeclared variable error
        assertThrows(StatementException.class, () -> decl.execute(state));
    }

    @Test
    void testAssignStmtAndErrors() throws MyExceptions {
        IStmt decl = new VarDeclStmt("v", new IntType());
        decl.execute(state);

        // Successful assignment
        IStmt assign = new AssignStmt("v", new ValueExp(new IntValue(42)));
        assign.execute(state);
        assertEquals(new IntValue(42), state.getSymTable().lookup("v"));

        // Assignment to undeclared variable
        IStmt undeclaredAssign = new AssignStmt("u", new ValueExp(new IntValue(10)));
        assertThrows(StatementException.class, () -> undeclaredAssign.execute(state));

        // Type mismatch assignment
        IStmt typeMismatch = new AssignStmt("v", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> typeMismatch.execute(state));
    }

    @Test
    void testPrintStmt() throws MyExceptions {
        IStmt print = new PrintStmt(new ValueExp(new IntValue(100)));
        print.execute(state);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(100), state.getOut().get(0));
    }


    @Test
    void testIfStmtAndErrors() throws MyExceptions {
        IStmt ifStmt = new IfStmt(
                new ValueExp(new BoolValue(true)),
                new VarDeclStmt("x", new IntType()),
                new VarDeclStmt("y", new IntType())
        );

        ifStmt.execute(state);
        // Since condition is true, thenS should be on the stack
        assertTrue(state.getExeStack().pop() instanceof VarDeclStmt);

        // Non-boolean if condition error
        IStmt badIf = new IfStmt(
                new ValueExp(new IntValue(1)),
                new NopStmt(),
                new NopStmt()
        );
        assertThrows(StatementException.class, () -> badIf.execute(state));
    }
}