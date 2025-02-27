package br.com.picpay_challenge.repository;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);

}
