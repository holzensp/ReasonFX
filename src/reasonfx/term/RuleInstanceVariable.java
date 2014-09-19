/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import reasonfx.rule.Binding;
import reasonfx.rule.Given;
import reasonfx.rule.RuleInstance;
import reasonfx.rule.UnificationException;
import reasonfx.rule.Wanted;

/**
 *
 * @author holzensp
 */
public class RuleInstanceVariable
        extends SimpleObjectProperty<Binding>
        implements UnificationVariable<RuleInstanceVariable> {

    private static int UniqueID = 0;
    
    private final RuleVariable bluePrint;
    private final int          varID;

    private int prettyID = -1;
    private RuleInstance parent;
    
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

    public void    ununify()    { this.set(null); System.out.println("UNbinding " + this.toString()); }
    public boolean isUnified()  { return null != this.get(); }
    public Term    getBinding() { return this.get().value; }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        if(debugging) {
            if(isUnified()) result.append('(');
            result
                .append(UnificationVariable.mkString(prettyID))
                .append('[').append(String.valueOf(getID())).append(']');
            if(isUnified()) {
                result.append(" => ");
                this.get().value.prettyPrint(result, -1, debugging);
                result.append(')');
            }
        } else {
            if(isUnified())
                this.get().value.prettyPrint(result,prec,debugging);
            else {
                result.append(UnificationVariable.mkString(prettyID));
            }
        }
    }

    @Override
    public void unify(Given unifier, Term wanted) throws UnificationException {
        if(!isUnified()) {
            this.set(new Binding(wanted, unifier));
            System.out.println("registering RuleInstanceVariable dependency");
            unifier.addListener(RuleInstanceVariable.class, new ChangeListener<Wanted>() {
                @Override
                public void changed(ObservableValue<? extends Wanted> prop,
                        Wanted oldV, Wanted newV) {
                    System.out.println("releasing RuleInstanceVariable dependency " + RuleInstanceVariable.this.dbgString());
                    //We only ever want to listen to the value being *unset*
                    assert(oldV == wanted && newV == null);
                    //We no longer depend on this unifier
                    prop.removeListener(this);
                    RuleInstanceVariable.this.ununify();
                }
            });
            System.out.println("Binding " + this.dbgString() + " to " + wanted.dbgString());
        } else {
            this.get().value.unify(unifier, wanted);
            System.out.println("!!!!!!!!!!!!!!!registering Given dependency");
            unifier.addListener(Given.class, new ChangeListener<Wanted>() {

                @Override
                public void changed(ObservableValue<? extends Wanted> observable, Wanted oldValue, Wanted newValue) {
                    System.out.println("releasing Given dependency");
                    observable.removeListener(this);
                }
            });
            this.get().origin.register(unifier);
            this.get().value.unify(unifier, wanted);
        }
    }
    
    @Override public String toString() { return dbgString(); }
}
