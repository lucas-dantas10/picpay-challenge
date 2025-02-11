package br.com.picpay_challenge.factory;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.enums.RoleEnum;

public class RoleFactory {

    public static Role createRole(RoleEnum role) {
        return Role.builder()
                .name(role)
                .build();
    }
}
