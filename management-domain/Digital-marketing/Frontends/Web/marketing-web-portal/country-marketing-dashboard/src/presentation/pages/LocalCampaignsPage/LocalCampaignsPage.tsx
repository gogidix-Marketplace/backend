import { Card, CardHeader, CardBody } from '../../components/common'
import './LocalCampaignsPage.css'

export function LocalCampaignsPage() {
  return (
    <div className="local-campaigns-page">
      <div className="page-header">
        <div>
          <h1>Local Campaigns</h1>
          <p className="page-subtitle">Manage campaigns for your country</p>
        </div>
        <button className="btn btn-primary">+ New Campaign</button>
      </div>

      <Card>
        <CardBody>
          <p className="empty-state">No campaigns yet. Create your first campaign to get started.</p>
        </CardBody>
      </Card>
    </div>
  )
}
