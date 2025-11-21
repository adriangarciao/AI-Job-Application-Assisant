package adriangarciao.ai_job_app_assistant.controller;

import adriangarciao.ai_job_app_assistant.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(ApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationService applicationService;
    @MockBean
    private adriangarciao.ai_job_app_assistant.repository.UserRepository userRepository;
    @MockBean
    private adriangarciao.ai_job_app_assistant.service.JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getApplicationById_returnsOk() throws Exception {
        when(applicationService.getOwned(anyLong(), anyLong())).thenReturn(Mockito.mock(adriangarciao.ai_job_app_assistant.dto.ApplicationDTO.class));
        var user = Mockito.mock(adriangarciao.ai_job_app_assistant.model.User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

        org.springframework.security.core.Authentication auth = Mockito.mock(org.springframework.security.core.Authentication.class);
        Mockito.when(auth.getName()).thenReturn("test@example.com");

        mockMvc.perform(get("/api/applications/1").principal(auth))
                .andExpect(status().isOk());
    }
}
