package adriangarciao.ai_job_app_assistant.controller;

import adriangarciao.ai_job_app_assistant.service.ResumeService;
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

@WebMvcTest(ResumeController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResumeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ResumeService resumeService;
    @MockBean
    private adriangarciao.ai_job_app_assistant.repository.UserRepository userRepository;
    @MockBean
    private adriangarciao.ai_job_app_assistant.service.JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getResumeById_returnsOk() throws Exception {
        when(resumeService.get(anyLong())).thenReturn(Mockito.mock(adriangarciao.ai_job_app_assistant.dto.ResumeDTO.class));
        mockMvc.perform(get("/api/resumes/1"))
                .andExpect(status().isOk());
    }
}
