package br.com.picpay_challenge.unit.service;

import br.com.picpay_challenge.dto.AuthorizeDTO;
import br.com.picpay_challenge.dto.CreateTransferRequest;
import br.com.picpay_challenge.dto.DataDTO;
import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.Transfer;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;
import br.com.picpay_challenge.exception.InsufficientBalanceException;
import br.com.picpay_challenge.exception.UnauthorizedException;
import br.com.picpay_challenge.repository.TransferRepository;
import br.com.picpay_challenge.repository.UserRepository;
import br.com.picpay_challenge.service.TransferService;
import br.com.picpay_challenge.service.authorize.AuthorizeClient;
import br.com.picpay_challenge.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AuthorizeClient authorizeClient;

    @Mock
    private NotificationService notificationService;

    private User receiver;
    private User sender;
    private CreateTransferRequest createTransferRequest;
    private AuthorizeDTO authorizeDTO;

    @BeforeEach
    void setUp() {
        sender = User.builder()
                .id(UUID.randomUUID())
                .roles(Set.of(Role.builder().name(RoleEnum.COMMON).build()))
                .email("sender@email.com")
                .cpf("7654321")
                .password("password")
                .balance(BigDecimal.valueOf(100))
                .build();

        receiver = User.builder()
                .id(UUID.randomUUID())
                .roles(Set.of(Role.builder().name(RoleEnum.SHOPKEEPER).build()))
                .email("receiver@email.com")
                .cpf("1234567")
                .password("password")
                .balance(BigDecimal.valueOf(50))
                .build();

        createTransferRequest = new CreateTransferRequest(BigDecimal.TEN, sender.getId().toString(), receiver.getId().toString());

        DataDTO dataDTO = DataDTO.builder().authorization(true).build();
        authorizeDTO = AuthorizeDTO.builder().data(dataDTO).status("SUCCESS").build();
    }

    @Test
    void createTransfer_ShouldSucceed_WhenValidRequest() throws Exception {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(authorizeClient.getAuthorize()).thenReturn(authorizeDTO);

        transferService.createTransfer(createTransferRequest);

        assertEquals(BigDecimal.valueOf(60), receiver.getBalance());
        assertEquals(BigDecimal.valueOf(90), sender.getBalance());

        verify(transferRepository).save(any(Transfer.class));
        verify(transferRepository, times(1)).save(any(Transfer.class));
        verify(userRepository).save(sender);
        verify(userRepository).save(receiver);
        verify(notificationService).notifyClient();
    }

    @Test
    void createTransfer_ShouldThrowNotFoundException_WhenSenderNotFound() {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.empty());

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> transferService.createTransfer(createTransferRequest));

        verify(userRepository, never()).findById(receiver.getId());
    }

    @Test
    void createTransfer_ShouldThrowNotFoundException_WhenReceiverNotFound() {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.empty());

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> transferService.createTransfer(createTransferRequest));

        verify(userRepository, times(1)).findById(sender.getId());
        verify(userRepository, times(1)).findById(receiver.getId());
    }

    @Test
    void createTransfer_ShouldThrowUnauthorizedException_WhenSenderIsShopkeeper() {
        sender.setRoles(Set.of(Role.builder().name(RoleEnum.SHOPKEEPER).build()));

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        assertThrows(UnauthorizedException.class, () -> transferService.createTransfer(createTransferRequest));
    }

    @Test
    void createTransfer_ShouldThrowInsufficientBalanceException_WhenSenderHasNoBalance() {
        sender.setBalance(BigDecimal.ZERO);

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        assertThrows(InsufficientBalanceException.class, () -> transferService.createTransfer(createTransferRequest));
    }

    @Test
    void createTransfer_ShouldThrowInsufficientBalanceException_WhenSenderHasNegativeBalance() {
        sender.setBalance(BigDecimal.valueOf(-10));

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        assertThrows(InsufficientBalanceException.class, () -> transferService.createTransfer(createTransferRequest));
    }

    @Test
    void createTransfer_ShouldThrowException_WhenAuthorizeClientFails() {
        DataDTO dataDTO = DataDTO.builder().authorization(false).build();
        authorizeDTO = AuthorizeDTO.builder().data(dataDTO).status("FAILURE").build();

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(authorizeClient.getAuthorize()).thenReturn(authorizeDTO);

        assertThrows(Exception.class, () -> transferService.createTransfer(createTransferRequest));
    }
}