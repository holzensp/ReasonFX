/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.rule;

/**
 *
 * @author holzensp
 */
public abstract class Variable implements Term {
    private final int varID;

    protected Variable(int id) { varID = id; }
    public int getID() { return varID; }
}
