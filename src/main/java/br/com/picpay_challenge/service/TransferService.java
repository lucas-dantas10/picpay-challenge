package br.com.picpay_challenge.service;

import br.com.picpay_challenge.dto.CreateTransferRequest;
import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.Transfer;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;
import br.com.picpay_challenge.exception.UnauthorizedException;
import br.com.picpay_challenge.repository.TransferRepository;
import br.com.picpay_challenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    public void createTransfer(CreateTransferRequest request) throws Exception {
        Optional<User> sender = Optional
                .ofNullable(userRepository.findById(UUID.fromString(request.payer()))
                .orElseThrow(NotFoundException::new));

        Optional<User> receiver = Optional
                .ofNullable(userRepository.findById(UUID.fromString(request.payee()))
                        .orElseThrow(NotFoundException::new));

        log.info("Usuario: {} | Ação: {} | Status: {}", sender.get().getFullname(), "Busca do usuário pagador", "Sucesso");
        log.info("Usuario: {} | Ação: {} | Status: {}", receiver.get().getFullname(), "Busca do usuário recebedor", "Sucesso");

       boolean isShopkeeper = sender.get()
               .getRoles()
               .stream()
               .anyMatch(role -> role.getName().name().equals(RoleEnum.SHOPKEEPER.name()));

       if (isShopkeeper) {
           log.error("Usuario: {} | Ação: {} | Status: {}", sender.get().getFullname(), "Não autorizado", "Erro");
           throw new UnauthorizedException("Unauthorized");
       }

        transferRepository.save(Transfer.builder()
                        .sender(sender.get())
                        .receiver(receiver.get())
                        .transferDate(LocalDateTime.now())
                        .amount(request.value())
                        .build());

        log.info("Ação: {} | Status: {}", "Criada transferência", "Sucesso");
    }
}
