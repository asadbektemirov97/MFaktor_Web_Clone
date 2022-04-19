package com.example.catalogservice.exceptions;


public class ResourceNotFoundException extends Throwable {
    public ResourceNotFoundException(Long id) {
        super(String.format("Resource with Id %d not found", id));
    }

    public ResourceNotFoundException(String id) {
        super(String.format("Resource with Id %d not found", id));
    }

    public ResourceNotFoundException(Integer id) {
        super(String.format("Resource with Id %d not found", id));
    }

}
