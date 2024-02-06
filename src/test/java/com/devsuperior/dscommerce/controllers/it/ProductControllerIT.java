package com.devsuperior.dscommerce.controllers.it;

import com.devsuperior.dscommerce.TokenUtil;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;
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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper mapper;

    private Long existingId, nonExistingId;
    private String clientUsername, clientPassword, adminUsername, adminPassword, productName, adminToken, clientToken, invalidToken;
    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        productName = "macbook";
        existingId = 1L;
        nonExistingId = 1000L;
        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";
        product = new Product(null, "PlayStation 5", "Lorem Ipsum Dolor sit amet", 3999.90, "imgUrl");
        Category category = new Category(2L, "Eletrônicos");
        product.getCategories().add(category);
        productDTO = new ProductDTO(product);

        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "xpto"; // Simulates wrong token
    }

    @Test
    public void findAllShouldReturnPagedProductDTOWhenNameParamIsNotEmpty() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/products?name={productName}", productName)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    }

    @Test
    public void findAllShouldReturnPagedProductDTOWhenNameParamIsEmpty() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/products")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
    }

    @Test
    public void insertShouldReturnProductDTOCreatedWhenAdminLoggedAndValidData() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedImgUrl = productDTO.getImgUrl();

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.imgUrl").value(expectedImgUrl));
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {
        product.setName("ab");
        productDTO = new ProductDTO(product);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/products")
                                .header("Authorization", "Bearer " + adminToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDescription() throws Exception {
        product.setDescription("ab");
        productDTO = new ProductDTO(product);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/products")
                                .header("Authorization", "Bearer " + adminToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndNegativePrice() throws Exception {
        product.setPrice(-200.0);
        productDTO = new ProductDTO(product);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/products")
                                .header("Authorization", "Bearer " + adminToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {
        product.setPrice(0.0);
        productDTO = new ProductDTO(product);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/products")
                                .header("Authorization", "Bearer " + adminToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndNoCategory() throws Exception {
        product.getCategories().clear();
        productDTO = new ProductDTO(product);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/products")
                                .header("Authorization", "Bearer " + adminToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/products")
                                .header("Authorization", "Bearer " + clientToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenNoUserLogged() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }
 }
