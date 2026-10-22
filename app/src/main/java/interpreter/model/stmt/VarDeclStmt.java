package interpreter.model.stmt;

import interpreter.exceptions.MyExceptions;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.type.IType;

public record VarDeclStmt(String name, IType type) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        var symTable = state.getSymTable();

        if (symTable.isDefined(name)) {
            throw new StatementException("Variable " + name + " is already declared.");
        }

        symTable.put(name, type.defaultValue());
        return state;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }
}