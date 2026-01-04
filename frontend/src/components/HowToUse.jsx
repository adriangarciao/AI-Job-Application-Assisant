import React from 'react'

export default function HowToUse(){
  const steps = [
    'Create an account and complete your profile',
    'Connect or paste your resume',
    'Paste a job posting to analyze',
    'Review strengths, weaknesses and suggestions',
    'Apply with confidence and iterate',
  ]

  return (
    <section className="howto-card">
      <h2>How to Use</h2>
      <div className="howto-steps">
        {steps.map((s,i)=> (
          <div key={i} className="howto-step">
            <div className="step-num">{i+1}</div>
            <div className="step-text">{s}</div>
          </div>
        ))}
      </div>
    </section>
  )
}
