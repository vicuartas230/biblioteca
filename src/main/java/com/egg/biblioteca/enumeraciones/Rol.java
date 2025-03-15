package com.egg.biblioteca.enumeraciones;

import java.util.Arrays;
import java.util.List;

public enum Rol {
    USER,
    ADMIN;


    public static List<Rol> getRoles() {
        return Arrays.asList(Rol.values());
    }
}
