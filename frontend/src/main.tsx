import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import '@telekom/scale-components/dist/scale-components/scale-components.css'
import { defineCustomElements } from '@telekom/scale-components/loader'
import './index.css'
import App from './App.tsx'

defineCustomElements(window)

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
