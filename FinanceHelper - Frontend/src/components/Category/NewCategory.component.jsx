import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import api from "../../services/api";
import "../Modal/NewModal.css";
import * as components from "../../components";
import { useAlert } from "../../contexts/AlertContext";

function NewCategory({ isOpen, onClose, onSuccess, category = null }) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [color, setColor] = useState("#007bff");
  const [file, setFile] = useState(null);

  const { showAlert } = useAlert();

  useEffect(() => {
    if (isOpen && category) {
      setName(category.name || "");
      setDescription(category.description || "");
      setColor(category.color || "");
    } else if (isOpen && !category) {
      setName("");
      setDescription("");
      setColor("#007bff");
    }
  }, [isOpen, category]);

  if (!isOpen) return null;


  const handleCreateCategory = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      formData.append("name", name);
      if (description) formData.append("description", description);
      if (color) formData.append("color", color);
      if (file) formData.append("file", file);

      if (!category) {
        await api.post("/category", formData,);
      } else {
        await api.put(`/category/update`, formData,{
            params: { categoryID: category.id },
          },);
      }

      if (onSuccess) onSuccess();
      setName("");
      setDescription("");
      setColor("#007bff");
      setFile(null);
      onClose();
    } catch (error) {
      showAlert(error, "error");
    }
  };

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button className="closeButton" onClick={onClose}>
          ✕
        </button>

        <h2 style={{ marginTop: 0, color: "#333" }}>Criar nova categoria</h2>

        <form className="formModel" onSubmit={handleCreateCategory}>
          <label className="textLabel">
            Nome da Categoria:
            <input
              className="form-input"
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
              className="form-input"
              type="text"
              placeholder="Ex: Gastos com supermercado"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </label>

          <label className="textLabel">
            Cor:
            <input
              className="form-input"
              type="color"
              value={color}
              onChange={(e) => setColor(e.target.value)}
            />
          </label>

          <label className="textLabel">
            Ícone / Imagem:
            <input
              className="form-input"
              type="file"
              accept="image/*"
              onChange={(e) => setFile(e.target.files[0])}
              style={{ padding: "10px" }}
            />
          </label>

          <button className="btn-primary" type="submit">
            Guardar Categoria
          </button>
        </form>
      </div>
    </div>,
    document.body,
  );
}

export default NewCategory;
