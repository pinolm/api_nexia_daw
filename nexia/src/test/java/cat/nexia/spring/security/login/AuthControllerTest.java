package cat.nexia.spring.security.login;

import cat.nexia.spring.SpringBootNexiaApplication;
import cat.nexia.spring.controllers.AuthController;
import cat.nexia.spring.dto.request.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

//TEST PROVA TEA3
@SpringBootTest(classes = SpringBootNexiaApplication.class)
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    /**
     * Test the login functionality in the authentication controller.
     *
     * @throws Exception If an error occurs during testing.
     */
    @Test
    public void testLogin() throws Exception {

        // modify the value "X" to the actual value
        String username = "X";
        String password = "X";

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username));
    }

    /**
     * Converts an object to a JSON representation.
     *
     * @param obj The object to convert to JSON.
     * @return A JSON representation of the object.
     */
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
