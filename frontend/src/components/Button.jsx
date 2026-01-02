import React from 'react'

export default function Button({children,onClick,disabled,className=''}){
  return (
    <button className={`analyze-button ${className}`} onClick={onClick} disabled={disabled}>
      {children}
    </button>
  )
}
