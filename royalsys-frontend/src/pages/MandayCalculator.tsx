import { useState, useEffect } from 'react'
import api from '../api/client'
import type { Scheme, MandayCalculationResponse } from '../types'

const CLIENT_ID = '50000000-0000-0000-0000-000000000001'

export default function MandayCalculator() {
  const [schemes, setSchemes] = useState<Scheme[]>([])
  const [schemeId, setSchemeId] = useState('')
  const [employeeCount, setEmployeeCount] = useState(50)
  const [riskLevel, setRiskLevel] = useState('')
  const [adjustmentType, setAdjustmentType] = useState('NONE')
  const [adjustmentPercent, setAdjustmentPercent] = useState(0)
  const [adjustmentReasons, setAdjustmentReasons] = useState('')
  const [siteEmployees, setSiteEmployees] = useState(0)
  const [result, setResult] = useState<MandayCalculationResponse | null>(null)
  const [error, setError] = useState('')

  useEffect(() => {
    api.get('/schemes').then(res => {
      setSchemes(res.data)
      if (res.data.length > 0) setSchemeId(res.data[0].id)
    })
  }, [])

  const calculate = async () => {
    setError('')
    setResult(null)
    try {
      const res = await api.post('/manday-calculator/calculate', {
        clientId: CLIENT_ID,
        schemeId,
        employeeCount,
        riskLevel: riskLevel || undefined,
        adjustmentType: adjustmentType === 'NONE' ? undefined : adjustmentType,
        adjustmentPercent: adjustmentType === 'NONE' ? undefined : adjustmentPercent,
        adjustmentReasons: adjustmentReasons || undefined,
        siteEmployeeCount: siteEmployees > 0 ? siteEmployees : undefined,
      })
      setResult(res.data)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Calculation failed')
    }
  }

  return (
    <div>
      <div className="page-header"><h2>Manday Calculator</h2></div>

      <div className="card">
        <div className="form-row">
          <div className="form-group">
            <label>Scheme</label>
            <select value={schemeId} onChange={e => setSchemeId(e.target.value)}>
              {schemes.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Number of Employees</label>
            <input type="number" min="1" value={employeeCount}
                   onChange={e => setEmployeeCount(Number(e.target.value))} />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Risk Level (for ISO 14001/45001)</label>
            <select value={riskLevel} onChange={e => setRiskLevel(e.target.value)}>
              <option value="">N/A (ISO 9001)</option>
              <option value="HIGH">High</option>
              <option value="MEDIUM">Medium</option>
              <option value="LOW">Low</option>
              <option value="LIMITED">Limited</option>
            </select>
          </div>
          <div className="form-group">
            <label>Adjustment</label>
            <select value={adjustmentType} onChange={e => setAdjustmentType(e.target.value)}>
              <option value="NONE">No Adjustment</option>
              <option value="INCREASE">Increase</option>
              <option value="DECREASE">Decrease</option>
            </select>
          </div>
        </div>

        {adjustmentType !== 'NONE' && (
          <div className="form-row">
            <div className="form-group">
              <label>Adjustment % (max 30)</label>
              <input type="number" min="0" max="30" value={adjustmentPercent}
                     onChange={e => setAdjustmentPercent(Number(e.target.value))} />
            </div>
            <div className="form-group">
              <label>Reasons</label>
              <input type="text" value={adjustmentReasons}
                     onChange={e => setAdjustmentReasons(e.target.value)}
                     placeholder="Reason for adjustment" />
            </div>
          </div>
        )}

        <div className="form-group">
          <label>Site Employees (0 = no site)</label>
          <input type="number" min="0" value={siteEmployees}
                 onChange={e => setSiteEmployees(Number(e.target.value))} />
        </div>

        <button className="btn btn-accent" onClick={calculate}>Calculate Mandays</button>

        {error && <p style={{ color: 'var(--danger)', marginTop: 12 }}>{error}</p>}
      </div>

      {result && (
        <div className="card">
          <h3>Calculation Results</h3>
          <table className="result-table">
            <thead><tr><th>Stage</th><th>Mandays</th></tr></thead>
            <tbody>
              <tr><td>Base Mandays</td><td>{result.baseMandays}</td></tr>
              <tr><td>Stage 1</td><td>{result.stage1Mandays}</td></tr>
              <tr><td>Stage 2</td><td>{result.stage2Mandays}</td></tr>
              <tr><td>Surveillance</td><td>{result.surveillanceMandays}</td></tr>
              <tr><td>Recertification</td><td>{result.recertificationMandays}</td></tr>

              {result.adjustedStage1 != null && (<>
                <tr><td colSpan={2} style={{fontWeight:600,paddingTop:16}}>
                  Adjusted ({result.adjustmentType} {result.adjustmentPercent}%)</td></tr>
                <tr><td>Adjusted Stage 1</td><td>{result.adjustedStage1}</td></tr>
                <tr><td>Adjusted Stage 2</td><td>{result.adjustedStage2}</td></tr>
                <tr><td>Adjusted Surveillance</td><td>{result.adjustedSurveillance}</td></tr>
                <tr><td>Adjusted Recertification</td><td>{result.adjustedRecertification}</td></tr>
              </>)}

              {result.siteStage1 != null && (<>
                <tr><td colSpan={2} style={{fontWeight:600,paddingTop:16}}>Site Calculations</td></tr>
                <tr><td>Site Stage 1</td><td>{result.siteStage1}</td></tr>
                <tr><td>Site Stage 2</td><td>{result.siteStage2}</td></tr>
                <tr><td>Site Surveillance</td><td>{result.siteSurveillance}</td></tr>
                <tr><td>Site Recertification</td><td>{result.siteRecertification}</td></tr>
              </>)}

              {result.siteAdjustedStage1 != null && (<>
                <tr><td colSpan={2} style={{fontWeight:600,paddingTop:16}}>Site Adjusted</td></tr>
                <tr><td>Site Adj. Stage 1</td><td>{result.siteAdjustedStage1}</td></tr>
                <tr><td>Site Adj. Stage 2</td><td>{result.siteAdjustedStage2}</td></tr>
                <tr><td>Site Adj. Surveillance</td><td>{result.siteAdjustedSurveillance}</td></tr>
                <tr><td>Site Adj. Recertification</td><td>{result.siteAdjustedRecertification}</td></tr>
              </>)}
            </tbody>
          </table>
          <p style={{marginTop:12,fontSize:'0.8rem',color:'var(--text-muted)'}}>
            Calculated at: {new Date(result.calculatedAt).toLocaleString()}
          </p>
        </div>
      )}
    </div>
  )
}
