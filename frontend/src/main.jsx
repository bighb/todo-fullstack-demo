import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  // StrictMode 在开发环境下会将组件挂载两次 (mount -> unmount -> mount)
  // 这会导致 useEffect 执行两次，从而产生双重 API 请求
  // 这是 React 18 的预期行为，用于帮助检测副作用问题
  // 生产环境 (npm run build) 不会出现此问题
  <StrictMode>
    <App />
  </StrictMode>,
)
