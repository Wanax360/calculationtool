import { useState, useEffect } from 'react'
import api from '../api/client'
import type { DashboardStats } from '../types'

const ORG_ID = '00000000-0000-0000-0000-000000000001'

export default function Dashboard() {
  const [stats, setStats] = useState<DashboardStats | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get(`/dashboard/stats/${ORG_ID}`)
      .then(res => setStats(res.data))
      .catch(() => setStats({
        activeProjects: 0, pendingApplications: 0,
        activeCertificates: 0, outstandingAR: 0, totalRevenue: 0
      }))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div>Loading...</div>

  return (
    <div>
      <div className="page-header"><h2>Dashboard</h2></div>
      <div className="stats-grid">
        <div className="card">
          <div className="card-title">Active Projects</div>
          <div className="card-value">{stats?.activeProjects ?? 0}</div>
        </div>
        <div className="card">
          <div className="card-title">Pending Applications</div>
          <div className="card-value">{stats?.pendingApplications ?? 0}</div>
        </div>
        <div className="card">
          <div className="card-title">Active Certificates</div>
          <div className="card-value">{stats?.activeCertificates ?? 0}</div>
        </div>
        <div className="card">
          <div className="card-title">Outstanding AR</div>
          <div className="card-value">${(stats?.outstandingAR ?? 0).toLocaleString()}</div>
        </div>
        <div className="card">
          <div className="card-title">Total Revenue</div>
          <div className="card-value">${(stats?.totalRevenue ?? 0).toLocaleString()}</div>
        </div>
      </div>

      <div className="card">
        <h3>Quick Actions</h3>
        <div style={{ display: 'flex', gap: '12px', marginTop: '12px' }}>
          <a href="/calculator" className="btn btn-primary">Manday Calculator</a>
          <a href="/clients" className="btn btn-primary">View Clients</a>
          <a href="/invoices" className="btn btn-primary">Invoices</a>
        </div>
      </div>
    </div>
  )
}
