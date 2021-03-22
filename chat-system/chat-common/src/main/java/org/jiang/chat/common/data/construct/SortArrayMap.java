package org.jiang.chat.common.data.construct;

import lombok.ToString;
import sun.reflect.generics.tree.VoidDescriptor;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 根据key排序的map
 * @author 蒋小胖
 */
public class SortArrayMap {
    /**
     * 核心数组
     */
    private Node[] buckets;

    private static final int DEFAULT_SIZE = 10;

    /**
     * 数组大小
     */
    private int size = 0;

    public SortArrayMap() {
        this.buckets = new Node[DEFAULT_SIZE];
    }

    /**
     * 写入数据
     * @param key
     * @param value
     */
    public void add(Long key,String value) {
        checkSize(size + 1);
        Node node = new Node(key, value);
        buckets[size++] = node;
    }

    /**
     * 校验是否需要扩容
     * @param size
     */
    public void checkSize(int size) {
        if (size >=  buckets.length) {
            //扩容自身的 3/2
            int oldSize = buckets.length;
            int newSize = oldSize + (oldSize >> 1);
            buckets = Arrays.copyOf(buckets,newSize);
        }
    }

    /**
     * 顺时针取出数据
     * @param key
     * @return
     */
    public String firstNodeValue(Long key) {
        if (size == 0) {
            return null;
        }
        for (Node node : buckets) {
            if (node == null) {
                break;
            }
            if (node.key >= key) {
                return node.value;
            }
        }

        return buckets[0].value;
    }

    /**
     * 排序
     */
    public void sort() {
        Arrays.sort(buckets, 0, size, (o1, o2) -> {
            if (o1.key > o2.key) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    public void print() {
        for (Node node : buckets) {
            if (node == null) {
                continue;
            }
            System.out.println(node.toString());
        }
    }

    public int size() {
        return size;
    }

    public void clear(){
        buckets = new Node[DEFAULT_SIZE];
        size = 0 ;
    }

    /**
     * 数据节点
     */
    private class Node {
        public Long key;
        public String value;

        public Node(Long key,String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
