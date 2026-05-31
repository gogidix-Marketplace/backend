import { Routes, Route } from 'react-router-dom'
import Layout from '@/components/Layout'
import Dashboard from '@/pages/Dashboard'
import Transactions from '@/pages/Transactions'
import TransactionDetail from '@/pages/TransactionDetail'
import Onboarding from '@/pages/Onboarding'
import Monitoring from '@/pages/Monitoring'

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/transactions" element={<Transactions />} />
        <Route path="/transactions/:id" element={<TransactionDetail />} />
        <Route path="/onboarding" element={<Onboarding />} />
        <Route path="/monitoring" element={<Monitoring />} />
      </Routes>
    </Layout>
  )
}

export default App
