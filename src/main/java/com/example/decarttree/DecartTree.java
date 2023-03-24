package com.example.decarttree;

public class DecartTree {

    private Node root;

    public DecartTree() {
        root = null;
    }

    public void insert(int key, int value) {
        root = insert(root, key, value);
    }

    private Node insert(Node node, int key, int value) {
        if (node == null) {
            return new Node(key, value);
        }
        if (node.getKey() > key) {
            Node newNode = insert(node.getLeft(), key, value);
            node.setLeft(newNode);
            if (node.getPriority() < newNode.getPriority()) {
                node = rotateRight(node);
            }
        } else {
            Node newNode = insert(node.getRight(), key, value);
            node.setRight(newNode);
            if (node.getPriority() < newNode.getPriority()) {
                node = rotateLeft(node);
            }
        }
        return node;
    }

    private Node rotateRight(Node node) {
        Node left = node.getLeft();
        node.setLeft(left.getRight());
        left.setRight(node);
        return left;
    }

    private Node rotateLeft(Node node) {
        Node right = node.getRight();
        node.setRight(right.getLeft());
        right.setLeft(node);
        return right;
    }

    public Node getRoot() {
        return root;
    }
}