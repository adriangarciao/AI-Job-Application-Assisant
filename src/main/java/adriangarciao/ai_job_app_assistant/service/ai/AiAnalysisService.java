package adriangarciao.ai_job_app_assistant.service.ai;
import adriangarciao.ai_job_app_assistant.dto.*;
import adriangarciao.ai_job_app_assistant.service.ai.llm.LLMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class AiAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(AiAnalysisService.class);
    private final ParserService parserService;
    private final LLMService llmService;
    public AiAnalysisService(ParserService parserService, LLMService llmService) {
        this.parserService = Objects.requireNonNull(parserService, "parserService");
        this.llmService = Objects.requireNonNull(llmService, "llmService");
    }
    public FeedbackDTO analyze(SubmitAnalysisRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        String resumeText = request.resumeText();
        String jobPostingText = request.jobPostingText();
        if (resumeText == null) resumeText = "";
        if (jobPostingText == null) jobPostingText = "";
        ParsedResumeDTO parsedResume = parserService.parseResume(resumeText);
        ParsedJobDTO parsedJob = parserService.parseJob(jobPostingText);
        boolean includeCoverLetter = request.includeCoverLetter();
        log.debug("Analyzing resume (len={}) against job (len={}), includeCoverLetter={}",
                parsedResume.rawText().length(), parsedJob.rawText().length(), includeCoverLetter);
        return llmService.generateFeedback(parsedResume, parsedJob, includeCoverLetter);
    }
}