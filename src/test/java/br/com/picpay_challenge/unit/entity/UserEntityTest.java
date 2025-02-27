package br.com.picpay_challenge.unit.entity;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEntityTest {

    @Test
    @DisplayName(value = "Happy Path Test: create entity with correct values")
    public void testUserEntity() {
        Role role = Role.builder()
                .name(RoleEnum.COMMON)
                .build();

        Set<Role> roles = Set.of(role);
        String fullname = "Common User";
        String cpf = "9999999999";
        String email = "common@example.com";
        String password = "12345";
        User user = User.builder()
                .roles(roles)
                .fullname(fullname)
                .cpf(cpf)
                .balance(BigDecimal.valueOf(100))
                .email(email)
                .password(password)
                .build();

        assertEquals(fullname, user.getFullname());
        assertEquals(cpf, user.getCpf());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(roles, user.getRoles());
    }
}
