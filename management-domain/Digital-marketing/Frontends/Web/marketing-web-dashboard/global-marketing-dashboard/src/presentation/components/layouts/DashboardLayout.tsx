// DashboardLayout Component
// Main layout wrapper for dashboard pages

import React from 'react'
import { Outlet } from 'react-router-dom'
import './DashboardLayout.css'

interface DashboardLayoutProps {
  sidebar: React.ReactNode
  header: React.ReactNode
}

export const DashboardLayout: React.FC<DashboardLayoutProps> = ({ sidebar, header }) => {
  return (
    <div className="dashboard-layout">
      {sidebar}
      <div className="dashboard-layout__main">
        {header}
        <main className="dashboard-layout__content">
          <Outlet />
        </main>
      </div>
    </div>
  )
}

export default DashboardLayout
