package cl.duoc.msusuarios.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/*
 * SEGURIDAD BÁSICA
 * Es el MISMO algoritmo que util/Seguridad.kt de la app (SHA-256 en
 * hexadecimal minúscula). Así el hash calculado en el teléfono y en el
 * servidor es idéntico y la app puede iniciar sesión sin conexión con la
 * última contraseña válida.
 * (En un sistema real se usaría BCrypt, que agrega "sal" y es más lento a propósito.)
 */
// final = nadie puede heredar de esta clase (es solo un "contenedor" de funciones)
public final class Seguridad {

    // Constructor privado: no se crean objetos Seguridad, se usan sus métodos static
    private Seguridad() {
    }

    // Recibe "huerto123" y devuelve su huella SHA-256 (siempre la misma para el mismo texto)
    public static String hashPassword(String password) {
        try {
            // MessageDigest es la clase de Java que calcula hashes; getBytes(UTF_8) pasa el texto a bytes
            byte[] bytes = MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes); // "a3f1..." (64 caracteres)
        // Java obliga a capturar esta excepción aunque SHA-256 siempre existe
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 existe en todas las JVM; esto no debería ocurrir nunca
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    // Compara el hash de lo que escribió el usuario con el guardado en la BD
    public static boolean passwordCorrecta(String passwordIngresada, String hashGuardado) {
        return hashPassword(passwordIngresada).equals(hashGuardado);
    }
}
