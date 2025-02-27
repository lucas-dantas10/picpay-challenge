package br.com.picpay_challenge.unit.repository;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;
import br.com.picpay_challenge.repository.RoleRepository;
import br.com.picpay_challenge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        Role role = Role.builder()
                .name(RoleEnum.COMMON)
                .build();

        roleRepository.save(role);
        this.role = role;
    }

    @Test
    void findById() {
        Set<Role> roles = Set.of(this.role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        entityManager.persist(user);
        entityManager.flush();


        List<User> userTest = userRepository.findAll();

        Optional<User> foundUser = userRepository.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void findByEmail() {
        Set<Role> roles = Set.of(this.role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByEmail(email);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void findByIdWithIncorretId() {
        Set<Role> roles = Set.of(this.role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findById(UUID.randomUUID());

        assertFalse(foundUser.isPresent());
    }

    @Test
    void findByEmailWithIncorretEmail() {
        Set<Role> roles = Set.of(this.role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByEmail("incorret.email@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    public void createUserWithDuplicateEmailThrowsException() {
        Set<Role> roles = Set.of(this.role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        entityManager.persist(user);
        entityManager.flush();

        User user2 = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf("0987654321")
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(user2));
    }

    @Test
    public void createUserWithDuplicateCpfThrowsException() {
        Set<Role> roles = Set.of(this.role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        entityManager.persist(user);
        entityManager.flush();

        User user2 = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email("email.different@example.com")
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(user2));
    }

    @Test
    void createUserWithRoles() {
        Set<Role> roles = Set.of(this.role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .email(email)
                .balance(BigDecimal.valueOf(100))
                .password(password)
                .build();

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(roles.size(), foundUser.get().getRoles().size());
    }
}
