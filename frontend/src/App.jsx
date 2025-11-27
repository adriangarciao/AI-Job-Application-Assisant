import { useState } from 'react'
import './App.css'

function App() {
  const [resumeText, setResumeText] = useState('')
  const [jobPostingText, setJobPostingText] = useState('')
  const [includeCoverLetter, setIncludeCoverLetter] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [result, setResult] = useState(null)

  const handleAnalyze = async () => {
    setError('')
    setResult(null)

    if (!resumeText.trim() || !jobPostingText.trim()) {
      setError('Please fill in both resume and job posting fields.')
      return
    }

    setLoading(true)

    try {
      const response = await fetch('http://localhost:8080/api/ai/analyze', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          resumeText,
          jobPostingText,
          includeCoverLetter,
        }),
      })

      if (!response.ok) {
        if (response.status === 400) {
          setError('Please check your input and try again.')
        } else {
          setError('Something went wrong. Please try again later.')
        }
        setLoading(false)
        return
      }

      const data = await response.json()
      setResult(data)
    } catch (err) {
      setError('Something went wrong. Please try again later.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app-container">
      <div className="content">
        <header>
          <h1>AI Job Application Assistant</h1>
          <p className="subtitle">Get AI-powered feedback on how well your resume matches a job posting.</p>
        </header>

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

          <button
            className="analyze-button"
            onClick={handleAnalyze}
            disabled={loading}
          >
            {loading ? 'Analyzing...' : 'Analyze'}
          </button>
        </div>

        {result && (
          <div className="results-card">
            <div className="match-score">
              <div className="score-label">Match Score</div>
              <div className="score-value">{result.matchScore}/100</div>
            </div>

            {(result.jobLocation || result.compensationInfo) && (
              <div className="job-details-section">
                <h3>Job Details</h3>
                <div className="job-details-content">
                  {result.jobLocation && (
                    <div className="job-detail-item">
                      <span className="job-detail-label">Location:</span>
                      <span className="job-detail-value">{result.jobLocation}</span>
                    </div>
                  )}
                  {result.compensationInfo && (
                    <div className="job-detail-item">
                      <span className="job-detail-label">Compensation:</span>
                      <span className="job-detail-value">{result.compensationInfo}</span>
                    </div>
                  )}
                </div>
              </div>
            )}

            <div className="summary-section">
              <h3>Summary</h3>
              <p>{result.summary}</p>
            </div>

            {result.strengths && result.strengths.length > 0 && (
              <div className="section">
                <h3>Strengths</h3>
                <ul>
                  {result.strengths.map((item, index) => (
                    <li key={index}>{item}</li>
                  ))}
                </ul>
              </div>
            )}

            {result.weaknesses && result.weaknesses.length > 0 && (
              <div className="section">
                <h3>Weaknesses</h3>
                <ul>
                  {result.weaknesses.map((item, index) => (
                    <li key={index}>{item}</li>
                  ))}
                </ul>
              </div>
            )}

            {result.suggestions && result.suggestions.length > 0 && (
              <div className="section">
                <h3>Suggestions</h3>
                <ul>
                  {result.suggestions.map((item, index) => (
                    <li key={index}>{item}</li>
                  ))}
                </ul>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}

export default App
