package pe.edu.vallegrande.workshop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import pe.edu.vallegrande.workshop.config.DotenvInitializer;
import pe.edu.vallegrande.workshop.config.TestSecurityConfig;
import pe.edu.vallegrande.workshop.model.Workshop;
import pe.edu.vallegrande.workshop.service.WorkshopService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@ActiveProfiles("test")
@WebFluxTest(controllers = WorkshopController.class)
@AutoConfigureWebTestClient
@Import(TestSecurityConfig.class)  // Clase para desactivar seguridad en tests
@ContextConfiguration(initializers = DotenvInitializer.class)  // Solo si usas initializer
class WorkshopControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WorkshopService workshopService;  // MockBean para el servicio

    private Workshop workshop;

    @BeforeEach
    void setUp() {
        workshop = new Workshop();
        workshop.setId(1L);
        workshop.setName("Curso de Java");
        workshop.setDescription("Aprende Java desde cero");
        workshop.setStartDate(LocalDate.of(2025, 5, 1));
        workshop.setEndDate(LocalDate.of(2025, 5, 31));
        workshop.setObservation("Incluye proyectos");
        workshop.setState("A");
        workshop.setPersonId("2,10,11,19,18,17,25,24");
    }

    @Test
    void testGetWorkshopList() {
        Mockito.when(workshopService.findAllWorkshop()).thenReturn(Flux.just(workshop));

        webTestClient.get()
                .uri("/api/workshops/list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Workshop.class)
                .hasSize(1)
                .value(workshops -> {
                    assert workshops.get(0).getName().equals("Curso de Java");
                });
    }

    @Test
    void testGetWorkshopById() {
        Mockito.when(workshopService.findById(1L)).thenReturn(Mono.just(workshop));

        webTestClient.get()
                .uri("/api/workshops/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Curso de Java");
    }

    @Test
    void testCreateWorkshop() {
        Mockito.when(workshopService.createWorkshop(Mockito.any())).thenReturn(Mono.just(workshop));

        webTestClient.post()
                .uri("/api/workshops/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(workshop)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Curso de Java");
    }

    @Test
    void testDeleteWorkshop() {
        Mockito.when(workshopService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/workshops/delete/1")
                .exchange()
                .expectStatus().isOk();
    }
}
