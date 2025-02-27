package br.com.picpay_challenge.service.notification;

import br.com.picpay_challenge.dto.ErrorResponse;
import br.com.picpay_challenge.service.notify.NotifyClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotifyClient notifyClient;

    public void notifyClient() {
        try {
            ResponseEntity<Void> notifier = notifyClient.notifyClient();

            if (notifier.getStatusCode().is2xxSuccessful()) {
                log.info("Notificação enviada com sucesso!");
            }
        } catch (FeignException e) {
            if (e.status() == HttpStatus.GATEWAY_TIMEOUT.value()) {
                handleErrorResponse(e);
            } else {
                log.error("Erro ao enviar notificação: {}", e.getMessage());
            }
        }
    }

    public void handleErrorResponse(FeignException exception)  {
        try {
            String responseBody = exception.contentUTF8();
            ErrorResponse errorResponse = new ObjectMapper().readValue(responseBody, ErrorResponse.class);
            log.error("Erro 504 :: status: {} message: {} ", errorResponse.status(), errorResponse.message());
        } catch (Exception e) {
            log.error("Erro ao processar erro da api: {}", e.getMessage());
        }
    }
}
