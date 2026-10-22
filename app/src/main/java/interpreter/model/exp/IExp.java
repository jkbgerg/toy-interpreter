package interpreter.model.exp;

import interpreter.exceptions.MyExceptions;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;


//IExp, ValueExp, VarExp, ArithExp, LogicExp

public interface IExp{
    IValue eval(IDictionary<String, IValue> symTable) throws MyExceptions;
}
