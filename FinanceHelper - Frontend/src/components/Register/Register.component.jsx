import { useNavigate } from "react-router-dom";
import { createPortal } from "react-dom";
import { useState } from "react";
import api from "../../services/api";
import "../Modal/NewModal.css";
import { useAlert } from "../../contexts/AlertContext";

function Register({ isOpen, onClose }) {
  const [name, setName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const navigate = useNavigate();
  const { showAlert } = useAlert();

  if (!isOpen) return null;

  const handleRegister = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      showAlert("As senhas não coincidem", "error");
      return;
    }

    try {
      await api.post("/auth/register", {
        name: name,
        lastName: lastName,
        email: email,
        password: password,
      });

      navigate("/login");
      showAlert("Conta criada com sucesso! Faça login", "success");
      clearData();
      onClose();
    } catch (error) {
      showAlert(error, "error");
      console.error(error);
    }
  };

  const clearData = () => {
    setName("");
    setLastName("");
    setEmail("");
    setPassword("");
    setConfirmPassword("");
  };

  return createPortal(
    <div
      className="modalOverlay"
      onClick={() => {
        clearData();
        onClose();
      }}
    >
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button
          className="closeButton"
          onClick={() => {
            clearData();
            onClose();
          }}
        >
          ✕
        </button>

        <h2 style={{ marginTop: 0, color: "#333" }}>Criar conta</h2>

        <form className="formModel" onSubmit={handleRegister}>
          <label className="textLabel">
            Nome:
            <input
              className="form-input"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Sobrenome:
            <input
              className="form-input"
              type="text"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Email:
            <input
              className="form-input"
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Senha:
            <input
              className="form-input"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Confirmar senha:
            <input
              className="form-input"
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </label>

          <button className="btn-primary" type="submit">
            Criar conta
          </button>
        </form>
      </div>
    </div>,
    document.body,
  );
}

export default Register;
