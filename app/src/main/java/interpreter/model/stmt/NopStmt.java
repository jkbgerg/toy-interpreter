package interpreter.model.stmt;

import interpreter.exceptions.MyExceptions;
import interpreter.model.PrgState;

public record NopStmt() implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        return state;
    }

    @Override
    public String toString() {
        return "nop";
    }
}