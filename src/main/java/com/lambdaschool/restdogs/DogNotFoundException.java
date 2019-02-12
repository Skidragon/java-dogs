package com.lambdaschool.restdogs;

public class DogNotFoundException extends RuntimeException {
    public DogNotFoundException(Long id) {
        super("Cound not find dog with id of " + id);
    }
}
