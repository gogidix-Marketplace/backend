import { Card, CardHeader, CardBody } from '../../components/common'
import './ReportsPage.css'

export function ReportsPage() {
  return (
    <div className="reports-page">
      <div className="page-header">
        <div>
          <h1>Reports</h1>
          <p className="page-subtitle">Generate and download marketing reports</p>
        </div>
        <button className="btn btn-primary">+ New Report</button>
      </div>

      <Card>
        <CardBody>
          <p>Report generation interface coming soon.</p>
        </CardBody>
      </Card>
    </div>
  )
}
