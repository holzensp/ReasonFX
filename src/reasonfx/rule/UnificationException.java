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
public class UnificationException extends Exception {
    public final Term given;
    public final Term wanted;
    
    public UnificationException(Term wasGiven, Term wasWanted) {
        super(String.format("Can't unify %s with %s", wasGiven.dbgString(), wasWanted.dbgString()));
        given  = wasGiven;
        wanted = wasWanted;
    }
}
