package ua.com.alevel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.com.alevel.persistence.type.ProductCategory;
import ua.com.alevel.test_entity.TestBasket;
import ua.com.alevel.test_entity.TestOffer;
import ua.com.alevel.test_entity.TestProduct;
import ua.com.alevel.test_entity.TestUser;
import ua.com.alevel.web.dto.request.BasketRequestDto;
import ua.com.alevel.web.dto.request.OfferRequestDto;
import ua.com.alevel.web.dto.request.ProductRequestDto;
import ua.com.alevel.web.dto.request.UserRequestDto;
import ua.com.alevel.web.dto.response.ProductResponseDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String authTokenAdmin = "";
    private String authTokenUser = "";

    private static int COUNT_PRODUCT = 10;

    @Test
    @Order(1)
    @Sql(value = {"/set-user-role.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreateUser() throws Exception {
//        ADMIN
        UserRequestDto admin = getUserREQ("admin", "+34666666666");
        System.out.println("Create admin: " + admin);

        // Преобразуем объект пользователя в JSON
        String adminJson = objectMapper.writeValueAsString(admin);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isOk()); // Check status 200
        System.out.println("Admin was created, and he is given the role of ADMIN");

//        USER
        UserRequestDto user = getUserREQ("user", "+34111111111");
        System.out.println("Create user: " + user);

        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk()); // Check status 200
        System.out.println("User was created, and he is given the role of USER");


        System.out.println("Attempt to create a user with the same email");
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().is(409)); // Check status 409(if user with email exist in DB)
        System.out.println("Error, status 409");
    }

    @Test
    @Order(2)
    void testLoginUser() throws Exception {

//        ADMIN
        UserRequestDto admin = new UserRequestDto();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("123123123");

        System.out.println("Attempt to authorize the admin: " + admin);
        String adminJson = objectMapper.writeValueAsString(admin);
        MvcResult adminResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isOk())
                .andReturn();
        this.authTokenAdmin = extractJwtToken(adminResult.getResponse().getContentAsString());
        System.out.println("DONE, admin JWTToken is: " + this.authTokenAdmin);


//        USER
        UserRequestDto user = new UserRequestDto();
        user.setEmail("user@gmail.com");
        user.setPassword("123123123");
        System.out.println("Attempt to authorize the user: " + user);
        String userJson = objectMapper.writeValueAsString(user);
        MvcResult userResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();
        this.authTokenUser = extractJwtToken(userResult.getResponse().getContentAsString());
        System.out.println("DONE, user JWTToken is: " + this.authTokenUser);


        System.out.println("Attempt to authorize a user with an invalid password");
        admin.setPassword("t" + admin.getPassword());
        userJson = objectMapper.writeValueAsString(admin);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(401));
        System.out.println("Error, status 401");

    }

    @Test
    @Order(3)
    void testGetActualUser() throws Exception {
        System.out.println("Getting a admin by his auth-token");
        MvcResult adminResult = mockMvc.perform(get("/auth/user/getUserByToken")
                        .header("Authorization", this.authTokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String adminResponseContent = adminResult.getResponse().getContentAsString();
        TestUser admin = objectMapper.readValue(adminResponseContent, TestUser.class);

        assertEquals(Optional.of(1L).get(), admin.getId());
        assertEquals("admin@gmail.com", admin.getEmail());
        assertEquals("admin", admin.getFirstName());
        assertEquals("admin", admin.getLastName());
        assertEquals("+34666666666", admin.getPhoneNumber());
        assertEquals("ADMIN", admin.getRole());
        assertTrue(admin.getEnabled());

        System.out.println("DONE");


        System.out.println("Getting a user by his auth-token");
        MvcResult userResult = mockMvc.perform(get("/auth/user/getUserByToken")
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String userResponseContent = userResult.getResponse().getContentAsString();
        TestUser user = objectMapper.readValue(userResponseContent, TestUser.class);

        assertEquals(Optional.of(2L).get(), user.getId());
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("user", user.getFirstName());
        assertEquals("user", user.getLastName());
        assertEquals("+34111111111", user.getPhoneNumber());
        assertEquals("USER", user.getRole());
        assertTrue(user.getEnabled());
        System.out.println("DONE");


        System.out.println("Attempt to get a user with an invalid jwt-token");
        mockMvc.perform(get("/auth/user/getUserByToken")
                        .header("Authorization", "123" + this.authTokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        System.out.println("Error, status 400");
    }

    @Test
    @Order(4)
    void testCreateNewProduct() throws Exception {
        String productConfig = "IPhone";
        List<ProductRequestDto> requestList = getProductsListReq(COUNT_PRODUCT, productConfig);

        System.out.println("Attempt to create a " + COUNT_PRODUCT + " products with the ADMIN role");

        List<TestProduct> resultList = new ArrayList<>();
        for (ProductRequestDto request : requestList) {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/products/create")
                            .header("Authorization", this.authTokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andReturn();
            String responseContent = result.getResponse().getContentAsString();
            TestProduct product = objectMapper.readValue(responseContent, TestProduct.class);
            resultList.add(product);
            System.out.println("Product with ID: " + product.getId() + " was created");
        }

        assertEquals(COUNT_PRODUCT, resultList.size());
        System.out.println("All " + COUNT_PRODUCT + " products have been created");

        System.out.println("Attempt to create a product with the USER role");
        mockMvc.perform(MockMvcRequestBuilders.post("/products/create")
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resultList.get(1))))
                .andExpect(status().is(403));
        System.out.println("Error, status 403");
    }


    @Test
    @Order(5)
    public void testFindProductById() throws Exception {
        System.out.println("Trying to find a product with the correct id: " + 1);
        MvcResult result = mockMvc.perform(get("/products/product/" + 1))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        TestProduct product = objectMapper.readValue(responseContent, TestProduct.class);
        assertEquals(Optional.of(1L).get(), product.getId());

        System.out.println("Trying to find a product with the incorrect id: " + COUNT_PRODUCT + 1);
        mockMvc.perform(get("/products/product/" + COUNT_PRODUCT + 1))
                .andExpect(status().is(404));
        System.out.println("Error, status 404");
    }

    @Test
    @Order(6)
    public void testFindAllProductsAndUpdate() throws Exception {
        List<TestProduct> allProducts = findAllProduct();
        assertEquals(COUNT_PRODUCT, allProducts.size());
        for (TestProduct product : allProducts) {
            System.out.println("Update product with title '" + product.getTitle()
                    + "' to update product '" + product.getTitle() + " update'");
            product.setTitle(product.getTitle() + "update");
            product.setDescription(product.getDescription() + "update");
            product.setPrice(product.getPrice().add(BigDecimal.valueOf(1000)));
            product.setImage(product.getImage() + "update");
            product.setYear(product.getYear() + 1000);
            product.setQuantityInWarehouse(product.getQuantityInWarehouse() + 1000);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/products/update")
                            .header("Authorization", this.authTokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(product)))
                    .andExpect(status().isOk())
                    .andReturn();
            String responseContent = result.getResponse().getContentAsString();
            TestProduct responseProduct = objectMapper.readValue(responseContent, TestProduct.class);
            assertEquals(product.getTitle(), responseProduct.getTitle());
            assertEquals(product.getImage(), responseProduct.getImage());
            assertEquals(product.getDescription(), responseProduct.getDescription());
            assertEquals(product.getPrice(), responseProduct.getPrice());
            assertEquals(product.getYear(), responseProduct.getYear());
            assertEquals(product.getQuantityInWarehouse(), responseProduct.getQuantityInWarehouse());

            System.out.println("DONE");
        }
    }


    @Test
    @Order(7)
    public void testDeleteProduct() throws Exception {
        System.out.println("Attempt to delete a product with a Role.User");
        mockMvc.perform(delete("/products/delete")
                        .header("Authorization", this.authTokenUser)
                        .header("productId", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));
        System.out.println("Error, status 403");

        System.out.println("Attempt to delete a product with a incorrect productId");
        mockMvc.perform(delete("/products/delete")
                        .header("Authorization", this.authTokenAdmin)
                        .header("productId", 321)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
        System.out.println("Error, status 404");

        System.out.println("Attempt to delete the first 5 products");
        for (int i = 1; i <= 5; i++) {
            mockMvc.perform(delete("/products/delete")
                            .header("Authorization", this.authTokenAdmin)
                            .header("productId", i)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
        COUNT_PRODUCT -= 5;
        System.out.println("Done");
    }

    @Test
    @Order(8)
    public void testAddProductToUserBasket() throws Exception {
        List<TestProduct> allProducts = findAllProduct();
        BasketRequestDto basketRequestDto = new BasketRequestDto();
        basketRequestDto.setUserId(2L);
        for (TestProduct product : allProducts) {
            basketRequestDto.setProductId(product.getId());
            mockMvc.perform(MockMvcRequestBuilders.post("/basket/addProductToBasket")
                            .header("Authorization", this.authTokenUser)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(basketRequestDto)))
                    .andExpect(status().isOk());
        }

//        Request with incorrect ProductId
        basketRequestDto.setProductId(123L);
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/addProductToBasket")
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(basketRequestDto)))
                .andExpect(status().is(404))
                .andReturn();

//        Request with incorrect JWTToken
        basketRequestDto.setProductId(6L);
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/addProductToBasket")
                        .header("Authorization", "123225v32v25v23452ergvegf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(basketRequestDto)))
                .andExpect(status().is(400))
                .andReturn();

//        Request with incorrect UserId
        basketRequestDto.setUserId(111L);
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/addProductToBasket")
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(basketRequestDto)))
                .andExpect(status().is(404))
                .andReturn();

//      If a user tries to add an item to another person's basket
        basketRequestDto.setUserId(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/addProductToBasket")
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(basketRequestDto)))
                .andExpect(status().is(403))
                .andReturn();

//      If a admin tries to add an item to another person's basket
        basketRequestDto.setUserId(2L);
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/addProductToBasket")
                        .header("Authorization", this.authTokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(basketRequestDto)))
                .andExpect(status().is(200))
                .andReturn();

        assertEquals(COUNT_PRODUCT, findProductsInUserBasket().size());
    }

    @Test
    @Order(9)
    public void deleteAllProductFromBasket() throws Exception{
        BasketRequestDto basketRequestDto = new BasketRequestDto();
        basketRequestDto.setUserId(2L);
        basketRequestDto.setProductId(6L);
        mockMvc.perform(MockMvcRequestBuilders.post("/basket/deleteAllProductFromBasket")
                        .header("Authorization", this.authTokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(basketRequestDto)))
                .andExpect(status().is(200))
                .andReturn();
        assertEquals(COUNT_PRODUCT - 1, findProductsInUserBasket().size());
    }

    @Test
    @Order(10)
    public void createOffer() throws Exception{
        OfferRequestDto offerRequestDto = new OfferRequestDto();
        offerRequestDto.setUserId(2L);
        offerRequestDto.setAddress("user_address");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/offers/createOffer")
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerRequestDto)))
                .andExpect(status().is(200))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        TestOffer offer = objectMapper.readValue(content, TestOffer.class);

        assertEquals(Optional.of(1L).get(), offer.getId());
        assertEquals("awaitingPayment", offer.getStatus());
        assertEquals("user_address", offer.getAddress());
    }

    @Test
    @Order(11)
    @Sql(value = {"/drop-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void paidOffer() throws Exception{
        TestOffer testOffer = new TestOffer();
        testOffer.setId(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/offers/successfulPayment")
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOffer)))
                .andExpect(status().is(200));
    }
    private List<TestBasket> findProductsInUserBasket() throws Exception {
        MvcResult result = mockMvc.perform(get("/basket/findAllProductByUserBasket/" + 2)
                        .header("Authorization", this.authTokenUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<List<TestBasket>>() {
        });
    }


    private List<TestProduct> findAllProduct() throws Exception {
        System.out.println("Find all(" + COUNT_PRODUCT + ") products");
        MvcResult result = mockMvc.perform(get("/products/findAll"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<List<TestProduct>>() {
        });
    }

    private String extractJwtToken(String input) {
        return "Bearer " + input.substring(input.indexOf(':') + 2, input.lastIndexOf('}') - 1);
    }

    private UserRequestDto getUserREQ(String config, String phoneNumber) {
        UserRequestDto userReq = new UserRequestDto();
        userReq.setFirstName(config);
        userReq.setLastName(config);
        userReq.setPhoneNumber(phoneNumber);
        userReq.setEmail(config + "@gmail.com");
        userReq.setPassword("123123123");
        return userReq;
    }

    private List<ProductRequestDto> getProductsListReq(Integer count, String config) {
        config = " " + config;
        List<ProductRequestDto> resultList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ProductRequestDto productRequestDto = new ProductRequestDto();
            productRequestDto.setTitle(config + i);
            productRequestDto.setPrice(BigDecimal.valueOf(1499 + i));
            productRequestDto.setYear(2023 + i);
            productRequestDto.setCategory(ProductCategory.smartphones.toString());
            productRequestDto.setDescription(config);
            productRequestDto.setImage(config);
            productRequestDto.setQuantityInWarehouse((long) i);
            resultList.add(productRequestDto);
        }

        return resultList;
    }
}
