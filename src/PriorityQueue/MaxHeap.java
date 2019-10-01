package PriorityQueue;

import java.util.ArrayList;

class pqNode<T extends Comparable> {
    public int sno;
    public T value;

    pqNode(int sno, T value) {
        this.sno = sno;
        this.value = value;
    }

    int compareTo(pqNode<T> element) {
        return (this.value.compareTo(element.value) != 0) ? this.value.compareTo(element.value)
                : element.sno - this.sno;
    }
}

public class MaxHeap<T extends Comparable> implements PriorityQueueInterface<T> {
    public ArrayList<pqNode<T>> list = new ArrayList<pqNode<T>>();
    int sno = 0;

    boolean hasLeft(int index){
        return list.size() > 2*index+1;
    }

    boolean hasRight(int index){
        return list.size() > 2*index+2;
    }

    int getLeft(int index) {
        return (2 * index + 1);
    }

    int getRight(int index) {
        return (2 * index + 2);
    }

    int getParent(int index) {
        return (index - 1) / 2;
    }

    void swap(int index1, int index2) {
        pqNode<T> temp;
        temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }

    @Override
    public void insert(T element) {
        list.add(new pqNode<T>(sno++, element));
        int index = list.size() - 1;
        while (list.get(index).compareTo(list.get(getParent(index))) > 0) {
            swap(index, getParent(index));
            index = getParent(index);
        }
    }

    @Override
    public T extractMax() {
        if(list.size() == 0) return null;
        swap(0, list.size() - 1);
        T toReturn = list.get(list.size() - 1).value;
        list.remove(list.size() - 1);
        int index = 0;
        if(list.size() == 0) return toReturn;
        pqNode<T> element = list.get(0);
        while (true) {
            int toSwap;
            if(hasRight(index)){
            toSwap = (list.get(getRight(index)).compareTo(list.get(getLeft(index))) > 0) ? getRight(index)
                    : getLeft(index);
            } else if(hasLeft(index)){
                toSwap = getLeft(index);
            } else break;
            if (list.get(toSwap).compareTo(element) < 0) break;
            swap(toSwap, index);
            index = toSwap;
        }
        return toReturn;
    }

}