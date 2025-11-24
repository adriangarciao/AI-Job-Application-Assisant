package adriangarciao.ai_job_app_assistant.service.ai;

import adriangarciao.ai_job_app_assistant.dto.ParsedJobDTO;
import adriangarciao.ai_job_app_assistant.dto.ParsedResumeDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SimpleParserService is a lightweight, deterministic placeholder parser.
 * It performs basic normalization and heuristic extraction of skills/experiences/title sections.
 */
@Service
public class SimpleParserService implements ParserService {

    private static final Pattern TITLE_LABEL = Pattern.compile("(?mi)^(?:title)\\s*[:\\-]\\s*(.+)$", Pattern.MULTILINE);

    @Override
    public ParsedResumeDTO parseResume(String resumeText) {
        String normalized = safeNormalize(resumeText);

        // redactedName: placeholder to simulate PII redaction
        String redactedName = "Candidate";

        List<String> skills = extractSkillsFromResume(normalized);
        List<String> experiences = extractExperiences(normalized);

        return new ParsedResumeDTO(redactedName, skills, experiences, normalized);
    }

    @Override
    public ParsedJobDTO parseJob(String jobPostingText) {
        String normalized = safeNormalize(jobPostingText);

        String title = extractTitle(normalized);
        List<String> required = extractSectionKeywords(normalized, "requirements", "required", "must have", "qualifications");
        List<String> niceToHave = extractSectionKeywords(normalized, "nice to have", "preferred", "nice-to-have");

        return new ParsedJobDTO(title, required, niceToHave, normalized);
    }

    private String safeNormalize(String text) {
        if (text == null) return "";
        // trim, normalize CRLF to LF, collapse multiple blank lines to a single blank line,
        String t = text.replace("\r\n", "\n").replace('\r', '\n').trim();
        t = t.replaceAll("\n{3,}", "\n\n");
        // collapse excessive whitespace within lines
        t = t.replaceAll("[ \t]{2,}", " ");
        return t;
    }

    private List<String> extractSkillsFromResume(String text) {
        List<String> skills = new ArrayList<>();
        if (text.isEmpty()) return skills;

        // Heuristic: look for a Skills section header
        String lower = text.toLowerCase(Locale.ROOT);
        int idx = indexOfSectionHeader(lower, "skills");
        if (idx >= 0) {
            String after = text.substring(Math.max(0, idx - "skills".length()));
            // stop at next header (two newlines) or end
            String[] parts = after.split("\n\n", 2);
            String section = parts[0];
            for (String token : splitToTokens(section)) {
                if (!token.isBlank()) skills.add(token.trim());
            }
            return skills;
        }

        // fallback: try to find lines that look like skill lists (commas, semicolons)
        for (String line : text.split("\n")) {
            if (line.contains(",") || line.contains(";")) {
                for (String token : line.split("[,;]")) {
                    String s = token.trim();
                    if (s.length() > 1 && s.length() < 60) skills.add(s);
                }
                if (!skills.isEmpty()) return skills;
            }
        }

        return skills;
    }

    private List<String> extractExperiences(String text) {
        List<String> exps = new ArrayList<>();
        if (text.isEmpty()) return exps;

        // Split by one or more blank lines (two or more newlines)
        String[] chunks = text.split("\n\s*\n+");
        for (String c : chunks) {
            String s = c.trim();
            if (!s.isBlank() && s.length() > 20) {
                exps.add(s);
            }
        }
        return exps;
    }

    private String extractTitle(String text) {
        if (text == null || text.isBlank()) return "";
        String[] lines = text.split("\n");
        // look for explicit "Title:" label
        for (String line : lines) {
            String l = line.trim();
            if (l.toLowerCase(Locale.ROOT).startsWith("title:")) {
                return l.substring(l.indexOf(":") + 1).trim();
            }
        }
        // fallback: first non-empty line
        for (String line : lines) {
            String l = line.trim();
            if (!l.isEmpty()) return l;
        }
        return "";
    }

    private List<String> extractSectionKeywords(String text, String... sectionNames) {
        List<String> found = new ArrayList<>();
        if (text == null || text.isBlank()) return found;
        String lower = text.toLowerCase(Locale.ROOT);

        for (String name : sectionNames) {
            int idx = lower.indexOf(name.toLowerCase(Locale.ROOT));
            if (idx >= 0) {
                // take substring after section name
                String after = text.substring(idx + name.length());
                // limit to next 500 chars to avoid huge capture
                after = after.length() > 500 ? after.substring(0, 500) : after;
                for (String token : splitToTokens(after)) {
                    if (!token.isBlank()) found.add(token.trim());
                }
            }
        }

        // Also add uppercase words as heuristics (e.g., JAVA, AWS)
        Pattern up = Pattern.compile("\\b([A-Z]{2,})\\b");
        Matcher m = up.matcher(text);
        while (m.find()) {
            String w = m.group(1);
            if (!found.contains(w)) found.add(w);
        }

        return found;
    }

    private int indexOfSectionHeader(String lowerText, String header) {
        // try patterns like "\nSkills" or start of text
        int idx = lowerText.indexOf("\n" + header);
        if (idx >= 0) return idx + 1 + header.length();
        if (lowerText.startsWith(header)) return header.length();
        return -1;
    }

    private String[] splitToTokens(String section) {
        // split on commas, semicolons, slashes, or newlines
        return section.split("[,;/\\n]");
    }
}
