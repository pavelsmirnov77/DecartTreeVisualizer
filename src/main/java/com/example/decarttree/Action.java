package com.example.decarttree;

public class Action {
    private final ActionType actionType;
    private int key;
    private int priority;

    public Action(ActionType actionType, int key, int priority) {
        this.actionType = actionType;
        this.key = key;
        this.priority = priority;
    }

    public Action(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return priority;
    }
}