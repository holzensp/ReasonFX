/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import java.util.Map;
import java.util.TreeMap;
import reasonfx.rule.Given;
import reasonfx.rule.UnificationException;

/**
 *
 * @author holzensp
 */
public class ElemProp extends ConcreteVariable<ElemProp> {
    private static final Map<String,ElemProp> eps = new TreeMap();
    
    public static ElemProp getInstance(String propName) {
        if(!eps.containsKey(propName)) {
            eps.put(propName, new ElemProp(propName));
        }
        
        return eps.get(propName);
    }
    
    private ElemProp(String propName) { super(propName); }

    @Override
    public void unifyImpl(Given unifier, Term wanted)
            throws UnificationException {
        if(ElemProp.class.isAssignableFrom(wanted.getClass()) &&
                ((ElemProp) wanted).getID() == this.getID()) return;
        else if(UnificationVariable.class.isAssignableFrom(wanted.getClass()))
            ((UnificationVariable) wanted).unifyImpl(unifier, this);
        else
            throw new UnificationException(this,wanted);
    }

//    @Override public String toString() { return dbgString(); }
}
