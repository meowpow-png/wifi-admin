import { useState } from 'react'

import { DashboardPage, LoginPage } from './login'

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  if (isAuthenticated) {
    return <DashboardPage onLogout={() => setIsAuthenticated(false)} />
  }

  return <LoginPage onLogin={() => setIsAuthenticated(true)} />
}

export default App
