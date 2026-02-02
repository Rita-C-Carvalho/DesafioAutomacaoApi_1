package br.com.teste.accenture.exceptions;

public class TesteAccentureExceptions extends RuntimeException {

    public TesteAccentureExceptions(String message) {

        super(message);
    }

    public TesteAccentureExceptions(String message, Throwable cause){
        super(message, cause);
    }
}
