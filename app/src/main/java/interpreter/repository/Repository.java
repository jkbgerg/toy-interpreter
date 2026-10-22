package interpreter.repository;

import interpreter.model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> prgStates;

    public Repository(PrgState initialPrgState) {
        this.prgStates = new ArrayList<>();
        this.prgStates.add(initialPrgState);
    }

    @Override
    public PrgState getCrtPrg() {
        return this.prgStates.get(0);
    }

    public List<PrgState> getPrgList() {
        return this.prgStates;
    }

    public void addPrg(PrgState prg) {
        this.prgStates.add(prg);
    }
}