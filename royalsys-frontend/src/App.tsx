import { Routes, Route, NavLink } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import Clients from './pages/Clients'
import MandayCalculator from './pages/MandayCalculator'
import Projects from './pages/Projects'
import Invoices from './pages/Invoices'

function App() {
  return (
    <div className="app">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h1>RoyalSys</h1>
          <p>Certification Management</p>
        </div>
        <nav className="sidebar-nav">
          <div className="nav-section">Overview</div>
          <NavLink to="/" className={({isActive}) => `nav-link${isActive ? ' active' : ''}`}>Dashboard</NavLink>

          <div className="nav-section">Operations</div>
          <NavLink to="/clients" className={({isActive}) => `nav-link${isActive ? ' active' : ''}`}>Clients</NavLink>
          <NavLink to="/projects" className={({isActive}) => `nav-link${isActive ? ' active' : ''}`}>Projects</NavLink>
          <NavLink to="/calculator" className={({isActive}) => `nav-link${isActive ? ' active' : ''}`}>Manday Calculator</NavLink>

          <div className="nav-section">Finance</div>
          <NavLink to="/invoices" className={({isActive}) => `nav-link${isActive ? ' active' : ''}`}>Invoices</NavLink>
        </nav>
      </aside>

      <main className="main-content">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/clients" element={<Clients />} />
          <Route path="/projects" element={<Projects />} />
          <Route path="/calculator" element={<MandayCalculator />} />
          <Route path="/invoices" element={<Invoices />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
