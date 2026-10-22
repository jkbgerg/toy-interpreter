package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyExceptions;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String description, IStmt stmt) {
        super(key, description);
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), stmt);
        Repository repo = new Repository(state);
        this.controller = new Controller(repo, true);
    }

    @Override
    public void execute() {
        try {
            controller.allStep();
        } catch (MyExceptions e) {
            System.out.println("Execution Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        }
    }
}