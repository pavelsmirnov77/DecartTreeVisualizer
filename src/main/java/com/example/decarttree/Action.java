package com.example.decarttree;

public class Action {
    private final ActionType actionType;
    private int key;
    private int value;

    public Action(ActionType actionType, int key, int value) {
        this.actionType = actionType;
        this.key = key;
        this.value = value;
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
        return value;
    }
}
