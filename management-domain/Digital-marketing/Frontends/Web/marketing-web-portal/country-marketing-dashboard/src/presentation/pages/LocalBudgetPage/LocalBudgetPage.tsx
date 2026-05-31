import { Card, CardHeader, CardBody } from '../../components/common'
import './LocalBudgetPage.css'

export function LocalBudgetPage() {
  return (
    <div className="local-budget-page">
      <div className="page-header">
        <div>
          <h1>Budget Management</h1>
          <p className="page-subtitle">Track and manage your marketing budget</p>
        </div>
      </div>

      <Card>
        <CardBody>
          <p>Budget overview and allocation details will be displayed here.</p>
        </CardBody>
      </Card>
    </div>
  )
}
