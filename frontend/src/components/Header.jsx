import React from 'react'

export default function Header({title, subtitle}){
  return (
    <header>
      <h1>{title}</h1>
      <div className="title-underline" aria-hidden="true" />
      {subtitle && <p className="subtitle">{subtitle}</p>}
    </header>
  )
}
