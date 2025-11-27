package adriangarciao.ai_job_app_assistant.config;

import adriangarciao.ai_job_app_assistant.service.ai.llm.FakeLLMService;
import adriangarciao.ai_job_app_assistant.service.ai.llm.LLMService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public LLMService llmService() {
        return new FakeLLMService();
    }
}
