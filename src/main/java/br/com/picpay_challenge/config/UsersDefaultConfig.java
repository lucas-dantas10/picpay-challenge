package br.com.picpay_challenge.config;

import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;
import br.com.picpay_challenge.repository.RoleRepository;
import br.com.picpay_challenge.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UsersDefaultConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private Role roleCommon;
    private Role roleShop;
    private User userCommon;
    private User userShop;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Optional<Role> roleCommon = roleRepository.findByName(RoleEnum.COMMON);
        Optional<Role> roleShopkeeper = roleRepository.findByName(RoleEnum.SHOPKEEPER);

        roleCommon.ifPresentOrElse(
                role -> log.info("Role ja existe {}", role.getName()),
                () -> {
                    Role role = Role.builder()
                            .name(RoleEnum.valueOf(RoleEnum.COMMON.name()))
                            .build();

                    roleRepository.save(role);


                    this.roleCommon = role;
                }
        );

        roleShopkeeper.ifPresentOrElse(
                role -> log.info("Role ja existe {}", role.getName()),
                () -> {
                    Role role = Role.builder()
                            .name(RoleEnum.valueOf(RoleEnum.SHOPKEEPER.name()))
                            .build();

                    roleRepository.save(role);

                    this.roleShop = role;
                }
        );

        Optional<User> userCommon = userRepository.findByEmail("common@example.com");
        Optional<User> userShop = userRepository.findByEmail("shop@example.com");

        userCommon.ifPresentOrElse(
                userExistent -> log.info("usuario ja existe: {}", userExistent.getId()),
                () -> {
                    User newUser = User.builder()
                            .roles(Set.of(this.roleCommon))
                            .fullname("Common")
                            .cpf("123456")
                            .email("common@example.com")
                            .password("123456")
                            .balance(BigDecimal.valueOf(100))
                            .build();

                    userRepository.save(newUser);

                    this.userCommon = newUser;
                }
        );

        userShop.ifPresentOrElse(
                userExistent -> log.info("usuario ja existe: {}", userExistent.getId()),
                () -> {
                    User newUser = User.builder()
                            .roles(Set.of(this.roleShop))
                            .fullname("Shopkeeper")
                            .cpf("1234567")
                            .password("123456")
                            .email("shop@example.com")
                            .balance(BigDecimal.valueOf(100))
                            .build();

                    userRepository.save(newUser);

                    this.userShop = newUser;
                }
        );


        log.info("Usuários default criado com sucesso!");
        log.info("user common: {}", this.userCommon.getId());
        log.info("user shop: {}", this.userShop.getId());
    }
}
