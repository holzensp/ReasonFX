/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Collection;
import java.util.HashMap;
import reasonfx.util.EmptyCollection;

/**
 *
 * @author holzensp
 */
public class ElemProp extends ConcreteVariable {
    private static final HashMap<String,ElemProp> eps = new HashMap();
    private static int idBase = 0;
    
    public static ElemProp getInstance(String propName) {
        if(!eps.containsKey(propName)) {
            eps.put(propName, new ElemProp(propName));
        }
        
        return eps.get(propName);
    }
    
    private ElemProp(String propName) {
        super(propName, idBase++);
    }

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
    @Override public Collection<Term> children() { return new EmptyCollection(); }
    @Override public Term copyWithChildren(Collection<Term> chlds) { return this; }
}
