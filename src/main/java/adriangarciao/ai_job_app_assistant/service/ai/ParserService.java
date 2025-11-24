package adriangarciao.ai_job_app_assistant.service.ai;

import adriangarciao.ai_job_app_assistant.dto.ParsedJobDTO;
import adriangarciao.ai_job_app_assistant.dto.ParsedResumeDTO;

/**
 * ParserService transforms raw resume and job posting text into internal structured DTOs.
 */
public interface ParserService {
    ParsedResumeDTO parseResume(String resumeText);

    ParsedJobDTO parseJob(String jobPostingText);
}
