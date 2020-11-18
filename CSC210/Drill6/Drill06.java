package edu.spr19cs210.drill06;

public class Drill06 {

    public static <K, V> V returnValue(HashNode<K, V> node) {
        return node.getValue();
    }

    public static <K, V> K returnKey(HashNode<K, V> node) {
        return node.getKey();
    }

    public static <K, V> V findLastVal(HashNode<K, V> first) {
        while (!(first.getNext() == null)) {
            first = first.getNext();
        }
        return first.getValue();
    }

    public static <K, V> HashNode<K, V> findNodeByIndex(HashNode<K, V> first,
            int index) {
        for (int i = 0; i < index; i++) {
            first = first.getNext();
        }
        return first;
    }

    public static <K, V> int countNodes(HashNode<K, V> first) {
        int sum = 1;
        if (first == null) {
            return 0;
        } else {
            while (!(first.getNext() == null)) {
                first = first.getNext();
                sum++;
            }
            return sum;
        }
    }

}
