package com.daw.services.exceptions;

public class PokemonNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PokemonNotFoundException(String message) {
        super(message);
    }

}