package br.com.picpay_challenge.factory;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;

import java.util.Set;
import java.util.UUID;

public class UserFactory {

    public static User createValidUser(Role role) {
        if (role.getName().equals(RoleEnum.SHOPKEEPER)) {
            return User.builder()
                    .roles(Set.of(role))
                    .fullname("Shopkeeper User")
                    .cpf("8888888888")
                    .email("shopkeeper@example.com")
                    .password("12345")
                    .build();
        }

        return User.builder()
                .roles(Set.of(role))
                .fullname("Common User")
                .cpf("9999999999")
                .email("common@example.com")
                .password("1234")
                .build();
    }
}
