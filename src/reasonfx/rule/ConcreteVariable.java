/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import javafx.beans.property.ReadOnlyStringProperty;
import reasonfx.util.StringConstant;

/**
 *
 * @author holzensp
 */
public abstract class ConcreteVariable extends Variable {
    private final ReadOnlyStringProperty varName;
    public ConcreteVariable(String name, int id) {
        super(id);
        varName = new StringConstant(name, this, "varName");
    }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        result.append(varName.get());
        if(debugging)
            result.append('(').append(String.valueOf(getID())).append(')');
    }
}
