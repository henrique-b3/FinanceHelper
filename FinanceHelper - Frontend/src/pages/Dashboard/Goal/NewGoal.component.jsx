import { useEffect, useState } from "react";
import { createPortal } from "react-dom"
import api from "../../../services/api";
import "../NewModal.css";

function NewCompany({ isOpen, onClose }) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [icon, setIcon] = useState("");
  const [color, setColor] = useState("#007bff");
  const [limitAmount, setLimitAmount] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [categoryID, setCategoryID] = useState("");
  const [categoriasLista, setCategoriasLista] = useState([]);
  const [erro, setErro] = useState("");

  useEffect(() => {
    if (isOpen) {
      api
        .get("/category/all")
        .then((resposta) => {
          setCategoriasLista(resposta.data);
        })
        .catch((erro) => {
          console.error("Erro ao buscar categorias", erro);
          setErro("Não foi possível carregar as suas categorias.");
        });
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const handleCreateGoal = async (e) => {
    e.preventDefault();
    setErro("");

    if (!categoryID) {
      setErro("Por favor, selecione uma Categoria para esta Empresa.");
      return;
    }

    try{
        await api.post("/goal", {
            name: name,
            description: description,
            icon: icon,
            color: color,
            limitAmount: limitAmount,
            startDate: startDate,
            endDate: endDate,
            categoryID: categoryID 
        });

        alert('Objetivo criado com sucesso! 🎯');

        setName("");
        setDescription("");
        setIcon("");
        setLimitAmount("");
        setColor("#007bff");
        setStartDate("");
        setEndDate("");
        setCategoryID("");
        onClose();
    }catch(error){
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
        
        <h2 style={{marginTop: 0, color: "#333"}}>Criar novo objetivo</h2>
        {erro && <p style={{ color: "red" }}>{erro}</p>}

        <form className="formModel" onSubmit={handleCreateGoal}>
          <label className="textLabel">
            Nome do objetivo:
            <input
              className="textInput"
              type="text"
              placeholder="Ex: Gastos com mercados..."
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
              placeholder="Ex: Gastar no máximo 200€..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </label>

          <label className="textLabel">
            Valor:
            <input
              className="textInput"
              type="number"
              placeholder="200€"
              value={limitAmount}
              onChange={(e) => setLimitAmount(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Data inicio:
            <input
              className="textInput"
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </label>

          <label className="textLabel">
            Data Fim:
            <input
              className="textInput"
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </label>

          <label className="textLabel">
          Categoria Associada:
          <select 
            className="textInput"
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
              className="textInput"
              type="color" 
              value={color}
              onChange={(e) => setColor(e.target.value)}
            />
          </label>

          <button className="submitButton" type="submit">
              Guardar Objetivo
          </button>
        </form>
      </div>
    </div>,
    document.body
  );
}

export default NewCompany;
