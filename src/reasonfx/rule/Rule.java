/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import reasonfx.term.ConcreteVariable;
import reasonfx.term.RuleVariable;
import reasonfx.term.Term;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author holzensp
 */
public class Rule extends EntailmentBase {

    private Rule(Term c, Collection<Term> ps) { super(c,ps); }
    public Rule(Entailment ent) { super(ent.getConclusion(), ent.getPremisses()); }
    
    public static Rule abstractFrom(Entailment ent) {
        final Map<ConcreteVariable,RuleVariable> lut = new TreeMap();
        Function<ConcreteVariable,RuleVariable> f = c -> {
            if(!lut.containsKey(c)) {
                lut.put(c, new RuleVariable(lut.size()));
            }
            return lut.get(c);
        };

        return new Rule(
                ent.getConclusion().copyWith(f, ConcreteVariable.class),
                ent.getPremisses().stream().map(t -> t.copyWith(f,ConcreteVariable.class)).collect(Collectors.toList())
            );
    }
}
