import React from 'react'

export default function FormCard({
  resumeText,
  setResumeText,
  jobPostingText,
  setJobPostingText,
  includeCoverLetter,
  setIncludeCoverLetter,
  handleAnalyze,
  loading,
  error,
}){
  return (
    <div className="form-card">
      <div className="form-group">
        <label htmlFor="resume">Your Resume</label>
        <textarea
          id="resume"
          placeholder="Paste your resume text here..."
          value={resumeText}
          onChange={(e) => setResumeText(e.target.value)}
          rows={8}
        />
      </div>

      <div className="form-group">
        <label htmlFor="job">Job Posting</label>
        <textarea
          id="job"
          placeholder="Paste the job posting text here..."
          value={jobPostingText}
          onChange={(e) => setJobPostingText(e.target.value)}
          rows={8}
        />
      </div>

      <div className="form-group checkbox-group">
        <label>
          <input
            type="checkbox"
            checked={includeCoverLetter}
            onChange={(e) => setIncludeCoverLetter(e.target.checked)}
          />
          <span>Include cover letter suggestions</span>
        </label>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div style={{marginTop:16}}>
        <button className="analyze-button" onClick={handleAnalyze} disabled={loading}>
          {loading ? 'Analyzing...' : 'Analyze'}
        </button>
      </div>
    </div>
  )
}
