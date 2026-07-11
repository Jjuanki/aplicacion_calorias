package com.juanc.aplicacion_calorias;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void correoValido() {
        assertTrue(Validador.validarCorreo("juan@gmail.com"));
        assertTrue(Validador.validarCorreo("test.user@domain.co.uk"));
        assertTrue(Validador.validarCorreo("user+tag@example.com"));
    }

    @Test
    public void correoInvalido() {
        assertFalse("Debe ser falso para nulo", Validador.validarCorreo(null));
        assertFalse("Debe ser falso para vacío", Validador.validarCorreo(""));
        assertFalse("Debe ser falso para espacios", Validador.validarCorreo("   "));
        assertFalse("Falta el @", Validador.validarCorreo("juan.com"));
        assertFalse("Falta el dominio", Validador.validarCorreo("juan@"));
        assertFalse("Dominio demasiado corto", Validador.validarCorreo("juan@c.c"));
    }

    @Test
    public void passwordValida() {
        assertTrue("Mínimo 6 caracteres", Validador.validarPassword("123456"));
        assertTrue("Más de 6 caracteres", Validador.validarPassword("abcdef123"));
    }

    @Test
    public void passwordInvalida() {
        assertFalse("Debe ser falso para nulo", Validador.validarPassword(null));
        assertFalse("Debe ser falso para vacío", Validador.validarPassword(""));
        assertFalse("Menos de 6 caracteres", Validador.validarPassword("12345"));
    }
}