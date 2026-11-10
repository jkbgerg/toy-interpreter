package interpreter.repository;

import interpreter.exceptions.MyExceptions;
import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> prgStates;
    private final String logFilePath;

    public Repository(PrgState initialPrgState,String logFilePath) {
        this.prgStates = new ArrayList<>();
        this.prgStates.add(initialPrgState);
        this.logFilePath=logFilePath;
    }

    @Override
    public PrgState getCrtPrg() {
        return this.prgStates.getFirst();
    }

    public List<PrgState> getPrgList() {
        return this.prgStates;
    }

    public void addPrg(PrgState prg) {
        this.prgStates.add(prg);
    }

    @Override
    public void logPrgStateExec() throws MyExceptions {
        try (var logFile = new PrintWriter(
                new BufferedWriter(new FileWriter(logFilePath, true)))) { // append mode
            logFile.println(getCrtPrg().toLogString());
        } catch (IOException e) {
            throw new RepositoryException("cannot write the log file " + logFilePath + ": " + e.getMessage());
        }
    }
}