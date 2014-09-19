/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.rule;

import reasonfx.term.Term;

/**
 *
 * @author holzensp
 */
public final class Binding {
    public final Term  value;
    public final Given origin;
    public Binding(Term v, Given o) { value = v; origin = o; }
}
