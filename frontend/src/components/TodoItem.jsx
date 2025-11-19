function TodoItem({ todo, onToggle, onDelete }) {
  return (
    <div className={`todo-item ${todo.completed ? 'completed' : ''}`}>
      <div className="todo-content">
        <input
          type="checkbox"
          checked={todo.completed}
          onChange={() => onToggle(todo.id)}
        />
        <div className="todo-text">
          <span className="todo-title">{todo.title}</span>
          {todo.description && (
            <span className="todo-description">{todo.description}</span>
          )}
        </div>
      </div>
      <button className="delete-button" onClick={() => onDelete(todo.id)}>
        Delete
      </button>
    </div>
  );
}

export default TodoItem;
