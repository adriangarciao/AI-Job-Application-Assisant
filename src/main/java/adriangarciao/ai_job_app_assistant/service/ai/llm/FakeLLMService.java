package adriangarciao.ai_job_app_assistant.service.ai.llm;

import adriangarciao.ai_job_app_assistant.dto.FeedbackDTO;
import adriangarciao.ai_job_app_assistant.dto.ParsedJobDTO;
import adriangarciao.ai_job_app_assistant.dto.ParsedResumeDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FakeLLMService implements LLMService {

    @Override
    public FeedbackDTO generateFeedback(ParsedResumeDTO resume, ParsedJobDTO job, boolean includeCoverLetter) {
        List<String> resumeSkills = resume == null ? List.of() : resume.skills();
        List<String> required = job == null ? List.of() : job.requiredSkills();

        Set<String> resumeLower = resumeSkills.stream()
                .filter(s -> s != null)
                .map(s -> s.toLowerCase(Locale.ROOT).trim())
                .collect(Collectors.toCollection(HashSet::new));

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String r : required) {
            if (r == null || r.isBlank()) continue;
            String rl = r.toLowerCase(Locale.ROOT).trim();
            if (resumeLower.contains(rl)) matched.add(r);
            else missing.add(r);
        }

        int reqCount = Math.max(required.size(), 1);
        int matches = matched.size();
        int matchScore = Math.toIntExact(Math.round((matches / (double) reqCount) * 100.0));
        matchScore = Math.max(0, Math.min(100, matchScore));

        List<String> strengths = new ArrayList<>();
        List<String> weaknesses = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        if (matchScore >= 70) {
            strengths.add("Your resume already aligns well with the required skills for this role.");
        }
        strengths.add(String.format("Matched %d of %d required skills.", matches, reqCount));

        if (matchScore < 70) {
            if (!missing.isEmpty()) {
                weaknesses.add("Missing required skills: " + String.join(", ", missing));
            } else {
                weaknesses.add("Some required skills may not be expressed clearly in your resume.");
            }
        }

        for (String m : missing) {
            suggestions.add(String.format("If you have experience with %s, emphasize it in your experience or skills section; otherwise consider building projects or training in %s.", m, m));
        }

        if (job != null && job.title() != null && !job.title().isBlank()) {
            suggestions.add(String.format("Tailor your resume summary to the role title '%s' to make your fit clearer.", job.title()));
        }

        if (includeCoverLetter) {
            // mention top matched or missing skills to highlight in cover letter
            String hint = (!matched.isEmpty()) ? String.join(", ", matched) : (!missing.isEmpty() ? missing.get(0) : "relevant skills");
            suggestions.add(String.format("You may want to generate a tailored cover letter emphasizing %s.", hint));
        }

        String band;
        if (matchScore >= 70) band = "strong";
        else if (matchScore >= 40) band = "moderate";
        else band = "weak";

        String title = (job == null || job.title() == null || job.title().isBlank()) ? "this role" : job.title();
        String summary = String.format("For the %s position, your resume shows a %s fit (%d%% match). See suggestions to improve alignment.", title, band, matchScore);

        return new FeedbackDTO(matchScore, strengths, weaknesses, suggestions, summary);
    }
}
