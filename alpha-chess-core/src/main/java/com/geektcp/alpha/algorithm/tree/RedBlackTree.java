package com.geektcp.alpha.algorithm.tree;

import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * @author tanghaiyang on 2019/9/16.
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    private static final boolean RED = true;

    private static final boolean BLACK = false;

    private class Node {
        K key;
        V value;
        Node left;
        Node right;
        boolean color;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.color = RED;
        }
    }

    private Node root;

    // 节点数
    private int size;

    public RedBlackTree() {
        root = null;
        size = 0;
    }

    /**
     * 判断结点node的颜色
     *
     * @param node
     * @return
     */
    private boolean isRed(Node node) {
        if (Objects.isNull(node)) {
            return BLACK;
        }
        return node.color;
    }


    /**
     * 左旋转(针对一整棵树，而不是节点)
     * //      node                                       x
     * //     /    \              左旋转                /    \
     * //    T1     x           --------->            node    T3
     * //         /   \                              /    \
     * //       T2     T3                           T1    T2
     *
     * @param node 要旋转的树
     * @return 旋转后的树
     */
    private Node leftRotate(Node node) {
        Node x = node.right;

        //左旋转
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;

        return x;
    }


    /**
     * 右旋转(针对一整棵树，而不是节点)
     * //      node                                       x
     * //     /    \              右旋转                /    \
     * //    x     T2           --------->             y     node
     * //  /  \                                             /    \
     * // y   T1                                           T1    T2
     *
     * @param node 要旋转的树
     * @return 旋转后的树
     */
    private Node rightRotate(Node node) {
        Node x = node.left;

        //右旋转
        node.left = x.right;
        x.right = node;

        x.color = node.color;
        node.color = RED;

        return x;
    }

    /**
     * 颜色翻转
     *
     * @param node 要反转的节点
     */
    private void flipColors(Node node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    /**
     * 向红黑树树中添加新的元素（key, value）
     *
     * @param key   单词
     * @param value 单词频率
     */
    public void add(K key, V value) {
        root = add(root, key, value);
        root.color = BLACK; //最终根结点为黑色结点
    }


    /**
     * 向以node为根的红黑树中插入元素(key, value)，递归算法
     * 返回插入新结点后红黑树的根
     *
     * @param node  根节点
     * @param key   单词
     * @param value 相同的单词数量
     * @return 返回根节点
     */
    private Node add(Node node, K key, V value) {
        if (Objects.isNull(node)) {
            size++;
            return new Node(key, value); //默认插入红色结点
        }

        int compare = getCompare(node, key);
        if (compare < 0) {   // 判断插入的节点和父节点的关系，用于确定是左子节点还是由子节点
            node.left = add(node.left, key, value);   // 往左边递归
        } else if (compare > 0) {
            node.right = add(node.right, key, value);
        } else {
            // key.compareTo(node.key) == 0
            node.value = value;   // 子节点和父节点一样，就不插入
        }
        return rotate(node);
    }

    /**
     * 插入过后，进行旋转，根据左右子节点的颜色判断旋转方向
     *
     * @param node 旋转前的树
     * @return 旋转后的树
     */
    private Node rotate(Node node) {
        if (!isRed(node.left) && isRed(node.right)) {    // 左黑右红就左旋
            node = leftRotate(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) { // 左红并且左节点的左子节点也是红就右旋
            node = rightRotate(node);
        }
        if (isRed(node.left) && isRed(node.right)) {     // 左右都红就颜色反转，即左右子节点变为黑色，当前节点变为红
            flipColors(node);
        }

        return node;
    }

    /**
     * 返回以node为根结点的二分搜索树中，key所在的结点
     *
     * @param node 根节点
     * @param key  单词
     * @return 子树
     */
    private Node getNode(Node node, K key) {
        if (node == null) {
            return null;
        }
        int compare = getCompare(node, key);
        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return getNode(node.left, key);
        } else { // if compare > 0
            return getNode(node.right, key);
        }
    }

    /**
     * 返回以node为根的二分搜索树的最小键值所在的结点
     *
     * @param node 子树
     */
    private Node minimum(Node node) {

        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }

    /**
     * @param node 删除以node为根的二分搜索树中的最小结点
     * @return 返回删除结点后新的二分搜索树的根
     */
    private Node removeMin(Node node) {
        if (Objects.isNull(node.left)) {
            return removeRight(node);
        }
        node.left = removeMin(node.left);
        return node;
    }

    private Node removeRight(Node node) {
        Node rightNode = node.right;
        node.right = null;
        size--;
        return rightNode;
    }

    /**
     * @param key 从二分搜索树中删除键值为key的结点
     * @return 返回节点的value
     */
    public V remove(K key) {
        Node node = getNode(root, key);
        if (node != null) {
            root = remove(root, key);
            return node.value;
        }

        return null;
    }

    /**
     * @param node 删除掉以node为根的二分搜索树中键为key的结点，递归算法
     * @param key  key
     * @return     返回删除结点后新的二分搜索树的根
     */
    private Node remove(Node node, K key) {
        if (node == null) {
            return null;
        }
        int compare = getCompare(node, key);
        if (compare < 0) {
            node.left = remove(node.left, key);
            return node;
        } else if (compare > 0) {
            node.right = remove(node.right, key);
            return node;
        } else {//compare == 0
            //待删除结点左子树为空的情况
            if (Objects.isNull(node.left)) {
                return removeRight(node);
            }

            //待删除结点右子树为空的情况
            if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }

            /*
            待删除结点左右子树为空的情况
            找到比待删除结点大的最小结点，即待删除结点右子树的最小结点
            用这个结点顶替待删除结点的位置
            * */
            Node successor = minimum(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;
            node.left = node.right = null;

            return successor;

        }
    }

    public boolean contains(K key) {
        return getNode(root, key) != null;
    }

    public V get(K key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public void set(K key, V newValue) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new IllegalArgumentException(key + " 不存在！");
        }

        node.value = newValue;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void print(){
        List<List<Node>> nodes = new ArrayList<>();
        List<Node> nodePair = new ArrayList<>();
        nodePair.add(root);
        nodes.add(nodePair);
        print(nodes);
    }

    private List<Node> getChild(Node node){
        List<Node> ret = new ArrayList<>();
        if(Objects.nonNull(node.left)){
            ret.add(node.left);
        }
        if(Objects.nonNull(node.right)){
            ret.add(node.right);
        }
        return ret;
    }

    @SuppressWarnings("all")
    private void print(List<List<Node>> nodes){
        StringBuilder sb = new StringBuilder();
        List<List<Node>> ret = new ArrayList<>();

        for(List<Node> nodePair: nodes){
            sb.append("[");
            sb.append(Joiner.on(",").join(getStringPair(nodePair)));
            sb.append("]");
            nodePair.forEach(nodeChild -> {
                List<Node> children = getChild(nodeChild);
                if(CollectionUtils.isNotEmpty(children)) {
                    ret.add(children);
                }
            });
        }
        if(CollectionUtils.isNotEmpty(nodes)) {
            print(ret);
        }
    }

    private List<K> getStringPair( List<Node> nodePair){
        List<K> printStr = new ArrayList<>();
        nodePair.forEach(node -> printStr.add(node.key));
        return printStr;
    }

    /////////////////////
    private int getCompare(Node node, K key) {
        return key.compareTo(node.key);
    }

}