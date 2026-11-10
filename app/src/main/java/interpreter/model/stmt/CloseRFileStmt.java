package interpreter.model.stmt;

import interpreter.exceptions.MyExceptions;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStmt(IExp exp) implements IStmt{
    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        if(!((exp.eval(state.getSymTable())) instanceof StringValue filename)){
            throw new StatementException("CloseFile: "+ exp+ " is not a string");
        }

        var fileTable=state.getFileTable();
        if (! fileTable.isDefined(filename)){
            throw new StatementException("CloseFile "+ filename+ " is not exist");
        }
        try {
            BufferedReader reader = fileTable.lookup(filename);
            reader.close();
            fileTable.remove(filename);

        }catch (IOException e){
            throw new StatementException("CloseRFile is not closing "+ filename);
        }

        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}
