import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Login, Dashboard, Category } from "./pages";
import Layout from "./components/Layout/Layout";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
      <Route element={<Layout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/category" element={<Category />} />
      </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
