import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import api from "../../services/api";
import "../Modal/NewModal.css";
import * as components from "../../components";

function NewCompany({ isOpen, onClose, onSuccess, company = null }) {
  const [name, setName] = useState("");
  const [color, setColor] = useState("#007bff");
  const [categoryID, setCategoryID] = useState("");
  const [categoriesList, setCategoriesList] = useState([]);

  const [alertConfig, setAlertConfig] = useState({
    isOpen: false,
    message: "",
    type: "error",
  });

  useEffect(() => {
    if (isOpen && company) {
      setName(company.name || "");
      setColor(company.color || "");
      setCategoryID(company.categoryID || "");
      setAlertConfig({ isOpen: false, message: "", type: "error" });
    } else if (isOpen && !company) {
      setName("");
      setColor("#007bff");
      setCategoryID("");
      setAlertConfig({ isOpen: false, message: "", type: "error" });
    }
  }, [isOpen, company]);


  useEffect(() => {
    if (isOpen) {
      api
        .get("/category/all")
        .then((answer) => {
          setCategoriesList(answer.data);
        })
        .catch((error) => {
          handleError(error);
        });
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const handleError = (error) => {
    let errorMessage = "Ocorreu um erro inesperado.";

    if (typeof error === "string") {
      errorMessage = error;
    } else if (error.response && error.response.data) {
      const data = error.response.data;

      if (data.message) {
        errorMessage = data.message;
      } else if (typeof data === "object") {
        const errors = Object.values(data);
        if (errors.length > 0) errorMessage = errors[0];
      } else if (typeof data === "string") {
        errorMessage = data;
      }
    } else if (error.message) {
      errorMessage = error.message;
    }

    setAlertConfig({ isOpen: true, message: errorMessage, type: "error" });
  };

  const handleSuccess = (successMessage) => {
    setAlertConfig({ isOpen: true, message: successMessage, type: "success" });
  };

  const handleCreateCompany = async (e) => {
    e.preventDefault();

    if (!categoryID) {
      setErro("Por favor, selecione uma Categoria para esta Empresa.");
      return;
    }

    try {
      if (!company) {
        await api.post("/company", {
          name: name,
          color: color,
          categoryID: categoryID,
        });

      } else {
        await api.put(
          "/company/update",
          {
            name: name,
            color: color,
            categoryID: categoryID,
          },
          {
            params: { companyID: company.id },
          },
        );
      }

      if (onSuccess) onSuccess();

      setName("");
      setColor("#007bff");
      setCategoryID("");
      onClose();
    } catch (error) {
      handleError(error);
    }
  };

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button className="closeButton" onClick={onClose}>
          ✕
        </button>

        <h2 style={{ marginTop: 0, color: "#333" }}>Criar nova empresa</h2>

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
              <option value="" disabled>
                {" "}
                Selecione uma categoria...
              </option>
              {categoriesList.map((categoria) => (
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
    document.body,

    <components.AlertModel
      isOpen={alertConfig.isOpen}
      title={alertConfig.type === "error" ? "Erro" : "Sucesso"}
      message={alertConfig.message}
      type={alertConfig.type}
      onClose={() => setAlertConfig({ isOpen: false, message: "" })}
    />,
  );
}

export default NewCompany;
