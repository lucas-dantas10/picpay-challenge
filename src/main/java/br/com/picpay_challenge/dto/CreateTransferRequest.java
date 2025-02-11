package br.com.picpay_challenge.dto;

import java.math.BigDecimal;

public record CreateTransferRequest(BigDecimal value, String payer, String payee) {
}
