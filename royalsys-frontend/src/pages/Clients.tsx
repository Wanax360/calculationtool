import { useState, useEffect } from 'react'
import api from '../api/client'
import type { Client } from '../types'

const ORG_ID = '00000000-0000-0000-0000-000000000001'

export default function Clients() {
  const [clients, setClients] = useState<Client[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get(`/clients?orgId=${ORG_ID}`)
      .then(res => setClients(res.data.content || []))
      .catch(() => setClients([]))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div>Loading...</div>

  return (
    <div>
      <div className="page-header">
        <h2>Clients</h2>
      </div>
      <div className="card">
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Company</th>
                <th>Sector</th>
                <th>NACE</th>
                <th>Employees</th>
                <th>Contact</th>
                <th>Location</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {clients.length === 0 ? (
                <tr><td colSpan={7} style={{ textAlign: 'center' }}>No clients found</td></tr>
              ) : clients.map(c => (
                <tr key={c.id}>
                  <td><strong>{c.companyName}</strong></td>
                  <td>{c.industrySector}</td>
                  <td>{c.naceCode}</td>
                  <td>{c.employeeCount}</td>
                  <td>{c.contactName}<br/><small>{c.contactEmail}</small></td>
                  <td>{c.city}, {c.country}</td>
                  <td><span className={`badge ${c.active ? 'badge-active' : 'badge-pending'}`}>
                    {c.active ? 'Active' : 'Inactive'}
                  </span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
