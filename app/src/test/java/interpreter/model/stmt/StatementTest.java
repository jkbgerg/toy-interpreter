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
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StatementTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),new MyDictionary<>() ,new NopStmt());
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


class FileStmtTest {

    @TempDir
    Path dir; // A new empty folder for every test

    private PrgState state;
    private ValueExp file; // The expression "<dir>/numbers.in"

    @BeforeEach
    void setUp() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n");

        file = new ValueExp(new StringValue(f.toString()));

        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        if (state != null && state.getFileTable() != null) {
            for (var reader : state.getFileTable().toMap().values()) {
                try {
                    reader.close();
                } catch (Exception ignored) {}
            }
        }
    }

    @Test
    void readGivesTheNextNumberThenZeroAtTheEnd() throws MyExceptions {
        new OpenRFileStmt(file).execute(state);

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(
                new IntValue(7),
                state.getSymTable().lookup("x")
        );

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(
                new IntValue(0),
                state.getSymTable().lookup("x")
        );
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyExceptions {
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class, () ->
                new OpenRFileStmt(file).execute(state)
        );
    }

    // --- ADDITIONAL OPEN R FILE TESTS ---

    @Test
    void openRFileAddsToFileTableAndSucceeds() throws MyExceptions {
        new OpenRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void openingNonExistentFileThrows() {
        ValueExp missingFile = new ValueExp(new StringValue(dir.resolve("nonexistent.in").toString()));
        assertThrows(StatementException.class, () ->
                new OpenRFileStmt(missingFile).execute(state)
        );
    }

    @Test
    void openingNonStringExpressionThrows() {
        ValueExp intExpr = new ValueExp(new IntValue(123));
        assertThrows(StatementException.class, () ->
                new OpenRFileStmt(intExpr).execute(state)
        );
    }

    // --- ADDITIONAL READ FILE TESTS ---

    @Test
    void readingUndeclaredVariableThrows() throws MyExceptions {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () ->
                new ReadFileStmt(file, "undeclaredVar").execute(state)
        );
    }

    @Test
    void readingIntoNonIntVariableThrows() throws MyExceptions {
        // Deklarálunk egy String típusú 's' változót a teszthez
        new VarDeclStmt("s", new interpreter.model.type.StringType()).execute(state);

        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () ->
                new ReadFileStmt(file, "s").execute(state)
        );
    }

    @Test
    void readingUnopenedFileThrows() {
        assertThrows(StatementException.class, () ->
                new ReadFileStmt(file, "x").execute(state)
        );
    }

    @Test
    void readingNonIntegerLineThrows() throws Exception {
        Path invalidFile = dir.resolve("invalid.in");
        Files.writeString(invalidFile, "notAnInt\n");
        ValueExp invalidExpr = new ValueExp(new StringValue(invalidFile.toString()));

        new OpenRFileStmt(invalidExpr).execute(state);
        assertThrows(StatementException.class, () ->
                new ReadFileStmt(invalidExpr, "x").execute(state)
        );
    }

    // --- CLOSE R FILE TESTS ---

    @Test
    void closeRFileRemovesFileFromFileTable() throws MyExceptions {
        new OpenRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().toMap().isEmpty());

        new CloseRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void closingUnopenedFileThrows() {
        assertThrows(StatementException.class, () ->
                new CloseRFileStmt(file).execute(state)
        );
    }
}