import { useState } from 'react'
import './App.css'
import Header from './components/Header'
import FormCard from './components/FormCard'
import ResultsCard from './components/ResultsCard'

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
        <Header title="AI Job Application Assistant." subtitle="Get AI-powered feedback on how well your resume matches a job posting." />

        <FormCard
          resumeText={resumeText}
          setResumeText={setResumeText}
          jobPostingText={jobPostingText}
          setJobPostingText={setJobPostingText}
          includeCoverLetter={includeCoverLetter}
          setIncludeCoverLetter={setIncludeCoverLetter}
          handleAnalyze={handleAnalyze}
          loading={loading}
          error={error}
        />

        <ResultsCard result={result} />
      </div>
    </div>
  )
}

export default App
