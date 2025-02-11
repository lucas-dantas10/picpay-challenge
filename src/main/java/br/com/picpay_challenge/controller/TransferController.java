package br.com.picpay_challenge.controller;

import br.com.picpay_challenge.dto.CreateTransferRequest;
import br.com.picpay_challenge.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> transfer(@RequestBody CreateTransferRequest request) throws Exception {
        transferService.createTransfer(request);

        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }
}
