import { Card, CardHeader, CardBody } from '../../components/common'
import './ContentPage.css'

export function ContentPage() {
  return (
    <div className="content-page">
      <div className="page-header">
        <div>
          <h1>Content Library</h1>
          <p className="page-subtitle">Manage your marketing content</p>
        </div>
        <button className="btn btn-primary">+ New Content</button>
      </div>

      <Card>
        <CardBody>
          <p>Content library and calendar coming soon.</p>
        </CardBody>
      </Card>
    </div>
  )
}
