import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Login, Dashboard, NewCategory, NewCompany } from "./pages";
import './index.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App