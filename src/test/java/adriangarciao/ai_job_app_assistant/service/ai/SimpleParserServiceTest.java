package adriangarciao.ai_job_app_assistant.service.ai;

import adriangarciao.ai_job_app_assistant.dto.ParsedJobDTO;
import adriangarciao.ai_job_app_assistant.dto.ParsedResumeDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleParserServiceTest {

    private final SimpleParserService parser = new SimpleParserService();

    @Test
    void parseResume_emptyAndNullDontCrash() {
        ParsedResumeDTO r1 = parser.parseResume(null);
        assertNotNull(r1);
        assertEquals("Candidate", r1.redactedName());
        assertTrue(r1.skills().isEmpty());
        assertTrue(r1.experiences().isEmpty());

        ParsedResumeDTO r2 = parser.parseResume("");
        assertNotNull(r2);
        assertEquals("Candidate", r2.redactedName());
        assertTrue(r2.skills().isEmpty());
        assertTrue(r2.experiences().isEmpty());
    }

    @Test
    void parseResume_extractsSkillsAndExperiences() {
        String resume = "John Doe\n\nSkills:\nJava, Spring, SQL\n\nExperience:\nSoftware Engineer at X\n- Did stuff\n\nSoftware Engineer at Y\n- Did other stuff\n";
        ParsedResumeDTO dto = parser.parseResume(resume);
        assertEquals("Candidate", dto.redactedName());
        assertTrue(dto.skills().contains("Java"));
        assertTrue(dto.skills().contains("Spring"));
        assertTrue(dto.skills().contains("SQL"));
        assertTrue(dto.experiences().size() >= 2);
    }

    @Test
    void parseJob_extractsTitleAndSkills() {
        String job = "Title: Senior Java Engineer\n\nRequirements:\nJava\nSpring\nAWS\n\nNice to have:\nDocker\nKubernetes\n";
        ParsedJobDTO dto = parser.parseJob(job);
        assertEquals("Senior Java Engineer", dto.title());
        // requiredSkills should include Java and Spring
        assertTrue(dto.requiredSkills().stream().anyMatch(s -> s.equalsIgnoreCase("Java")));
        assertTrue(dto.requiredSkills().stream().anyMatch(s -> s.equalsIgnoreCase("Spring")));
        // niceToHaveSkills should include Docker
        assertTrue(dto.niceToHaveSkills().stream().anyMatch(s -> s.equalsIgnoreCase("Docker")));
    }
}
