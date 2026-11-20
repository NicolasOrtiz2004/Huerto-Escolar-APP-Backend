package cl.duoc.mshuerto.service;

import cl.duoc.mshuerto.client.UsuarioClient;
import cl.duoc.mshuerto.dto.SectorRequest;
import cl.duoc.mshuerto.dto.SectorResponse;
import cl.duoc.mshuerto.exception.ApiException;
import cl.duoc.mshuerto.model.EstadoSector;
import cl.duoc.mshuerto.model.Sector;
import cl.duoc.mshuerto.repository.SectorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * PRUEBAS UNITARIAS DE SectorService
 * El repositorio y el cliente de ms-usuarios son FALSOS (Mockito):
 * no se necesita MySQL ni tener ms-usuarios encendido.
 */
// Activa Mockito para esta clase de pruebas
@ExtendWith(MockitoExtension.class)
class SectorServiceTest {

    // Repositorio falso (no usa MySQL)
    @Mock
    private SectorRepository sectorRepository;

    // Cliente falso: no necesita ms-usuarios encendido
    @Mock
    private UsuarioClient usuarioClient;

    // Servicio real que se prueba
    @InjectMocks
    private SectorService sectorService;

    // Ayuda: arma un SectorRequest de prueba
    private SectorRequest request(String codigo, Long responsableId) {
        return new SectorRequest(codigo, "Bancal Sur", "Bancal de flores nativas", "Junto al gimnasio", EstadoSector.EN_USO, responsableId);
    }

    // Prueba: código repetido → 409, sin guardar y sin llamar a ms-usuarios
    @Test
    void crearConCodigoRepetido_lanza409YNoGuarda() {
        when(sectorRepository.existsByCodigoIgnoreCase("S-01")).thenReturn(true);

        ApiException error = assertThrows(ApiException.class, () -> sectorService.crear(request("s-01", 1L)));

        assertEquals(HttpStatus.CONFLICT, error.getEstado());
        verify(sectorRepository, never()).save(any());
        verify(usuarioClient, never()).verificarResponsable(anyLong());
    }

    // Prueba: sector válido → se guarda en mayúsculas y se consulta al responsable en ms-usuarios
    @Test
    void crearSectorValido_guardaCodigoEnMayusculasYVerificaResponsable() {
        when(sectorRepository.existsByCodigoIgnoreCase("S-05")).thenReturn(false);
        // thenAnswer: en vez de un valor fijo, ejecuta código con el argumento recibido
        when(sectorRepository.save(any(Sector.class))).thenAnswer(inv -> {
            Sector s = inv.getArgument(0);
            s.setId(5L); // simulamos el AUTO_INCREMENT de MySQL
            return s;
        });

        SectorResponse respuesta = sectorService.crear(request("s-05", 1L));

        assertEquals(5L, respuesta.id());
        assertEquals("S-05", respuesta.codigo());
        verify(usuarioClient).verificarResponsable(1L); // se consultó a ms-usuarios
    }

    // Prueba: sin responsable no hay llamada a ms-usuarios
    @Test
    void crearSinResponsable_noLlamaAMsUsuarios() {
        when(sectorRepository.existsByCodigoIgnoreCase("S-06")).thenReturn(false);
        when(sectorRepository.save(any(Sector.class))).thenAnswer(inv -> inv.getArgument(0));

        sectorService.crear(request("S-06", null));

        verify(usuarioClient, never()).verificarResponsable(anyLong());
    }

    // Prueba: si ms-usuarios rechaza al responsable, el sector NO se guarda
    @Test
    void siMsUsuariosRechazaAlResponsable_noSeGuardaElSector() {
        when(sectorRepository.existsByCodigoIgnoreCase("S-07")).thenReturn(false);
        // doThrow(...).when(mock).metodo() = hace que un método void lance una excepción
        doThrow(new ApiException(HttpStatus.BAD_REQUEST, "El responsable debe ser un docente o tallerista"))
                .when(usuarioClient).verificarResponsable(3L);

        ApiException error = assertThrows(ApiException.class, () -> sectorService.crear(request("S-07", 3L)));

        assertEquals("El responsable debe ser un docente o tallerista", error.getMessage());
        verify(sectorRepository, never()).save(any());
    }

    // Prueba: editar sin cambiar el responsable no consulta a ms-usuarios
    @Test
    void actualizarSinCambiarResponsable_noLlamaAMsUsuarios() {
        Sector existente = new Sector("S-01", "Bancal Norte", "Bancal elevado", "Patio", EstadoSector.EN_USO, 1L);
        existente.setId(1L);
        when(sectorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sectorRepository.existsByCodigoIgnoreCaseAndIdNot("S-01", 1L)).thenReturn(false);
        when(sectorRepository.save(any(Sector.class))).thenAnswer(inv -> inv.getArgument(0));

        SectorResponse respuesta = sectorService.actualizar(1L, request("S-01", 1L)); // mismo responsable

        assertEquals("Bancal Sur", respuesta.nombre());
        verify(usuarioClient, never()).verificarResponsable(anyLong());
    }

    // Prueba: editar un sector que no existe → 404
    @Test
    void actualizarSectorInexistente_lanza404() {
        when(sectorRepository.findById(99L)).thenReturn(Optional.empty());

        ApiException error = assertThrows(ApiException.class, () -> sectorService.actualizar(99L, request("S-01", null)));

        assertEquals(HttpStatus.NOT_FOUND, error.getEstado());
    }
}
