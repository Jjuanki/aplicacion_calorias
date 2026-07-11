package com.juanc.aplicacion_calorias;

public class Validador {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) return false;
        return correo.trim().matches(EMAIL_REGEX);
    }

    public static boolean validarPassword(String password) {
        if (password == null || password.trim().isEmpty()) return false;
        return password.trim().length() >= 6;
    }
}
