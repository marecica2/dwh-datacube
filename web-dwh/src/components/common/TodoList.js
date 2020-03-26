import React, { useState, useRef } from 'react';

export const TodoList = () => {
  const [todos, setTodos] = useState([
    { id: '8NaU7k', label: 'Learn about Hooks', done: true },
    { id: 'Wxxfs1', label: 'Prepare a demo', done: false },
    { id: 'sPMpSH', label: 'Prepare presentation slides', done: false },
  ]);

  const labelInputRef = useRef();

  const setTodoDone = (id, done) =>
    setTodos(todos.map(todo => (todo.id === id ? { ...todo, done } : todo)));

  const removeTodo = id => setTodos(todos.filter(todo => todo.id !== id));

  const addNewTodo = label =>
    setTodos([
      ...todos,
      {
        id: Math.round(Math.random() * 10000),
        label,
        done: false,
      },
    ]);

  const onAddButtonClick = () => {
    const labelInput = labelInputRef.current;
    addNewTodo(labelInput.value);
    labelInput.value = '';
  };

  return (
    <div className="todo-list">
      <ul>
        {todos.map(todo => (
          <li key={todo.id}>
            <input
              type="checkbox"
              checked={todo.done}
              onChange={({ target }) => setTodoDone(todo.id, target.checked)}
              label={todo.label}
            />
            <span className={todo.done ? 'done' : ''}>{todo.label}</span>
            <button onClick={() => removeTodo(todo.id)}>X</button>
          </li>
        ))}
      </ul>
      <div className="new-todo">
        <input ref={labelInputRef} type="text" />
        <button onClick={onAddButtonClick}>Add</button>
      </div>
    </div>
  );
};