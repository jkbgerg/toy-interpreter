package interpreter.model.stmt;

import interpreter.exceptions.MyExceptions;
import interpreter.model.PrgState;


///TODO: VarDeclStmt(String name, IType type)  – S3
/// PrintStmt(IExp exp) – S4
///IfStmt(IExp cond, IStmt thenS, IStmt elseS)– S5
///  NopStmt() – S6

public interface IStmt {
    PrgState execute(PrgState state) throws MyExceptions;
}