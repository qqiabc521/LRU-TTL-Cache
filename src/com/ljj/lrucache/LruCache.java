package com.ljj.lrucache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LruCache<K, V> implements ILruCache<K,V> {

    private int capacity;
    private HashMap<K, Node> map = new HashMap<>();
    private Node head;
    private Node end;

    public LruCache(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        if (map.containsKey(key)) {
            Node<K, V> n = map.get(key);
            remove(n);
            setEnd(n);
            return n.value;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node n = map.get(key);
            n.value = value;

            if (n != end) {
                remove(n);
                setEnd(n);
            }
        } else {
            Node create = new Node(key, value);
            map.put(key, create);
            setEnd(create);

            if (capacity < map.size()) {
                map.remove(head.key);
                remove(head);
            }
        }
    }

    @Override
    public V remove(K key) {
        if (map.containsKey(key)) {
            Node n = map.remove(key);
            remove(n);
            return (V) n.value;
        }
        return null;
    }

    @Override
    public int size() {
        return map.size();
    }

    private void remove(Node node) {
        if (node.pre != null) {
            node.pre.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.pre = node.pre;
        } else {
            end = node.pre;
        }
    }

    private void setHead(Node node) {
        node.next = head;
        node.pre = null;

        if (head != null) {
            head.pre = node;
        }

        head = node;

        if (end == null) {
            end = head;
        }
    }

    private void setEnd(Node node) {
        node.pre = end;
        node.next = null;

        if (end != null) {
            end.next = node;
        } else {
            end = node;
            head = end;
        }

        end = node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("link:").append("\n");
        Node tempHead = head;
        while (tempHead != null) {
            sb.append(tempHead.key).append(":").append(tempHead.value).append("; ");
            tempHead = tempHead.next;
        }
        sb.append("\n");

        sb.append("map:").append("\n");
        Set<Map.Entry<K, Node>> set = map.entrySet();
        Iterator<Map.Entry<K, Node>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Node<K, V> node = iterator.next().getValue();
            sb.append(node.key).append(":").append(node.value).append("; ");
        }

        sb.append("\n");

        return sb.toString();
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node pre;
        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key + ":" + value;
        }
    }
}
