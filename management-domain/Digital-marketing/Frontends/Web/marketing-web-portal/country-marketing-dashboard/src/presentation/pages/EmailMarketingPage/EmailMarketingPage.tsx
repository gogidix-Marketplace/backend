import { Card, CardHeader, CardBody } from '../../components/common'
import './EmailMarketingPage.css'

export function EmailMarketingPage() {
  return (
    <div className="email-marketing-page">
      <div className="page-header">
        <div>
          <h1>Email Marketing</h1>
          <p className="page-subtitle">Create and manage email campaigns</p>
        </div>
        <button className="btn btn-primary">+ New Campaign</button>
      </div>

      <Card>
        <CardBody>
          <p>Email campaign management interface coming soon.</p>
        </CardBody>
      </Card>
    </div>
  )
}
