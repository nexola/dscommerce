package com.devsuperior.dscommerce.controllers.it;

import com.devsuperior.dscommerce.TokenUtil;
import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.*;
import com.devsuperior.dscommerce.factories.ProductFactory;
import com.devsuperior.dscommerce.factories.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper mapper;

    private Long existingOrderId, nonExistingOrderId;
    private String clientUsername, clientPassword, adminUsername, adminPassword, productName, adminToken, clientToken, invalidToken;
    private Order order;
    private OrderDTO orderDTO;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 1000L;
        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";

        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "xpto"; // Simulates wrong token

        user = UserFactory.createUserClient();
        order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);

        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/orders/{id}", existingOrderId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingOrderId));
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/orders/{id}", existingOrderId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingOrderId));
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenIdExistsAndClientLoggedAndOrderDoesNotBelongUser() throws Exception {
        Long otherOrderId = 2L;

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/orders/{id}", otherOrderId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/orders/{id}", nonExistingOrderId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndClientLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/orders/{id}", nonExistingOrderId)
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenNoUserLogged() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/orders/{id}", existingOrderId)
                .header("Authorization", "Bearer " + invalidToken)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnauthorized());
    }


}
