import { Card, CardHeader, CardBody } from '../../components/common'
import './SettingsPage.css'

export function SettingsPage() {
  return (
    <div className="settings-page">
      <div className="page-header">
        <div>
          <h1>Settings</h1>
          <p className="page-subtitle">Configure your dashboard preferences</p>
        </div>
      </div>

      <Card>
        <CardHeader>
          <h3>General Settings</h3>
        </CardHeader>
        <CardBody>
          <p>Dashboard settings and preferences.</p>
        </CardBody>
      </Card>
    </div>
  )
}
