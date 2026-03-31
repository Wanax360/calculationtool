import { useState, useEffect } from 'react'
import api from '../api/client'

const ORG_ID = '00000000-0000-0000-0000-000000000001'

interface Project {
  id: string; projectNumber: string; status: string; cycleType: string
}

export default function Projects() {
  const [projects, setProjects] = useState<Project[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get(`/projects?orgId=${ORG_ID}`)
      .then(res => setProjects(res.data.content || []))
      .catch(() => setProjects([]))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div>Loading...</div>

  const statusBadge = (status: string) => {
    if (status.includes('ACTIVE')) return 'badge-active'
    if (status.includes('PENDING') || status.includes('CREATED')) return 'badge-pending'
    return ''
  }

  return (
    <div>
      <div className="page-header"><h2>Projects</h2></div>
      <div className="card">
        <div className="table-container">
          <table>
            <thead>
              <tr><th>Project #</th><th>Cycle</th><th>Status</th></tr>
            </thead>
            <tbody>
              {projects.length === 0 ? (
                <tr><td colSpan={3} style={{textAlign:'center'}}>No projects yet</td></tr>
              ) : projects.map(p => (
                <tr key={p.id}>
                  <td><strong>{p.projectNumber}</strong></td>
                  <td>{p.cycleType}</td>
                  <td><span className={`badge ${statusBadge(p.status)}`}>{p.status}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
