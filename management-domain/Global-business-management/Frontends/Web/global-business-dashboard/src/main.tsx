import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './styles/globals.css';
import './i18n';

// Initialize Zustand store with persisted data
const rootElement = document.getElementById('root');

if (rootElement) {
  ReactDOM.createRoot(rootElement).render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
}

// Remove loading splash if exists
const loadingSplash = document.getElementById('loading-splash');
if (loadingSplash) {
  loadingSplash.remove();
}
