/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author holzensp
 */
public class RuleInstanceVariable
        extends SimpleObjectProperty<Term>
        implements UnificationVariable<RuleInstanceVariable> {

    private static int UniqueID = 0;
    
    private final RuleVariable bluePrint;
    private final int          varID;

    private int prettyID = -1;
    private RuleInstance parent;
    private class Binding {
        public final Term  value;
        public final Given origin;
        public Binding(Term v, Given o) { value = v; origin = o; }
    }
    private Binding binding = null;
    
    public RuleInstanceVariable(RuleVariable v) {
        varID     = UniqueID++;
        bluePrint = v;
        prettyID  = v.getRuleLocalID();
    }
    
    protected void setParent(RuleInstance i) {
        if(parent != null)
            throw new NullPointerException("Parent set when it WASN'T null");
        parent = i;
    }
    
    @Override public int getID() { return varID; }

    public void setPrettyID(int id) { prettyID = id; }
    public void unsetPrettyID() { prettyID = bluePrint.getRuleLocalID(); }

    public void    ununify()    { binding = null; System.out.println("UNbinding " + this.toString()); }
    public boolean isUnified()  { return null != binding; }
    public Term    getBinding() { return binding.value; }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        if(debugging) {
            if(isUnified()) result.append('(');
            result
                .append(UnificationVariable.mkString(prettyID))
                .append('[').append(String.valueOf(getID())).append(']');
            if(isUnified()) {
                result.append(" => ");
                binding.value.prettyPrint(result, -1, debugging);
                result.append(')');
            }
        } else {
            if(isUnified())
                binding.value.prettyPrint(result,prec,debugging);
            else {
                result.append(UnificationVariable.mkString(prettyID));
            }
        }
    }

    @Override
    public void unify(Given unifier, Term wanted) throws UnificationException {
        if(!isUnified()) {
            this.set(wanted);
            unifier.addListener(new ChangeListener<Wanted>() {
                @Override
                public void changed(ObservableValue<? extends Wanted> prop,
                        Wanted oldV, Wanted newV) {
                    //We only ever want to listen to the value being *unset*
                    assert(oldV != null && newV == null);
                    //We no longer depend on this unifier
                    prop.removeListener(this);
                    RuleInstanceVariable.this.ununify();
                }
            });
            //unifier.register(this);
            binding = new Binding(wanted, unifier);
            System.out.println("Binding " + this.dbgString() + " to " + wanted.dbgString());
        } else {
            this.get().unify(unifier, wanted);
            //unifier.addListener((Observable observable) -> {
            //    
            //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            //});
            binding.origin.register(unifier);
            binding.value.unify(unifier, wanted);
        }
    }
    
    @Override public String toString() { return dbgString(); }
}
