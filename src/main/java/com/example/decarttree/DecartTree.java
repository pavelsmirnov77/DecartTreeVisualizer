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
    public Node find(int key) {
        Node current = root;
        while (current != null && current.getKey() != key) {
            if (key < current.getKey()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        if (current == null) {
            return null;
        }
        return current;
    }

    public void delete(int key) {
        root = delete(root, key);
    }
    private Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (node.getKey() == key) {
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            }
            else if (node.getLeft() == null) {
                return node.getRight();
            }
            else if (node.getRight() == null) {
                return node.getLeft();
            }
            else {
                Node temp = findMin(node.getRight());
                node.setKey(temp.getKey());
                node.setValue(temp.getValue());
                node.setRight(delete(node.getRight(), temp.getKey()));
            }
        }
        else if (node.getKey() > key) {
            node.setLeft(delete(node.getLeft(), key));
        }
        else {
            node.setRight(delete(node.getRight(), key));
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    public Node getRoot() {
        return root;
    }
}