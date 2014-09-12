/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author holzensp
 */
public class RuleInstanceVariable extends RuleVariable {
    private final IntegerProperty prettyID = new SimpleIntegerProperty();
    private final ObjectProperty<Term> binding = new SimpleObjectProperty();
    
    private final Set<ProofStep> deps = new HashSet();

    public RuleInstanceVariable(RuleVariable v) {
        super(v);
        prettyID.set(getID());
    }

    public void setPrettyID(int id) { prettyID.set(id); }
    public void unsetPrettyID() { prettyID.set(getID()); }

    public void register(ProofStep dep) { deps.add(dep); }
    
    public void unbind() {
        binding.set(null);
        deps.stream().forEach((s) -> {
            s.notifyUnbinding(this);
        });
    }
    
    public void bind(Term t, ProofStep dep) throws DoubleBindingException {
        if(isBound()) {
            throw new DoubleBindingException(this,binding.get(),t,dep);
        } else {
            assert(deps.isEmpty());
            binding.set(Objects.requireNonNull(t, "RuleInstanceVariables may not be bound to null"));
        }
    }
    
    public boolean isBound() { return null != binding.get(); }
    
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        if(isBound())
            binding.get().prettyPrint(result,prec,debugging);
        else {
            result.append(mkString(prettyID.get()));
            if(debugging)
                result.append(String.format("[%d]", prettyID.get()));
        }
    }

    @Override
    public void unify(reasonfx.rule.Unifier unifier, Term wanted) throws UnificationException {
        if(!isBound()) {
            if(unifier.containsKey(this)) {
                unifier.get(this).unify(unifier, wanted);
            } else {
                unifier.put(this, wanted);
            }
        } else {
            binding.get().unify(unifier, wanted);
        }
    }
    
    @Override public String toString() { return dbgString(); }
    @Override public Term copyWithChildren(Collection<Term> chlds) { return new RuleInstanceVariable(this); }
}
