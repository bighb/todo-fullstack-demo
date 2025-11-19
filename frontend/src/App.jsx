import { useState, useEffect } from "react";
import { todoApi } from "./services/todoApi";
import TodoForm from "./components/TodoForm";
import TodoList from "./components/TodoList";
import "./App.css";

function App() {
  const [todos, setTodos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 开发环境下此 useEffect 会执行两次，导致 /api/todos 被请求两次
  // 原因：StrictMode 的双重挂载机制，详见 main.jsx
  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      const response = await todoApi.getAll();
      setTodos(response.data);
      setError(null);
    } catch (err) {
      setError("Failed to fetch todos. Is the backend running?");
      console.error("Failed to fetch todos:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddTodo = async (todoData) => {
    try {
      const response = await todoApi.create(todoData);
      setTodos([response.data, ...todos]);
    } catch (err) {
      console.error("Failed to create todo:", err);
    }
  };

  const handleToggleTodo = async (id) => {
    const todo = todos.find((t) => t.id === id);
    try {
      const response = await todoApi.update(id, {
        ...todo,
        completed: !todo.completed,
      });
      setTodos(todos.map((t) => (t.id === id ? response.data : t)));
    } catch (err) {
      console.error("Failed to update todo:", err);
    }
  };

  const handleDeleteTodo = async (id) => {
    try {
      await todoApi.delete(id);
      setTodos(todos.filter((t) => t.id !== id));
    } catch (err) {
      console.error("Failed to delete todo:", err);
    }
  };

  if (loading) {
    return (
      <div className="app">
        <div className="app-container">
          <p className="loading">Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <div className="app-container">
        <h1>Todo List</h1>
        <p className="app-subtitle">Organize your tasks efficiently</p>
        {error && <p className="error">{error}</p>}
        <TodoForm onSubmit={handleAddTodo} />
        <TodoList
          todos={todos}
          onToggle={handleToggleTodo}
          onDelete={handleDeleteTodo}
        />
      </div>
    </div>
  );
}

export default App;
