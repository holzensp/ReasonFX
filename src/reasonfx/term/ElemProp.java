/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import java.util.Map;
import java.util.TreeMap;
import reasonfx.rule.ConcreteVariable;
import reasonfx.rule.Given;
import reasonfx.rule.Term;
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
    public void unify(Given unifier, Term wanted)
            throws UnificationException {
        if(!
            (  wanted instanceof ElemProp
            && ((ElemProp) wanted).getID() == this.getID()
          ))
            throw new UnificationException(this,wanted);
    }

    @Override public String toString() { return dbgString(); }
}
