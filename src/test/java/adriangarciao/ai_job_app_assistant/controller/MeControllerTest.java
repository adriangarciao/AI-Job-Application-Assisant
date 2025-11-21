package adriangarciao.ai_job_app_assistant.controller;

import adriangarciao.ai_job_app_assistant.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private adriangarciao.ai_job_app_assistant.repository.UserRepository userRepository;
    @MockBean
    private adriangarciao.ai_job_app_assistant.service.JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getMe_returnsOk() throws Exception {
        var user = Mockito.mock(adriangarciao.ai_job_app_assistant.model.User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("test@example.com");
        when(user.getName()).thenReturn("Test User");
        when(user.getRole()).thenReturn(Role.USER);
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

        org.springframework.security.core.Authentication auth = Mockito.mock(org.springframework.security.core.Authentication.class);
        Mockito.when(auth.getName()).thenReturn("test@example.com");

        mockMvc.perform(get("/api/me").principal(auth))
                .andExpect(status().isOk());
    }
}
