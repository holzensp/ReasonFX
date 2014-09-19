/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author holzensp
 * @param <T>
 */
public abstract class ConcreteVariable<T extends ConcreteVariable> implements Variable<T> {
    private static int UniqueID = 0;
    private final ReadOnlyStringWrapper varName;
    private final int varID;
    
    public ConcreteVariable(String name) {
        varID = UniqueID++;
        varName = new ReadOnlyStringWrapper(name);
    }
    
    @Override public int getID() { return varID; }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        result.append(varName.get());
        if(debugging)
            result.append('(').append(String.valueOf(getID())).append(')');
    }
}
