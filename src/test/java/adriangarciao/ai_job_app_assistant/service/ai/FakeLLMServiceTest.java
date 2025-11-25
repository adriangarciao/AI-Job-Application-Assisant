package adriangarciao.ai_job_app_assistant.service.ai._moved;

/**
 * Placeholder to keep the moved duplicate test file harmless.
 * The real `FakeLLMServiceTest` lives under `service.ai.llm`.
 */
public final class FakeLLMServiceTest_Old {
    private FakeLLMServiceTest_Old() { }

    @Test
    void generateFeedback_matchesAndMissingSkills() {
        ParsedResumeDTO resume = new ParsedResumeDTO("Candidate", List.of("java", "spring", "docker"), List.of("exp1"), "raw");
        ParsedJobDTO job = new ParsedJobDTO("Senior Eng", List.of("Java", "AWS", "Spring"), List.of("Kubernetes"), "rawjob");

        FeedbackDTO fb = llm.generateFeedback(resume, job, false);
        assertNotNull(fb);

        // Matches: java, spring => 2 matches of 3 required -> ~67 -> rounded
        assertTrue(fb.matchScore() >= 0 && fb.matchScore() <= 100);
        assertTrue(fb.strengths().size() >= 1);
        // Should list missing required skill AWS
        boolean mentionsAws = fb.weaknesses().stream().anyMatch(s -> s.toLowerCase().contains("aws")) || fb.suggestions().stream().anyMatch(s -> s.toLowerCase().contains("aws"));
        assertTrue(mentionsAws, "Feedback should mention missing required skill AWS");
    }

    @Test
    void generateFeedback_highMatchAddsStrength() {
        ParsedResumeDTO resume = new ParsedResumeDTO("Candidate", List.of("java", "spring", "aws", "kubernetes"), List.of("exp1"), "raw");
        ParsedJobDTO job = new ParsedJobDTO("Senior Eng", List.of("Java", "AWS"), List.of(), "rawjob");

        FeedbackDTO fb = llm.generateFeedback(resume, job, true);
        assertNotNull(fb);
        assertTrue(fb.matchScore() >= 70);
        assertTrue(fb.strengths().stream().anyMatch(s -> s.toLowerCase().contains("aligns well") || s.toLowerCase().contains("matched")));
        // includeCoverLetter true should add a suggestion mentioning cover letter
        assertTrue(fb.suggestions().stream().anyMatch(s -> s.toLowerCase().contains("cover letter")));
    }
}
