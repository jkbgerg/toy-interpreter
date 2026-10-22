package interpreter.controller;

import interpreter.exceptions.MyExceptions;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private static PrgState run(IStmt program) throws MyExceptions {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
        Controller controller = new Controller(new Repository(state), false); // display off for tests
        controller.allStep();
        return state;
    }

    @Test
    void testOneStepOnEmptyStackThrows() {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        try {
            state.getExeStack().pop(); // Clear initial statement
        } catch (Exception ignored) {}

        Controller controller = new Controller(new Repository(state), false);
        assertThrows(MyExceptions.class, () -> controller.oneStep(state));
    }

    @Test
    void example2PrintsEighteen() throws MyExceptions {
        // Example program: int a; a=2+3*5; print(a)  -> output should be 17 or 18 depending on expression,
        // let's construct the statement for EX2: int v; v=2; print(v) or arithmetic example:
        // int a; int b; a=2+3*5; b=a+1; print(b) -> 18
        IStmt ex2 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)), new ArithExp(ArithOp.MUL, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(
                                        new AssignStmt("b", new ArithExp(ArithOp.ADD, new VarExp("a"), new ValueExp(new IntValue(1)))),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );

        PrgState state = run(ex2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }
}