package com.example.demo.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.exception.CredencialesInvalidasException;
import com.example.demo.exception.CuentaBloqueadaException;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    UsuarioRepository repo; // doble: no toca la base de datos real
    @InjectMocks
    LoginService service; // la clase que probamos

    Usuario ana;

    @BeforeEach
    void preparar() { // se ejecuta antes de CADA prueba
        ana = new Usuario("ana", "Patitas2026", "Ana");
    }

    @Test
    @DisplayName("CP-01 Login correcto devuelve bienvenida")
    void loginCorrecto_devuelveBienvenida() {
        // Arrange
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        // Act
        String resultado = service.login("ana", "Patitas2026");
        // Assert
        assertEquals("Bienvenido, Ana", resultado);
        assertEquals(0, ana.getIntentos());
    }

    // ---------- Paso 9 · casos negativos ----------

    @Test
    @DisplayName("CP-02 Clave incorrecta")
    void claveIncorrecta_lanzaExcepcion() {
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("ana", "1234"));
        assertEquals(1, ana.getIntentos());
    }

    @Test
    @DisplayName("CP-03 Usuario inexistente")
    void usuarioInexistente_lanzaExcepcion() {
        when(repo.findByUsuario("pepe")).thenReturn(Optional.empty());
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("pepe", "Patitas2026"));
    }

    @Test
    @DisplayName("CP-04 Campos vacíos")
    void camposVacios_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> service.login("", ""));
    }

    // ---------- Paso 10 · valores límite ----------
    @Test
    @DisplayName("CP-05 Segundo fallo no bloquea")
    void segundoFallo_noBloquea() {
        ana.setIntentos(1);
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("ana", "1234"));
        assertFalse(ana.isBloqueado());
    }

    @Test
    @DisplayName("CP-06 Tercer fallo bloquea la cuenta")
    void tercerFallo_bloqueaCuenta() {
        ana.setIntentos(2);
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("ana", "1234"));
        assertTrue(ana.isBloqueado());
    }

    @Test
    @DisplayName("CP-07 Correcto tras fallos reinicia intentos")
    void correctoTrasFallos_reiniciaIntentos() {
        ana.setIntentos(2);
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        assertEquals("Bienvenido, Ana", service.login("ana", "Patitas2026"));
        assertEquals(0, ana.getIntentos());
    }

    @Test
    @DisplayName("CP-09 Usuario con solo espacios")
    void usuarioConEspacios_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> service.login("   ", "Patitas2026"));
    }

    @Test
@DisplayName("CP-08 Cuenta bloqueada no permite login")
void cuentaBloqueada_lanzaExcepcion() {
    // Arrange: preparamos al usuario bloqueado
    ana.setBloqueado(true);
    when(repo.findByUsuario("ana"))
        .thenReturn(Optional.of(ana));

    // Act y Assert: verificamos la excepción
    assertThrows(CuentaBloqueadaException.class,
        () -> service.login("ana", "Patitas2026"));
}
}
