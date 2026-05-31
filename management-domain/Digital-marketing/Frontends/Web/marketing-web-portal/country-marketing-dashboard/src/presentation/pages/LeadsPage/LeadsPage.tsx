import { Card, CardHeader, CardBody } from '../../components/common'
import './LeadsPage.css'

export function LeadsPage() {
  return (
    <div className="leads-page">
      <div className="page-header">
        <div>
          <h1>Lead Management</h1>
          <p className="page-subtitle">Track and manage leads from your campaigns</p>
        </div>
        <button className="btn btn-primary">+ Add Lead</button>
      </div>

      <Card>
        <CardBody>
          <p>Lead management interface coming soon.</p>
        </CardBody>
      </Card>
    </div>
  )
}
