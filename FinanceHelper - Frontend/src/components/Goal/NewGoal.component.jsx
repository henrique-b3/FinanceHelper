import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import api from "../../services/api";
import "../Modal/NewModal.css";

function NewGoal({ isOpen, onClose, onSuccess, goal = null }) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [icon, setIcon] = useState("");
  const [color, setColor] = useState("#007bff");
  const [limitAmount, setLimitAmount] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  
  // Estados para Categoria e Empresa
  const [categoryID, setCategoryID] = useState("");
  const [companyID, setCompanyID] = useState("");
  
  const [categoriesList, setCategoriesList] = useState([]);
  const [companiesList, setCompaniesList] = useState([]); // Adicionado estado para empresas
  const [erro, setErro] = useState("");

  useEffect(() => {
    if (isOpen) {
      // Carregar Categorias
      api
        .get("/category/all")
        .then((answer) => setCategoriesList(answer.data))
        .catch((erro) => console.error("Erro ao buscar categorias", erro));

      // Carregar Empresas
      api
        .get("/company/all")
        .then((answer) => setCompaniesList(answer.data))
        .catch((erro) => console.error("Erro ao buscar empresas", erro));
    }
  }, [isOpen]);

  useEffect(() => {
    if (isOpen && goal) {
      setName(goal.name || "");
      setDescription(goal.description || "");
      setIcon(goal.icon || "");
      setColor(goal.color || "");
      setLimitAmount(goal.limitAmount || "");
      setStartDate(goal.startDate || "");
      setEndDate(goal.endDate || "");
      setCategoryID(goal.categoryID || "");
      setCompanyID(goal.companyID || ""); // Preencher empresa se existir
    } else if (isOpen && !goal) {
      // Limpar formulário ao abrir para criar novo
      setName("");
      setDescription("");
      setIcon("");
      setLimitAmount("");
      setColor("#007bff");
      setStartDate("");
      setEndDate("");
      setCategoryID("");
      setCompanyID("");
      setErro("");
    }
  }, [isOpen, goal]);

  if (!isOpen) return null;

  const handleCreateGoal = async (e) => {
    e.preventDefault();
    setErro("");

    // Validação: Tem de escolher pelo menos um
    if (!categoryID && !companyID) {
      setErro("Por favor, associe o objetivo a uma Categoria ou a uma Empresa.");
      return;
    }

    try {
      const payload = {
        name,
        description,
        icon,
        color,
        limitAmount,
        startDate,
        endDate,
        categoryID: categoryID !== "" ? categoryID : null,
        companyID: companyID !== "" ? companyID : null,
      };

      if (!goal) {
        await api.post("/goal", payload);
      } else {
        await api.put("/goal/update", payload, {
          params: { goalID: goal.id },
        });
      }

      if (onSuccess) onSuccess();
      onClose();
    } catch (error) {
      console.error(error);
      setErro("Erro ao guardar objetivo. Verifique os dados e tente novamente.");
    }
  };

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button className="closeButton" onClick={onClose}>
          &times;
        </button>

        <h2 style={{ marginTop: 0, color: "white" }}>
          {goal ? "Editar objetivo" : "Criar novo objetivo"}
        </h2>
        {erro && <p style={{ color: "#ff4d4f", fontSize: "0.9rem" }}>{erro}</p>}

        <form className="formModel" onSubmit={handleCreateGoal}>
          <label className="textLabel">
            Nome do objetivo:
            <input
              className="form-input"
              type="text"
              placeholder="Ex: Gastos com mercados..."
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </label>

          <div style={{ display: "flex", gap: "10px" }}>
            <label className="textLabel" style={{ flex: 1 }}>
              Data Início:
              <input
                className="form-input"
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
              />
            </label>
            <label className="textLabel" style={{ flex: 1 }}>
              Data Fim:
              <input
                className="form-input"
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
            </label>
          </div>

          <label className="textLabel">
            Limite Gasto (€):
            <input
              className="form-input"
              type="number"
              step="0.01"
              placeholder="Ex: 200"
              value={limitAmount}
              onChange={(e) => setLimitAmount(e.target.value)}
              required
            />
          </label>

          <hr style={{ borderColor: "rgba(255,255,255,0.1)", margin: "10px 0" }} />
          <p style={{ fontSize: "0.85rem", color: "rgba(255,255,255,0.6)", margin: 0 }}>
            Associe a uma Categoria OU a uma Empresa:
          </p>

          <label className="textLabel" style={{ opacity: companyID ? 0.5 : 1 }}>
            Categoria Associada:
            <select
              className="form-input"
              value={categoryID}
              onChange={(e) => {
                setCategoryID(e.target.value);
                setCompanyID(""); // Limpa a empresa ao escolher categoria
              }}
              disabled={companyID !== ""} // Desativa se empresa estiver selecionada
            >
              <option value="">Nenhuma categoria...</option>
              {categoriesList.map((categoria) => (
                <option key={categoria.id} value={categoria.id}>
                  {categoria.name}
                </option>
              ))}
            </select>
          </label>

          <label className="textLabel" style={{ opacity: categoryID ? 0.5 : 1 }}>
            Empresa Associada:
            <select
              className="form-input"
              value={companyID}
              onChange={(e) => {
                setCompanyID(e.target.value);
                setCategoryID(""); // Limpa a categoria ao escolher empresa
              }}
              disabled={categoryID !== ""} // Desativa se categoria estiver selecionada
            >
              <option value="">Nenhuma empresa...</option>
              {companiesList.map((company) => (
                <option key={company.id} value={company.id}>
                  {company.name}
                </option>
              ))}
            </select>
          </label>

          <hr style={{ borderColor: "rgba(255,255,255,0.1)", margin: "10px 0" }} />

          <label className="textLabel">
            Cor:
            <input
              className="form-input"
              type="color"
              value={color}
              onChange={(e) => setColor(e.target.value)}
            />
          </label>

          <button className="btn-primary" type="submit" style={{ marginTop: "10px" }}>
            Guardar Objetivo
          </button>
        </form>
      </div>
    </div>,
    document.body
  );
}

export default NewGoal;