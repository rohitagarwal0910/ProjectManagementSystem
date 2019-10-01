package RedBlack;

import Util.RBNodeInterface;

import java.util.ArrayList;
import java.util.List;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {
    public T key;
    public List<E> values = null;
    boolean b;
    public RedBlackNode<T, E> left = null;
    public RedBlackNode<T, E> right = null;
    RedBlackNode<T, E> parent = null;

    RedBlackNode(T key, E value) {
        if (key != null) {
            this.key = key;
            values = new ArrayList<E>();
            values.add(value);
            left = new RedBlackNode<T,E>(null, null);
            right = new RedBlackNode<T,E>(null, null);
            left.parent = this;
            right.parent = this;
        }
    }

    @Override
    public E getValue() {
        return null;
    }

    @Override
    public List<E> getValues() {
        return values;
    }
}
