import { useEffect, useState } from 'react'
import { api } from './api.js'

export function TodoList({ user, onSignOut }) {
  const [todos, setTodos] = useState([])
  const [title, setTitle] = useState('')
  const [error, setError] = useState('')
  const token = user.token

  // When this screen appears, ask the server for my todos.
  useEffect(() => {
    api('/todos', { token }).then(setTodos).catch(e => setError(e.message))
  }, [token])

  async function add(e) {
    e.preventDefault()
    if (!title.trim()) return
    const created = await api('/todos', { method: 'POST', body: { title }, token })
    setTodos([created, ...todos])
    setTitle('')
  }

  async function toggle(todo) {
    const updated = await api(`/todos/${todo.id}/toggle`, { method: 'PATCH', token })
    setTodos(todos.map(t => (t.id === todo.id ? updated : t)))
  }

  async function remove(todo) {
    await api(`/todos/${todo.id}`, { method: 'DELETE', token })
    setTodos(todos.filter(t => t.id !== todo.id))
  }

  return (
    <main className="box">
      <header>
        <h1>Hi {user.name}</h1>
        <button className="link" onClick={onSignOut}>Sign out</button>
      </header>

      <form onSubmit={add} className="row">
        <input placeholder="What needs doing?" value={title} onChange={e => setTitle(e.target.value)} />
        <button type="submit">Add</button>
      </form>

      {error && <p className="error">{error}</p>}
      {todos.length === 0 && !error && <p className="empty">Nothing yet. Add your first todo above.</p>}

      <ul>
        {todos.map(todo => (
          <li key={todo.id} className={todo.done ? 'done' : ''}>
            <label>
              <input type="checkbox" checked={todo.done} onChange={() => toggle(todo)} />
              {todo.title}
            </label>
            <button className="link" onClick={() => remove(todo)}>Delete</button>
          </li>
        ))}
      </ul>
    </main>
  )
}
