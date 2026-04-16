import { useState } from "react";
import { createPortal } from "react-dom"
import api from "../../../services/api";
import "../NewModal.css";

function NewCategory({ isOpen, onClose }) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [color, setColor] = useState("#007bff");
  const [erro, setErro] = useState("");

  
  if (!isOpen) return null;

  const handleCreateCategory = async (e) => {
    e.preventDefault();
    setErro("");

    try {
      await api.post("/category", {
        name: name,
        description: description,
        color: color,
        image: "icone.png",
      });

      alert("Categoria criada com sucesso! 🎉");

      setName("");
      setDescription("");
      setColor("#007bff");
      onClose(); 
    } catch (error) {
      console.error(error);
      setErro("Erro ao criar categoria. Verifique se o nome já existe.");
    }
  };

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button className="closeButton" onClick={onClose}>
          ✕
        </button>
        
        <h2 style={{marginTop: 0, color: "#333"}}>Criar nova categoria</h2>
        {erro && <p style={{ color: "red" }}>{erro}</p>}

        <form className="formModel" onSubmit={handleCreateCategory}>
          <label className="textLabel">
            Nome da Categoria:
            <input
              className="textInput"
              type="text"
              placeholder="Ex: Alimentação, Transporte..."
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Descrição:
            <input
              className="textInput"
              type="text" 
              placeholder="Ex: Gastos com supermercado" 
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </label>

          <label className="textLabel">
            Cor:
            <input 
              className="textInput"
              type="color" 
              value={color}
              onChange={(e) => setColor(e.target.value)}
            />
          </label>

          <button className="submitButton" type="submit">
              Guardar Categoria
          </button>
        </form>
      </div>
    </div>,
    document.body
  );
}

export default NewCategory;