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
interface PrettyPrintable {
    public default String dbgString() {
        StringBuilder b = new StringBuilder();
        this.prettyPrint(b, -1, true);
        return b.toString();
    }
    public default String show() {
        StringBuilder b = new StringBuilder();
        this.prettyPrint(b, -1, false);
        return b.toString();
    }
    public void prettyPrint(StringBuilder result, int prec, boolean debugging);
}
