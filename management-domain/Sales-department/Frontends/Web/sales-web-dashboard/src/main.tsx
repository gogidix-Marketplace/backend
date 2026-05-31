import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './presentation/App'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import '@shared/styles/globals.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
    <ReactQueryDevtools initialIsOpen={false} />
  </React.StrictMode>,
)
