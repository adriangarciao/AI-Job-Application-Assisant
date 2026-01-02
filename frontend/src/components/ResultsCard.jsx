import React from 'react'

export default function ResultsCard({result}){
  if(!result) return null
  return (
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
  )
}
