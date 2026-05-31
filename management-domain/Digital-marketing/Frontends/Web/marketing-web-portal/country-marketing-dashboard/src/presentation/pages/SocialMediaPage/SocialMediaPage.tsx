import { Card, CardHeader, CardBody } from '../../components/common'
import './SocialMediaPage.css'

export function SocialMediaPage() {
  return (
    <div className="social-media-page">
      <div className="page-header">
        <div>
          <h1>Social Media</h1>
          <p className="page-subtitle">Manage social media posts and engagement</p>
        </div>
        <button className="btn btn-primary">+ New Post</button>
      </div>

      <Card>
        <CardBody>
          <p>Social media management interface coming soon.</p>
        </CardBody>
      </Card>
    </div>
  )
}
