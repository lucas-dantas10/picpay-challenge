package br.com.picpay_challenge.repository;

import br.com.picpay_challenge.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
