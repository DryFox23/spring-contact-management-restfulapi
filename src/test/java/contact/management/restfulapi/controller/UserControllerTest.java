package contact.management.restfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import contact.management.restfulapi.entity.User;
import contact.management.restfulapi.model.RegisterUserRequest;
import contact.management.restfulapi.model.UpdateUserRequest;
import contact.management.restfulapi.model.UserResponse;
import contact.management.restfulapi.model.WebResponse;
import contact.management.restfulapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerSucces() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("password");
        request.setName("Test User");

        mockMvc.perform(post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    assertEquals("success", response.getData());
                });
    }

    @Test
    void registerBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    assertNotNull(response.getError());
                });
    }

    @Test
    void registerDuplicate() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("Test User");
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("password");
        request.setName("Test User");

        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });

                    assertNotNull((response.getError()));
                });
    }

    @Test
    void getUserUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "invalid-token")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull((response.getError()));
        });
    }

    @Test
    void getUserUnauthorizedNotFound() throws Exception {
        mockMvc.perform(get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull((response.getError()));
        });
    }

    @Test
    void getUserSucces() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setUsername("test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000L);
        userRepository.save(user);

        mockMvc.perform(get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
            });

            assertNull(response.getError());
            assertEquals("test", response.getData().getUsername());
            assertEquals("Test User", response.getData().getName());
        });
    }

    @Test
    void getUserFiledTokenExpired() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setUsername("test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis() - 10000000L);
        userRepository.save(user);

        mockMvc.perform(get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
            });

            assertNotNull(response.getError());
        });
    }

    // update user test
    @Test
    void updateUserUnauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();

        mockMvc.perform(patch("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull((response.getError()));
        });
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setName("Test User");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000L);
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Test User Updated");
        request.setPassword("newPw");

        mockMvc.perform(patch("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull((response.getError()));
            assertEquals("Test User Updated", response.getData().getName());
            assertEquals("test", response.getData().getUsername());

            User userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertTrue(BCrypt.checkpw("newPw", userDb.getPassword()));
        });
    }
}
