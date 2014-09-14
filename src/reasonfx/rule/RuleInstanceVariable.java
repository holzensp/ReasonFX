/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Collection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author holzensp
 */
public class RuleInstanceVariable extends RuleVariable {
    private final IntegerProperty prettyID = new SimpleIntegerProperty();
    private class Binding {
        public final Term  value;
        public final Given origin;
        public Binding(Term v, Given o) { value = v; origin = o; }
    }
    private Binding binding = null;
    
    public RuleInstanceVariable(RuleVariable v) {
        super(v);
        prettyID.set(getID());
    }

    public void setPrettyID(int id) { prettyID.set(id); }
    public void unsetPrettyID() { prettyID.set(getID()); }

    public void    unbind()     { binding = null; }
    public boolean isBound()    { return null != binding; }
    public Term    getBinding() { return binding.value; }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        if(debugging) {
            if(isBound()) result.append('(');
            result
                .append(mkString(prettyID.get()))
                .append('[').append(String.valueOf(getID())).append(']');
            if(isBound()) {
                result.append(" => ");
                binding.value.prettyPrint(result, -1, debugging);
                result.append(')');
            }
        } else {
            if(isBound())
                binding.value.prettyPrint(result,prec,debugging);
            else {
                result.append(mkString(prettyID.get()));
            }
        }
    }

    @Override
    public void unify(Given unifier, Term wanted) throws UnificationException {
        if(!isBound()) {
            unifier.register(this);
            binding = new Binding(wanted, unifier);
        } else {
            binding.origin.register(unifier);
            binding.value.unify(unifier, wanted);
        }
    }
    
    @Override public String toString() { return dbgString(); }
    @Override public Term copyWithChildren(Collection<Term> chlds) { return new RuleInstanceVariable(this); }
}
