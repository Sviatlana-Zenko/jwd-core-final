package com.epam.jwd.core_final.exception;

public class AssignationException extends RuntimeException {

    private final String entityName;

    public AssignationException(String entityName) {
        super();
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "There is no any" + entityName + "with requested parameters." +
               "New mission cannot be created.";
    }
}
