package com.company;

public interface IPolicyDecisionMaker {
    boolean isAllowedAccess(String username, Operation operation);

    void reset();
}
