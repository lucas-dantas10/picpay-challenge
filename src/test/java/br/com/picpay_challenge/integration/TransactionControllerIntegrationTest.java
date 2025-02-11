package br.com.picpay_challenge.integration;

import br.com.picpay_challenge.dto.CreateTransferRequest;
import br.com.picpay_challenge.entity.Role;
import br.com.picpay_challenge.entity.User;
import br.com.picpay_challenge.enums.RoleEnum;
import br.com.picpay_challenge.factory.RoleFactory;
import br.com.picpay_challenge.factory.UserFactory;
import br.com.picpay_challenge.repository.RoleRepository;
import br.com.picpay_challenge.repository.TransferRepository;
import br.com.picpay_challenge.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.yaml")
@ExtendWith(SpringExtension.class)
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User userCommon;

    private User userShopkeeper;

    private static final String URL_TRANSFER = "/transfer";
    @Autowired
    private TransferRepository transferRepository;

    @BeforeEach
    void setUp() {
        Role roleCommon = RoleFactory.createRole(RoleEnum.COMMON);
        Role roleShopkeeper = RoleFactory.createRole(RoleEnum.SHOPKEEPER);
        roleRepository.save(roleCommon);
        roleRepository.save(roleShopkeeper);

        this.userCommon = UserFactory.createValidUser(roleCommon);
        this.userShopkeeper = UserFactory.createValidUser(roleShopkeeper);
        userRepository.save(userCommon);
        userRepository.save(userShopkeeper);
    }

    @AfterEach
    void tearDown() {
        transferRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "Happy Path Test: create transaction and return status created")
    public void givenCorrectResponseTransaction_whenCreateTransaction_thenReturnStatusCreated() throws Exception {
        CreateTransferRequest request = new CreateTransferRequest(new BigDecimal("100.50"),
                userCommon.getId().toString(),
                userShopkeeper.getId().toString());

        String jsonPayload = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                post(URL_TRANSFER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName(value = "Unhappy Path Test: create transaction as a shopkeeper and return status unauthorized")
    public void givenResponseError_whenSendTransferAsShopkeeper_thenReturnStatusUnauthorized() throws Exception {
        CreateTransferRequest request = new CreateTransferRequest(new BigDecimal("100.50"),
                userShopkeeper.getId().toString(),
                userCommon.getId().toString());

        String jsonPayload = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(
                post(URL_TRANSFER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isUnauthorized());
    }
}
