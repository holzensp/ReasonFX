/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonfx.util;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author holzensp
 */
public class EmptyCollection<E> extends AbstractCollection<E> {
    @Override public int size() { return 0; }

    @Override
    public Iterator<E> iterator() {
        return new Iterator() {
            @Override public boolean hasNext() { return false; }
            @Override public Object  next()    { throw new NoSuchElementException(); }
        };
    }
}
