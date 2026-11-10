package interpreter.model.stmt;

import interpreter.exceptions.MyExceptions;
import interpreter.model.PrgState;


public interface IStmt {
    PrgState execute(PrgState state) throws MyExceptions;
}