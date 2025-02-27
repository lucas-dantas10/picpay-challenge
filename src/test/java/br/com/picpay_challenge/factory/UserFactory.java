package br.com.picpay_challenge.factory;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.User;

import java.math.BigDecimal;
import java.util.Set;

public class UserFactory {

    public static User createValidUser(Role role,
                                       String cpf,
                                       String email,
                                       BigDecimal balance) {
        return User.builder()
                .roles(Set.of(role))
                .fullname("User")
                .cpf(cpf)
                .email(email)
                .password("1234")
                .balance(balance)
                .build();
    }
}
