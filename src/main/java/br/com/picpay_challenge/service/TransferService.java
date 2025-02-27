package br.com.picpay_challenge.service;

import br.com.picpay_challenge.dto.CreateTransferRequest;
import br.com.picpay_challenge.entity.Transfer;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;
import br.com.picpay_challenge.exception.InsufficientBalanceException;
import br.com.picpay_challenge.exception.UnauthorizedException;
import br.com.picpay_challenge.repository.TransferRepository;
import br.com.picpay_challenge.repository.UserRepository;
import br.com.picpay_challenge.service.authorize.AuthorizeClient;
import br.com.picpay_challenge.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final AuthorizeClient authorizeClient;
    private final NotificationService notificationService;

    @Transactional
    public void createTransfer(CreateTransferRequest request) throws Exception {
        log.info("Ação: {} | Status: {}", "Criando transferência", "Começo");

        User sender = userRepository.findById(UUID.fromString(request.payer())).orElseThrow(NotFoundException::new);
        User receiver = userRepository.findById(UUID.fromString(request.payee())).orElseThrow(NotFoundException::new);

        boolean isShopkeeper = sender
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().name().equals(RoleEnum.SHOPKEEPER.name()));

        if (isShopkeeper) {
            log.error("Erro de autorização negada");
            throw new UnauthorizedException("Unauthorized");
        }

        if (sender.getBalance().compareTo(BigDecimal.ZERO) == 0
                || sender.getBalance().compareTo(request.value()) < 0) {
            log.error("Erro de saldo insuficiente");
            throw new InsufficientBalanceException("Insufficient balance");
        }

        if (!authorizeClient.getAuthorize().data().authorization()) {
            log.error("Erro no autorizador externo");
            throw new Exception("Not Authorized");
        }

        BigDecimal senderNewBalance = sender.getBalance().subtract(request.value());
        BigDecimal receiverNewBalannce = receiver.getBalance().add(request.value());

        sender.setBalance(senderNewBalance);
        receiver.setBalance(receiverNewBalannce);

        transferRepository.save(Transfer.builder()
                .sender(sender)
                .receiver(receiver)
                .transferDate(LocalDateTime.now())
                .amount(request.value())
                .build());

        userRepository.save(sender);
        userRepository.save(receiver);

        notificationService.notifyClient();

        log.info("Ação: {} | Status: {}", "Criada transferência", "Sucesso");
    }
}
