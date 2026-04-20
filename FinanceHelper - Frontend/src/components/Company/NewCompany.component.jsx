import { useEffect, useState } from "react";
import { createPortal } from "react-dom"
import api from "../../services/api";
import "../Modal/NewModal.css";

function NewCompany({ isOpen, onClose }) {
  const [name, setName] = useState("");
  const [color, setColor] = useState("#007bff");
  const [categoryID, setCategoryID] = useState("");
  const [categoriasLista, setCategoriasLista] = useState([]);
  const [erro, setErro] = useState('');

  
  useEffect(() => {
    if (isOpen) {
      api.get('/category/all')
        .then(resposta => {
          setCategoriasLista(resposta.data);
        })
        .catch(erro => {
          console.error("Erro ao buscar categorias", erro);
          setErro("Não foi possível carregar as suas categorias.");
        });
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const handleCreateCompany = async (e) => {
    e.preventDefault();
    setErro("");

    if (!categoryID) {
      setErro('Por favor, selecione uma Categoria para esta Empresa.');
      return;
    }

    try {

      await api.post("/company", {
        name: name,
        color: color,
        categoryID: categoryID,
      });

      alert('Objetivo criado com sucesso! 🏢');

      setName("");
      setColor("#007bff");
      setCategoryID("");
      onClose();
    } catch (error) {
      console.error(error);
      setErro("Erro ao criar empresa. Verifique se o nome já existe.");
    }
  };

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button className="closeButton" onClick={onClose}>
          ✕
        </button>
        
        <h2 style={{marginTop: 0, color: "#333"}}>Criar nova empresa</h2>
        {erro && <p style={{ color: "red" }}>{erro}</p>}

        <form className="formModel" onSubmit={handleCreateCompany}>
          <label className="textLabel">
            Nome da empresa:
            <input
              className="form-input"
              type="text"
              placeholder="Ex: Continente, Galp, EDP..."
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
          Categoria Associada:
          <select 
            className="form-input"
            value={categoryID} 
            onChange={(e) => setCategoryID(e.target.value)}
            required
          >
            <option value="" disabled>Selecione uma categoria...</option>
            {categoriasLista.map(categoria => (
              <option key={categoria.id} value={categoria.id}>
                {categoria.name}
              </option>
            ))}
          </select>
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

          <button className="btn-primary" type="submit">
              Guardar Empresa
          </button>
        </form>
      </div>
    </div>,
    document.body
  );
}

export default NewCompany;
