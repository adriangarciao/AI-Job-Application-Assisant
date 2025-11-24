package adriangarciao.ai_job_app_assistant.service.ai.llm;

import adriangarciao.ai_job_app_assistant.dto.FeedbackDTO;
import adriangarciao.ai_job_app_assistant.dto.ParsedJobDTO;
import adriangarciao.ai_job_app_assistant.dto.ParsedResumeDTO;

/**
 * Abstraction for generating AI-style feedback from parsed resume and job posting.
 */
public interface LLMService {
    FeedbackDTO generateFeedback(ParsedResumeDTO resume, ParsedJobDTO job, boolean includeCoverLetter);
}
