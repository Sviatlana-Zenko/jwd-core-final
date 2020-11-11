package com.epam.jwd.core_final.exception;

public class EntityDuplicateException extends RuntimeException {
    private final String entityName;

    public EntityDuplicateException(String entityName) {
        super();
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "Attempt to create entity duplicate (entity with name '" +
                entityName + "' already exists.";
    }
}
