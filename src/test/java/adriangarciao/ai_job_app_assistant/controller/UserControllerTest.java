package adriangarciao.ai_job_app_assistant.controller;

import adriangarciao.ai_job_app_assistant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private adriangarciao.ai_job_app_assistant.repository.UserRepository userRepository;
    @MockBean
    private adriangarciao.ai_job_app_assistant.service.JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserById_returnsOk() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(Mockito.mock(adriangarciao.ai_job_app_assistant.dto.UserDTO.class));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }
}
