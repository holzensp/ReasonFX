/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author holzensp
 */
public class Rule extends EntailmentBase {
    private final int varCnt;

    private Rule(int v, Term c, Collection<Term> ps) {
        super(c,ps);
        varCnt = v;
    }
    
    public Rule(Entailment ent) {
        super(ent.getConclusion(), ent.getPremisses());
        varCnt = (int) Stream.concat(
                getConclusion().collect(RuleVariable.class),
                getPremisses().stream().flatMap(t -> t.collect(RuleVariable.class))
            ).count();
    }
    
    public Rule(Rule that) {
        super(that.getConclusion(), that.getPremisses());
        this.varCnt = that.varCnt;
    }
    
    public static Rule abstractFrom(Entailment ent) {
        final Map<ConcreteVariable,RuleVariable> lut = new TreeMap();
        Function<ConcreteVariable,RuleVariable> f = c -> {
            if(!lut.containsKey(c)) {
                lut.put(c, new RuleVariable(lut.size()));
            }
            return lut.get(c);
        };

        return new Rule(lut.size(),
                ent.getConclusion().copyWith(f, ConcreteVariable.class),
                ent.getPremisses().stream().map(t -> t.copyWith(f,ConcreteVariable.class)).collect(Collectors.toList())
            );
    }
}
