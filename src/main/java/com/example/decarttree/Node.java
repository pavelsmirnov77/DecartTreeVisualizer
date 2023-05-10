package com.example.decarttree;

import javafx.scene.paint.Color;

public class Node {
    public Color color;
    private int key;
    private int priority;
    private Node left;
    private Node right;

    public Node(int key, int priority) {
        this.key = key;
        this.priority = priority;
        this.left = null;
        this.right = null;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
