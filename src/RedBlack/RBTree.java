package RedBlack;

public class RBTree<T extends Comparable, E> implements RBTreeInterface<T, E> {
    public RedBlackNode<T, E> root = null;

    void insert_(T key, E value, RedBlackNode<T, E> cn) {
        if (cn == null) {
            root = new RedBlackNode<T, E>(key, value);
            root.b = true;
            return;
        } else {
            while (true) {
                if (key.compareTo(cn.key) < 0) {
                    if (cn.left.key == null) {
                        cn.left = new RedBlackNode<T, E>(key, value);
                        cn.left.b = false;
                        cn.left.parent = cn;
                        cn = cn.left;
                        break;
                    } else
                        cn = cn.left;
                } else if (key.compareTo(cn.key) == 0) {
                    cn.values.add(value);
                    return;
                } else if (key.compareTo(cn.key) > 0) {
                    if (cn.right.key == null) {
                        cn.right = new RedBlackNode<T, E>(key, value);
                        cn.right.b = false;
                        cn.right.parent = cn;
                        cn = cn.right;
                        break;
                    } else
                        cn = cn.right;
                }
            }
        }
        balance(cn);
    }

    void balance(RedBlackNode<T, E> cn) {
        if (cn.parent == null) {
            cn.b = true;
            return;
        }
        if (cn.parent.b == true) {
            return;
        }
        boolean l = false;
        if (cn.parent.parent.left == cn.parent)
            {l = true;}
        if (l && cn.parent.parent.right.key != null && cn.parent.parent.right.b == false) {
            cn.parent.b = true;
            cn.parent.parent.b = false;
            cn.parent.parent.right.b = true;
            balance(cn.parent.parent);
        } else if (!l && cn.parent.parent.left.key != null && cn.parent.parent.left.b == false) {
            cn.parent.b = true;
            cn.parent.parent.b = false;
            cn.parent.parent.left.b = true;
            balance(cn.parent.parent);
        }
        else if(l){
            if (cn == cn.parent.left) {
                cn.parent.b = true;
                cn.parent.parent.b = false;
                rotateRight(cn);
            } else {
                cn.b = true;
                cn.parent.parent.b = false;
                rotateLeft(cn.right);
                rotateRight(cn.left);
            }
        } else if (!l) {
            if (cn == cn.parent.right) {
                cn.parent.b = true;
                cn.parent.parent.b = false;
                rotateLeft(cn);
            } else {
                cn.b = true;
                cn.parent.parent.b = false;
                rotateRight(cn.left);
                rotateLeft(cn.right);
            }
        }

    }

    void rotateRight(RedBlackNode<T, E> cn) {
        boolean l = false;
        RedBlackNode<T, E> ggp = cn.parent.parent.parent;
        RedBlackNode<T, E> gp = cn.parent.parent;
        RedBlackNode<T, E> p = cn.parent;
        if (ggp != null && ggp.left == gp)
            l = true;
        p.right.parent = gp;
        gp.left = p.right;
        p.right = gp;
        gp.parent = p;
        if (ggp != null && l)
            ggp.left = p;
        else if (ggp != null)
            ggp.right = p;
        p.parent = ggp;
        if (ggp == null)
            root = p;
    }

    void rotateLeft(RedBlackNode<T, E> cn) {
        boolean l = false;
        RedBlackNode<T, E> ggp = cn.parent.parent.parent;
        RedBlackNode<T, E> gp = cn.parent.parent;
        RedBlackNode<T, E> p = cn.parent;
        if (ggp != null && ggp.left == gp)
            l = true;
        p.left.parent = gp;
        gp.right = p.left;
        p.left = gp;
        gp.parent = p;
        if (ggp != null && l)
            ggp.left = p;
        else if (ggp != null)
            ggp.right = p;
        p.parent = ggp;
        if (ggp == null)
            root = p;
    }

    @Override
    public void insert(T key, E value) {
        insert_(key, value, root);
    }

    @Override
    public RedBlackNode<T, E> search(T key) {
        RedBlackNode<T, E> cn = root;
        if (root == null)
            return new RedBlackNode<T, E>(null, null);
        else
            while (true) {
                if (cn.key == null) {
                    return cn;
                } else if (key.compareTo(cn.key) < 0) {
                    cn = cn.left;
                } else if (key.compareTo(cn.key) == 0) {
                    break;
                } else if (key.compareTo(cn.key) > 0) {
                    cn = cn.right;
                }
            }
        return cn;
    }
}