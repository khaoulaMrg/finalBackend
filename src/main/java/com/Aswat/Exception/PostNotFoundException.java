package com.Aswat.Exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Post with ID " + id + " not found");
    }
}