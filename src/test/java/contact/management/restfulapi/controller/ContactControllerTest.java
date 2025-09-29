package contact.management.restfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import contact.management.restfulapi.entity.Contact;
import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.ContactResponse;
import contact.management.restfulapi.model.CreateContactRequest;
import contact.management.restfulapi.model.WebResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Menghapus semua data pada tabel contact dan user sebelum setiap pengujian
        contactRepository.deleteAll();
        userRepository.deleteAll();
        // Menambahkan user untuk keperluan testing

        User user = new User();
        user.setUsername("test username");
        user.setPassword(BCrypt.hashpw("test password", BCrypt.gensalt()));
        user.setName("test name");
        user.setToken("test token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salah email");

        mockMvc.perform(post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test token")
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getError());
                });
    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Test First Name");
        request.setLastName("Test Last Name");
        request.setEmail("test@example.com");
        request.setPhone("1234567890");

        mockMvc.perform(post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test token")
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
                    });
                    assertNull(response.getError());
                    assertNotNull(response.getData());
                    assertEquals("Test First Name", response.getData().getFirstName());
                    assertEquals("Test Last Name", response.getData().getLastName());
                    assertEquals("test@example.com", response.getData().getEmail());
                    assertEquals("1234567890", response.getData().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getId()));
                });
    }


    // get contact by id test case
    @Test
    void getContactFailedNotFound() throws Exception {
        mockMvc.perform(get("/api/contacts/1231")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test token"))
                .andExpectAll(status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getError());
                });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepository.findById("test username").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("Test First Name");
        contact.setLastName("Test Last Name");
        contact.setEmail("test@example.com");
        contact.setPhone("1234567890");
        contactRepository.save(contact);

        mockMvc.perform(get("/api/contacts/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "test token"))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
                    });

                    assertNull(response.getError());
                    assertEquals(contact.getId(), response.getData().getId());
                    assertEquals(contact.getFirstName(), response.getData().getFirstName());
                    assertEquals(contact.getLastName(), response.getData().getLastName());
                    assertEquals(contact.getEmail(), response.getData().getEmail());
                    assertEquals(contact.getPhone(), response.getData().getPhone());
                });
    }
}
