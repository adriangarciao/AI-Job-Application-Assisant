package com.adriangarciao.jobmatch.config;

import com.adriangarciao.jobmatch.model.Application;
import com.adriangarciao.jobmatch.model.ApplicationStatus;
import com.adriangarciao.jobmatch.model.Resume;
import com.adriangarciao.jobmatch.model.Role;
import com.adriangarciao.jobmatch.model.User;
import com.adriangarciao.jobmatch.repository.ApplicationRepository;
import com.adriangarciao.jobmatch.repository.ResumeRepository;
import com.adriangarciao.jobmatch.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Seeds a single non-admin demo account (plus one sample resume and one sample
 * application it owns) on startup so a recruiter can Authorize in Swagger and get
 * non-empty 200 responses from protected endpoints without any setup.
 *
 * <p>Safe to run on every boot: it is fully idempotent (creates the user only if the
 * email is not already present) and harmless when unconfigured (skips silently if the
 * demo credentials are not provided). Disabled entirely in the test profile via
 * {@code app.demo.seed.enabled=false} so it never pollutes the H2 test suite.
 *
 * <p>Credentials are read from environment variables — the password is hashed with the
 * shared {@link PasswordEncoder} and never stored or logged in plaintext.
 */
@Component
@ConditionalOnProperty(prefix = "app.demo.seed", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    /** Demo account email, sourced from DEMO_USER_EMAIL. */
    @Value("${app.demo.email:}")
    private String demoEmail;

    /** Demo account password, sourced from DEMO_USER_PASSWORD. */
    @Value("${app.demo.password:}")
    private String demoPassword;

    public DemoDataSeeder(UserRepository userRepository,
                          ResumeRepository resumeRepository,
                          ApplicationRepository applicationRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(org.springframework.boot.ApplicationArguments args) {
        if (!StringUtils.hasText(demoEmail) || !StringUtils.hasText(demoPassword)) {
            log.info("Demo seeding skipped: DEMO_USER_EMAIL / DEMO_USER_PASSWORD not set.");
            return;
        }

        if (userRepository.existsByEmail(demoEmail)) {
            log.info("Demo seeding skipped: demo account '{}' already exists.", demoEmail);
            return;
        }

        User demo = new User();
        demo.setName("Demo Recruiter");
        demo.setEmail(demoEmail);
        demo.setPasswordHash(passwordEncoder.encode(demoPassword));
        demo.setRole(Role.USER); // non-admin, so role-gated routes still return 403
        demo.setSkills(List.of("java", "spring", "sql", "react"));
        demo.setDesiredJobTitle(List.of("Backend Engineer"));
        demo = userRepository.save(demo);

        Resume sampleResume = new Resume();
        sampleResume.setUser(demo);
        sampleResume.setOriginalFilename("sample-resume.txt");
        sampleResume.setStoredFilename("sample-resume.txt");
        sampleResume.setContentType("text/plain");
        sampleResume.setSizeBytes(0L);
        sampleResume.setUploadedAt(LocalDateTime.now());
        sampleResume.setParsedText("SAMPLE DEMO DATA - Backend engineer with Java, Spring Boot, "
                + "PostgreSQL and React experience. Built REST APIs and CI pipelines.");
        resumeRepository.save(sampleResume);

        Application sampleApplication = new Application();
        sampleApplication.setUser(demo);
        sampleApplication.setJobTitle("Backend Engineer (SAMPLE DEMO DATA)");
        sampleApplication.setCompany("Acme Corp");
        sampleApplication.setStatus(ApplicationStatus.APPLIED);
        sampleApplication.setAppliedDate(LocalDate.now());
        sampleApplication.setCompensation(120000);
        applicationRepository.save(sampleApplication);

        log.info("Demo seeding complete: created demo account '{}' with 1 sample resume "
                + "and 1 sample application.", demoEmail);
    }
}
