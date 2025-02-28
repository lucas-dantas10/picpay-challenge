package br.com.picpay_challenge.unit.entity;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.enums.RoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    @DisplayName(value = "Happy Path Test: create entity with correct values")
    public void testRoleCommonEntity() {
        Long id = 1L;
        RoleEnum roleName = RoleEnum.COMMON;

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        assertEquals(id, role.getId());
        assertEquals(roleName, role.getName());
    }

    @Test
    @DisplayName(value = "Happy Path Test: create entity with correct values")
    public void testRoleShopkeeperEntity() {
        Long id = 2L;
        RoleEnum roleName = RoleEnum.SHOPKEEPER;

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        assertEquals(id, role.getId());
        assertEquals(roleName, role.getName());
    }
}
