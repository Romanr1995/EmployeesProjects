package ru.consulting.exception_handling;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchEntityException extends RuntimeException {

    public NoSuchEntityException(Long id, Class c) {
        super("Entity  " + c.getSimpleName() + " with id = " + id + " is not found");
    }

    public NoSuchEntityException(String message) {
        super(message);
    }
}
