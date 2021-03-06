/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import reasonfx.rule.Binding;
import reasonfx.rule.Given;
import reasonfx.rule.RuleInstance;
import reasonfx.rule.UnificationException;
import reasonfx.rule.Wanted;
import reasonfx.util.ReasonLogger;

/**
 *
 * @author holzensp
 */
public class RuleInstanceVariable
        extends SimpleObjectProperty<Binding>
        implements UnificationVariable<RuleInstanceVariable> {

    public static final ReasonLogger LOGGER = new ReasonLogger(RuleInstanceVariable.class);

    private static int UniqueID = 0;
    
    private final RuleVariable     bluePrint;
    private final int              varID;

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

    public void    ununify()    { LOGGER.info("UNbinding {0}", this); this.set(null); }
    public boolean isUnified()  { return null != this.get(); }
    public Term    getBinding() { return this.get().value; }
    
    @Override public StringExpression asStringExpression(int prec) {
        //TODO: FIX THIS!
        return Bindings.createStringBinding(() -> {
            if(isUnified())
                return this.get().value.asStringExpression(prec).get();
            else
                return UnificationVariable.mkString(prettyID);
        }, this);
    }

    @Override
    public void unifyImpl(Given unifier, Term wanted) throws UnificationException {
        if(!isUnified()) {
            LOGGER.info("Binding {0} to {1}", this.dbgString(), wanted.dbgString());
            this.set(new Binding(wanted, unifier));
            LOGGER.info("registering RuleInstanceVariable dependency");
            unifier.addListener(RuleInstanceVariable.class, new ChangeListener<Wanted>() {
                @Override
                public void changed(ObservableValue<? extends Wanted> prop,
                        Wanted oldV, Wanted newV) {
                    LOGGER.info("releasing RuleInstanceVariable dependency {0}", RuleInstanceVariable.this.dbgString());
                    //We only ever want to listen to the value being *unset*
                    assert(oldV == wanted && newV == null);
                    //We no longer depend on this unifier
                    prop.removeListener(this);
                    RuleInstanceVariable.this.ununify();
                }
            });
        } else {
            this.get().value.unify(unifier, wanted);
            LOGGER.info("Registering Given dependency");
            this.get().origin.addListener(Given.class, new ChangeListener<Wanted>() {
                @Override
                public void changed(ObservableValue<? extends Wanted> observable, Wanted oldValue, Wanted newValue) {
                    LOGGER.info("Releasing Given dependency for {0}", unifier);
                    observable.removeListener(this);
                    LOGGER.info("Calling reUnify for {0}", unifier);
                    unifier.reUnify();
                }
            });
        }
    }
    
}
