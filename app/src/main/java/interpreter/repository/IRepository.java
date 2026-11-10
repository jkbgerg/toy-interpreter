package interpreter.repository;

import interpreter.exceptions.MyExceptions;
import interpreter.model.PrgState;

public interface IRepository {
    PrgState getCrtPrg();
    void logPrgStateExec() throws MyExceptions;
}