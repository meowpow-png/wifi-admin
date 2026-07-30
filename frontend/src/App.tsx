import {useState} from 'react'

import {DashboardPage, LoginPage} from './wifi'

function App() {
    const [token, setToken] = useState<string | null>(null)

    if (token === null) {
        return <LoginPage onLogin={setToken}/>
    }
    return (
        <DashboardPage
            token={token}
            onLogout={() => setToken(null)}
        />
    )
}

export default App
