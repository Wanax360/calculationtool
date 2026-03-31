import { useState, useEffect } from 'react'
import api from '../api/client'
import type { Invoice } from '../types'

const ORG_ID = '00000000-0000-0000-0000-000000000001'

export default function Invoices() {
  const [invoices, setInvoices] = useState<Invoice[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get(`/invoices?orgId=${ORG_ID}`)
      .then(res => setInvoices(res.data.content || []))
      .catch(() => setInvoices([]))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div>Loading...</div>

  const statusBadge = (status: string) => {
    if (status.includes('PAID') && !status.includes('PARTIALLY')) return 'badge-active'
    if (status.includes('OVERDUE')) return 'badge-overdue'
    return 'badge-pending'
  }

  return (
    <div>
      <div className="page-header"><h2>Invoices</h2></div>
      <div className="card">
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Invoice #</th><th>Total</th><th>Paid</th>
                <th>Balance</th><th>Due Date</th><th>Status</th>
              </tr>
            </thead>
            <tbody>
              {invoices.length === 0 ? (
                <tr><td colSpan={6} style={{textAlign:'center'}}>No invoices yet</td></tr>
              ) : invoices.map(i => (
                <tr key={i.id}>
                  <td><strong>{i.invoiceNumber}</strong></td>
                  <td>{i.currency} {i.totalAmount?.toLocaleString()}</td>
                  <td>{i.currency} {i.amountPaid?.toLocaleString()}</td>
                  <td>{i.currency} {i.balanceDue?.toLocaleString()}</td>
                  <td>{i.dueDate}</td>
                  <td><span className={`badge ${statusBadge(i.status)}`}>{i.status}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
