import { useState } from 'react'
import { AuthForm } from './AuthForm.jsx'
import { TodoList } from './TodoList.jsx'

// The whole app is one question: do we know who you are?
export function App() {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user')
    return saved ? JSON.parse(saved) : null
  })

  function signedIn(u) {
    localStorage.setItem('user', JSON.stringify(u))
    setUser(u)
  }

  function signOut() {
    localStorage.removeItem('user')
    setUser(null)
  }

  if (!user) return <AuthForm onSignedIn={signedIn} />
  return <TodoList user={user} onSignOut={signOut} />
}
