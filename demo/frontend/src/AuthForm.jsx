import { useState } from 'react'
import { api } from './api.js'

export function AuthForm({ onSignedIn }) {
  const [mode, setMode] = useState('signup') // or 'login'
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState('')

  function update(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  async function submit(e) {
    e.preventDefault()
    setError('')
    try {
      const user = await api('/auth/' + mode, { method: 'POST', body: form })
      onSignedIn(user)
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <main className="box">
      <h1>{mode === 'signup' ? 'Create your account' : 'Welcome back'}</h1>
      <p className="hint">
        {mode === 'signup' ? 'A name, an email and a password. That is all.' : 'Log in to see your list.'}
      </p>
      <form onSubmit={submit}>
        {mode === 'signup' && (
          <input name="name" placeholder="Your name" value={form.name} onChange={update} required />
        )}
        <input name="email" type="email" placeholder="Email" value={form.email} onChange={update} required />
        <input name="password" type="password" placeholder="Password (6+ characters)" value={form.password} onChange={update} required />
        {error && <p className="error">{error}</p>}
        <button type="submit">{mode === 'signup' ? 'Sign up' : 'Log in'}</button>
      </form>
      <p className="switch">
        {mode === 'signup' ? 'Already have an account? ' : 'New here? '}
        <button type="button" className="link" onClick={() => setMode(mode === 'signup' ? 'login' : 'signup')}>
          {mode === 'signup' ? 'Log in' : 'Sign up'}
        </button>
      </p>
    </main>
  )
}
