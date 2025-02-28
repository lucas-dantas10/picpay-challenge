package br.com.picpay_challenge.dto;

import lombok.Builder;

@Builder
public record AuthorizeDTO(String status, DataDTO data) {
}
