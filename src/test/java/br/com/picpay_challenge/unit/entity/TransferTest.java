package br.com.picpay_challenge.unit.entity;

import br.com.picpay_challenge.entity.Transfer;
import br.com.picpay_challenge.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    @Test
    @DisplayName(value = "Happy Path Test: create entity with correct values")
    public void testTransferEntity() {
        User receiver = new User();
        receiver.setId(UUID.randomUUID());

        User sender = new User();
        sender.setId(UUID.randomUUID());

        BigDecimal amount = new BigDecimal("100.00");
        LocalDateTime now = LocalDateTime.now();

        Transfer transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setAmount(amount);
        transfer.setTransferDate(now);

        assertEquals(receiver, transfer.getReceiver());
        assertEquals(sender, transfer.getSender());
        assertEquals(amount, transfer.getAmount());
        assertEquals(now, transfer.getTransferDate());
    }
}