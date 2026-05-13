import { useNavigate } from "react-router-dom";
import { useState } from "react";
import api from "../../services/api";
import "./Login.css";
import "../../App.css";
import { images } from "../../svg";
import * as pages from "..";
import * as components from "../../components";
import { useAlert } from "../../contexts/AlertContext";


function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isRegisterModalOpen, setIsRegisterModalOpen] = useState(false);

  const { showAlert } = useAlert();

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const resposta = await api.post("/auth/login", {
        email: email,
        password: password,
      });

      const token = resposta.data.token;
      console.log("Authenticated: " + token);

      localStorage.setItem("token", token);
      navigate("/dashboard");
    } catch (error) {
      showAlert(error, "error");
    }
  };

  return (
    <div className="login-container">
      <components.Register
        isOpen={isRegisterModalOpen}
        onClose={() => setIsRegisterModalOpen(false)}
      />
      <img className="logo" src={images.logo} alt={images.logo} />
      <div className="login-card">
        <h2>Login</h2>

        <form onSubmit={handleLogin} className="login-form">
          <div className="input-group">
            <label>Email</label>
            <input
              className="form-input"
              type="email"
              placeholder="O seu Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <label>Password</label>
            <input
              className="form-input"
              type="password"
              placeholder="A sua Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn-primary">
            Entrar
          </button>
        </form>
        <div
          className="createAccButton"
          onClick={() => setIsRegisterModalOpen(true)}
        >
          <p>Criar conta</p>
        </div>
      </div>
    </div>
  );
}

export default Login;
