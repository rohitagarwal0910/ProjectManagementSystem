package Trie;


import Util.NodeInterface;


public class TrieNode<T> implements NodeInterface<T> {
    T value;
    boolean isEnd;
    TrieNode<T> branches[];
    int children;

    TrieNode() {
        value = null;
        isEnd = false;
        children = 0;
        branches = new TrieNode[95];
        for(int i = 0; i < 95; i++){
            branches[i] = null;
        }
    }

    @Override
    public T getValue() {
        return value;
    }


}