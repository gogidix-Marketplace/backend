import { Routes, Route } from 'react-router-dom'
import Layout from '@/components/Layout'
import Dashboard from '@/pages/Dashboard'
import Charts from '@/pages/Charts'
import Realtime from '@/pages/Realtime'
import Settings from '@/pages/Settings'

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/charts" element={<Charts />} />
        <Route path="/realtime" element={<Realtime />} />
        <Route path="/settings" element={<Settings />} />
      </Routes>
    </Layout>
  )
}

export default App
