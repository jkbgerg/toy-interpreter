package interpreter.controller;

import interpreter.exceptions.MyExceptions;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private final IRepository repo;
    private boolean displayFlag = false;

    public Controller(IRepository repo) {
        this.repo = repo;
    }

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public void setDisplayFlag(boolean value) {
        this.displayFlag = value;
    }

    public PrgState oneStep(PrgState state) throws MyExceptions {
        var stack = state.getExeStack();
        if (stack.isEmpty()) {
            throw new StatementException("program state stack is empty");
        }
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyExceptions {
        PrgState prg = repo.getCrtPrg();
        display(prg);

        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            display(prg);

        }
    }

    private void display(PrgState state) {
        if (displayFlag) {
            System.out.println(state);
        }
    }
}