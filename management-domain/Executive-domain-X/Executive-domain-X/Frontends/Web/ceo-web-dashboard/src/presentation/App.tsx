import { BrowserRouter } from 'react-router-dom'
import Routing from './routing'
import { ToastProvider } from '@shared/components/feedback/toast'

function App() {
  return (
    <BrowserRouter>
      <ToastProvider>
        <Routing />
      </ToastProvider>
    </BrowserRouter>
  )
}

export default App
