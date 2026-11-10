package interpreter.model.stmt;

import interpreter.exceptions.MyExceptions;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;


public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyExceptions{
        if(!(exp.eval(state.getSymTable()) instanceof StringValue filename)) {
            throw new StatementException("ReadFile: " + exp + " is not a string");
        }
        if(! state.getSymTable().isDefined(varName)) {
            throw new StatementException("This variable name doesnt exist: "+varName);
        }

        if (!(state.getSymTable().lookup(varName) instanceof IntValue)) {
            throw new StatementException("Variable " + varName + " is not an integer");
        }

        var fileTable=state.getFileTable();
        if (! fileTable.isDefined(filename)){
            throw new StatementException("readFile "+ filename+ " is not exist");
        }
        try {
            BufferedReader reader=fileTable.lookup(filename);
            String line;
            line=reader.readLine();
            if (line==null){
                int num=0;
                IntValue num2 = new IntValue(num);
                state.getSymTable().update(varName, num2);
            }
            else {
                line = line.strip();
                int num = Integer.parseInt(line);
                IntValue num2 = new IntValue(num);
                state.getSymTable().update(varName, num2);
            }


        }
        catch (IOException e){
            throw new StatementException("ReadFile cannot read "+filename);
        }
        catch (NumberFormatException e){
            throw new StatementException("The data in the file is not a valid integer number!");
        }

        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}

