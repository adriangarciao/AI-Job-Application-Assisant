import React from 'react'

export default function Header({title, subtitle}){
  return (
    <header>
      <h1>{title}</h1>
      {subtitle && <p className="subtitle">{subtitle}</p>}
    </header>
  )
}
