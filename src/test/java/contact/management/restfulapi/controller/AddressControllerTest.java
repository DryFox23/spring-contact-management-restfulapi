package contact.management.restfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import contact.management.restfulapi.entity.Address;
import contact.management.restfulapi.entity.Contact;
import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.AddressResponse;
import contact.management.restfulapi.model.CreateAddressRequest;
import contact.management.restfulapi.model.WebResponse;
import contact.management.restfulapi.repository.AddressRepository;
import contact.management.restfulapi.repository.ContactRepository;
import contact.management.restfulapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test username");
        user.setPassword(BCrypt.hashpw("test password", BCrypt.gensalt()));
        user.setName("test name");
        user.setToken("test token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("testId");
        contact.setUser(user);
        contact.setFirstName("Test Username");
        contact.setLastName("Test Last Name");
        contact.setEmail("user@example.com");
        contact.setPhone("123456");
        contactRepository.save(contact);
    }

    @Test
    void createAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(post("/api/contacts/testId/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });
                    assertNotNull(response.getError());
                });

    }

    @Test
    void createAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCity("Test City");
        request.setStreet("Test Street");
        request.setProvince("Test Province");
        request.setCountry("Test Country");
        request.setPostalCode("12345");

        mockMvc.perform(post("/api/contacts/testId/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<AddressResponse>>() {
                            });
                    assertNull(response.getError());
                    assertEquals("Test City", response.getData().getCity());
                    assertEquals("Test Street", response.getData().getStreet());
                    assertEquals("Test Province", response.getData().getProvince());
                    assertEquals("Test Country", response.getData().getCountry());
                    assertEquals("12345", response.getData().getPostalCode());
                    assertNotNull(response.getData().getId());
                    assertTrue(addressRepository.existsById(response.getData().getId()));
                });
    }

    // Unit test for get address

    @Test
    void getAddressFailed() throws Exception {
        mockMvc.perform(get("/api/contacts/testId/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test token"))
                .andExpectAll(status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getError());
                });
    }

    @Test
    void getAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("testId").orElseThrow();

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet("Test Street");
        address.setCity("Test City");
        address.setProvince("Test Province");
        address.setCountry("Test Country");
        address.setPostalCode("12345");
        addressRepository.save(address);

        mockMvc.perform(get("/api/contacts/testId/addresses/" + address.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test token"))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<AddressResponse>>() {
                            });
                    assertNull(response.getError());
                    assertEquals(address.getId(), response.getData().getId());
                    assertEquals(address.getStreet(), response.getData().getStreet());
                    assertEquals(address.getCity(), response.getData().getCity());
                    assertEquals(address.getProvince(), response.getData().getProvince());
                    assertEquals(address.getCountry(), response.getData().getCountry());
                    assertEquals(address.getPostalCode(), response.getData().getPostalCode());
                    assertTrue(addressRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void deleteAddressFailed() throws Exception {
        mockMvc.perform(delete("/api/contacts/testId/addresses/123")
                .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "test token"))
                .andExpectAll(status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNotNull(response.getError());
                    assertFalse(addressRepository.existsById("123"));
                });
    }

    @Test
    void deleteAddressSucces() throws Exception {
        Contact contact = contactRepository.findById("testId").orElseThrow();

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet("Test Street");
        address.setCity("Test City");
        address.setProvince("Test Province");
        address.setCountry("Test Country");
        address.setPostalCode("12345");
        addressRepository.save(address);

        mockMvc.perform(delete("/api/contacts/testId/addresses/" + address.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test token"))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNull(response.getError());
                    assertEquals("Address successfully deleted", response.getData());
                    assertFalse(addressRepository.existsById(address.getId()));
                });
    }
}
