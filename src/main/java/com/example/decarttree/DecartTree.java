package com.example.decarttree;
import java.util.ArrayList;
import java.util.List;
public class DecartTree {

    private Node root;
    private final List<Action> actions; // список действий
    private final List<Action> undoneActions; //список отмененных действий

    public DecartTree() {
        root = null;
        actions = new ArrayList<>();
        undoneActions = new ArrayList<>();
    }

    public void insert(int key, int priority) {
        addAction(new Action(ActionType.INSERT, key, priority));
        NodePair pair = split(root, key);
        root = merge(merge(pair.left, new Node(key, priority)), pair.right);
    }

    private Node insert(Node node, int key, int priority) {
        if (node == null) {
            return new Node(key, priority);
        }
        if (node.getKey() > key) {
            Node newNode = insert(node.getLeft(), key, priority);
            node.setLeft(newNode);
            if (node.getPriority() < newNode.getPriority()) {
                node = rotateRight(node);
            }
        } else {
            Node newNode = insert(node.getRight(), key, priority);
            node.setRight(newNode);
            if (node.getPriority() < newNode.getPriority()) {
                node = rotateLeft(node);
            }
        }
        return node;
    }

    public void delete(int key, int priority) {
        addAction(new Action(ActionType.DELETE, key, priority));
        NodePair pair = split(root, key);
        Node left = pair.left;
        Node right = pair.right;
        NodePair pair2 = split(right, key);
        root = merge(left, pair2.right);
        root = delete(root, key, priority); // удаление элемента с указанным ключом и приоритетом
    }

    private Node delete(Node node, int key, int priority) {
        if (node == null) {
            return null;
        }
        if (node.getKey() == key && node.getPriority() == priority) {
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            } else if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            } else {
                Node temp = findMin(node.getRight());
                node.setKey(temp.getKey());
                node.setPriority(temp.getPriority());
                node.setRight(delete(node.getRight(), temp.getKey(), temp.getPriority()));
                return node;
            }
        } else if (node.getKey() > key) {
            node.setLeft(delete(node.getLeft(), key, priority));
        } else {
            node.setRight(delete(node.getRight(), key, priority));
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
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

    public Node find(int key, int priority) {
        Node current = root;
        while (current != null && (current.getKey() != key || current.getPriority() != priority)) {
            if (key < current.getKey()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return current;
    }

    public void deleteAll() {
        addAction(new Action(ActionType.DELETE_ALL)); //добавляем новое действие - удаление всех элементов из дерева
        root = null;
    }

    public Node getRoot() {
        return root;
    }

    private void addAction(Action action) {
        actions.add(action);
    }

    public void undo() {
        if (!actions.isEmpty()) {
            Action lastAction = actions.remove(actions.size() - 1);
            switch (lastAction.getActionType()) {
                case INSERT -> root = delete(root, lastAction.getKey(), lastAction.getPriority());
                case DELETE -> root = insert(root, lastAction.getKey(), lastAction.getPriority());
                case DELETE_ALL -> actions.clear();
            }
            undoneActions.add(lastAction); // добавляем отмененное действие в список отмененных действий
        }
    }

    public void redo() {
        if (!undoneActions.isEmpty()) {
            Action lastUndoneAction = undoneActions.remove(undoneActions.size() - 1);
            switch (lastUndoneAction.getActionType()) {
                case INSERT -> root = insert(root, lastUndoneAction.getKey(), lastUndoneAction.getPriority());
                case DELETE -> root = delete(root, lastUndoneAction.getKey(), lastUndoneAction.getPriority());
                case DELETE_ALL -> actions.clear();
            }
            actions.add(lastUndoneAction); // добавляем повторенное действие обратно в список действий
        }
    }

    private NodePair split(Node node, int key) {
        if (node == null) {
            return new NodePair(null, null);
        }
        if (node.getKey() <= key) {
            NodePair pair = split(node.getRight(), key);
            node.setRight(pair.left);
            return new NodePair(node, pair.right);
        } else {
            NodePair pair = split(node.getLeft(), key);
            node.setLeft(pair.right);
            return new NodePair(pair.left, node);
        }
    }

    private Node merge(Node left, Node right) {
        if (left == null || right == null) {
            return left == null ? right : left;
        }
        if (left.getPriority() < right.getPriority()) {
            right.setLeft(merge(left, right.getLeft()));
            return right;
        } else {
            left.setRight(merge(left.getRight(), right));
            return left;
        }
    }
}
