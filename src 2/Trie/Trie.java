package Trie;

import java.util.ArrayList;

public class Trie<T> implements TrieInterface {
    TrieNode<T> root = new TrieNode<T>();

    @Override
    public boolean delete(String word) {
        TrieNode<T> cn = root;
        TrieNode<T> temp = new TrieNode<T>();
        TrieNode<T> last = temp;
        int td = 0;
        for (int i = 0; i < word.length(); i++) {
            if (cn.branches[word.charAt(i) - 32] == null) {
                return false;
            }
            if (cn.branches[word.charAt(i) - 32].children <= 1 && !cn.branches[word.charAt(i) - 32].isEnd
                    && last == temp) {
                last = cn;
                td = word.charAt(i) - 32;
            }
            if (cn.branches[word.charAt(i) - 32].children > 1 || (cn.branches[word.charAt(i) - 32].isEnd && cn.branches[word.charAt(i) - 32].children != 0)) {
                last = temp;
            }
            if (cn.branches[word.charAt(i) - 32].children == 0 && last == temp) {
                last = cn;
                td = word.charAt(i) - 32;
            }
            cn = cn.branches[word.charAt(i) - 32];
        }
        if (cn.isEnd == true) {
            cn.isEnd = false;
            cn.value = null;
            if (cn.children == 0) {
                last.branches[td] = null;
                last.children -= 1;
            }
            return true;
        } else
            return false;
    }

    @Override
    public TrieNode search(String word) {
        TrieNode<T> cn = root;
        for (int i = 0; i < word.length(); i++) {
            if (cn.branches[word.charAt(i) - 32] == null) {
                return null;
            }
            cn = cn.branches[word.charAt(i) - 32];
        }
        if (cn.isEnd == true)
            return cn;
        else
            return null;
    }

    @Override
    public TrieNode startsWith(String prefix) {
        TrieNode<T> cn = root;
        for (int i = 0; i < prefix.length(); i++) {
            cn = cn.branches[prefix.charAt(i) - 32];
            if (cn == null)
                break;
        }
        return cn;
    }

    @Override
    public void printTrie(TrieNode trieNode) {
        if (trieNode.isEnd == true) {
            System.out.println(trieNode.value.toString());
        }
        for (int i = 0; i < trieNode.branches.length; i++) {
            if (trieNode.branches[i] != null) {
                printTrie(trieNode.branches[i]);
            }
        }
    }

    @Override
    public boolean insert(String word, Object value) {
        TrieNode<T> cn = root;
        for (int i = 0; i < word.length(); i++) {
            if (cn.branches[word.charAt(i) - 32] == null) {
                cn.branches[word.charAt(i) - 32] = new TrieNode<T>();
                cn.children += 1;
            }
            cn = cn.branches[word.charAt(i) - 32];
        }
        if (cn.isEnd == true)
            return false;
        cn.isEnd = true;
        cn.value = (T) value;
        return true;
    }

    ArrayList<Character> printLevelResult = new ArrayList<Character>();

    public void printLevel_(int level, TrieNode trieNode) {
        if (level == 1) {
            for (int i = 0; i < trieNode.branches.length; i++) {
                if (trieNode.branches[i] != null && i != 0) {
                    printLevelResult.add((char) (i + 32));
                }
            }
        } else {
            for (int i = 0; i < trieNode.branches.length; i++) {
                if (trieNode.branches[i] != null) {
                    printLevel_(level - 1, trieNode.branches[i]);
                }
            }
        }
    }

    public void sortAndPrint(ArrayList<Character> list, int level) {
        list.sort(null);
        System.out.print("Level " + level + ": ");
        if (list.size() > 0)
            System.out.print(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            System.out.print("," + list.get(i));
        }
        System.out.println();
    }

    @Override
    public void printLevel(int level) {
        printLevelResult.clear();
        printLevel_(level, root);
        sortAndPrint(printLevelResult, level);
    }

    public void print_(int level, ArrayList<TrieNode<T>> list) {
        ArrayList<Character> clist = new ArrayList<Character>();
        ArrayList<TrieNode<T>> nlist = new ArrayList<TrieNode<T>>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).branches.length; j++) {
                if (list.get(i).branches[j] != null) {
                    if ((char) j + 32 != ' ')
                        clist.add((char) (j + 32));
                    nlist.add(list.get(i).branches[j]);
                }
            }
        }
        sortAndPrint(clist, level);
        if (nlist.isEmpty() == false) {
            print_(level + 1, nlist);
        }
    }

    @Override
    public void print() {
        System.out.println("-------------");
        System.out.println("Printing Trie");
        ArrayList<TrieNode<T>> tlist = new ArrayList<TrieNode<T>>();
        tlist.add(root);
        print_(1, tlist);
        System.out.println("-------------");
    }

}