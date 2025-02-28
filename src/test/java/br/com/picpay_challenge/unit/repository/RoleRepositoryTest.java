package br.com.picpay_challenge.unit.repository;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.enums.RoleEnum;
import br.com.picpay_challenge.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByNameWithCommonUser() {
        Role role = Role.builder()
                .name(RoleEnum.COMMON)
                .build();

        entityManager.persistAndFlush(role);

        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.COMMON);

        assertTrue(foundRole.isPresent());
        assertEquals(role, foundRole.get());
    }

    @Test
    void findByNameWithShopkeeperUser() {
        Role role = Role.builder()
                .name(RoleEnum.SHOPKEEPER)
                .build();

        entityManager.persistAndFlush(role);

        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.SHOPKEEPER);

        assertTrue(foundRole.isPresent());
        assertEquals(role, foundRole.get());
    }

    @Test
    void findByNameWithIncorretRoleName() {
        Role role = Role.builder()
                .name(RoleEnum.COMMON)
                .build();

        entityManager.persistAndFlush(role);

        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.SHOPKEEPER);

        assertFalse(foundRole.isPresent());
    }
}
