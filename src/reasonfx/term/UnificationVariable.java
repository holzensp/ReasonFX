/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.term;

/**
 *
 * @author holzensp
 * @param <T>
 */
public interface UnificationVariable<T extends UnificationVariable> extends Variable<T> {
    public static final int varCodePoints[] = "φψρσπξηχωθταβγδεζκμυ".codePoints().toArray();
    public static final int DigitOffset = 0x2080 - Character.codePointAt("0",0);
    
    public static String mkString(final int id) {
        StringBuilder b = new StringBuilder()
            .appendCodePoint(varCodePoints[id % varCodePoints.length]);
        if(id >= varCodePoints.length) {
            String.valueOf(id / varCodePoints.length)
                .codePoints()
                .map(i -> DigitOffset + i)
                .forEach(b::appendCodePoint);
        }
        return b.toString();
    }
}
