/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.term;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
import reasonfx.rule.Given;
import reasonfx.rule.UnificationException;

/**
 *
 * @author holzensp
 */
public class Sentence implements Term {
    private final Operator   operator;
    private final List<Term> operands;
    
    private Sentence(List<Term> args, Operator op) {
        operator = op;
        operands = args;
    }
    
    public Sentence(Operator op, Collection<Term> args) throws Exception {
        Objects.requireNonNull(op);
        args.stream().forEach(Objects::requireNonNull);
        if(op.arity != args.size() && (!op.associative || op.arity > args.size())) {
            throw new Exception("Malformed operator expression");
        }

        operator = op;
        operands = new ArrayList();
        args.stream().forEach(op.associative ? this::collect : operands::add);
    }
    
    private void collect(Term t) {
        if  (  Sentence.class.isAssignableFrom(t.getClass())
            && ((Sentence) t).operator == operator
            )
        {
            children().stream().forEach(this::collect);
        } else {
            operands.add(t);
        }
    }
    
    @Override
    public void unifyImpl(Given unifier, Term wanted) throws UnificationException {
        UnificationException e = new UnificationException(this, wanted);
        if(!(wanted instanceof Sentence)) throw e;
        Sentence that = (Sentence) wanted;
        if(!this.operator.equals(that.operator)) throw e;
        if(this.operands.size() != that.operands.size()) throw e;
        for(int i = 0; i < operands.size(); i++)
            this.operands.get(i).unifyImpl(unifier, that.operands.get(i));
    }
    
    @Override public String toString() { return this.show(); }

/*
    @Override
    public void prettyPrint(StringBuilder result, int prec, boolean debugging) {
        boolean parens = prec >= operator.precedence;
        String glue = "";
        switch(operands.size()) {
            case 0: result.append(operator.StringRepr); break;
            case 1:
                glue = operator.StringRepr;
            default:
                if(parens) result.append("(");
                for(Term t : operands) {
                    result.append(glue);
                    t.prettyPrint(result, operator.precedence, debugging);
                    glue = operator.StringRepr;
                }
                if(parens) result.append(")");
        }
    }
*/

    @Override
    public StringExpression asStringExpression(int prec) {
        boolean parens = prec >= operator.precedence;
        StringExpression glue   = new ReadOnlyStringWrapper("");
        StringExpression result = new ReadOnlyStringWrapper("");
        switch(operands.size()) {
            case 0: return operator.stringRepr;
            case 1:
                glue = operator.stringRepr;
            default:
                if(parens) result = result.concat("(");
                for(Term t : operands) {
                    result = result.concat(glue)
                                   .concat(t.asStringExpression(operator.precedence));
                    glue = operator.stringRepr;
                }
                return parens ? result.concat(")") : result;
        }
    }

    @Override public Collection<Term> children() { return operands; }
    @Override public Term copyWithChildren(Collection<Term> chlds) {
        try {
            return new Sentence(operator, chlds);
        } catch (Exception ex) {
            Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, "Malformed children collection in copy " + chlds.toString(), ex);
            return null;
        }
    }
}
