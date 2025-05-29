package pe.edu.vallegrande.workshop.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.edu.vallegrande.workshop.dto.WorkshopKafkaEventDto;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Test
    void testSendWorkshopEvent_success() throws Exception {
        WorkshopKafkaEventDto eventoDto = new WorkshopKafkaEventDto();
        eventoDto.setId(1L);
        String mensaje = "{\"id\":1}";

        when(objectMapper.writeValueAsString(eventoDto)).thenReturn(mensaje);

        ProducerRecord<String, String> record = new ProducerRecord<>("workshop-events", "1", mensaje);
        CompletableFuture<SendResult<String, String>> future = CompletableFuture
                .completedFuture(new SendResult<>(record, null));

        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(future);

        kafkaProducerService.sendWorkshopEvent(eventoDto);

        verify(objectMapper).writeValueAsString(eventoDto);
        verify(kafkaTemplate).send("workshop-events", "1", mensaje);
    }

    @Test
    void testSendWorkshopEvent_exception() throws Exception {
        WorkshopKafkaEventDto eventoDto = new WorkshopKafkaEventDto();
        eventoDto.setId(1L);

        when(objectMapper.writeValueAsString(eventoDto))
                .thenThrow(new RuntimeException("Falla de serialización"));

        kafkaProducerService.sendWorkshopEvent(eventoDto);

        verify(objectMapper).writeValueAsString(eventoDto);
        // no verify en kafkaTemplate porque no se llega a llamar por la excepción
    }

}
