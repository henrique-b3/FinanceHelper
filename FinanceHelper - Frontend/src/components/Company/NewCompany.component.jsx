import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import api from "../../services/api";
import "../Modal/NewModal.css";
import * as components from "../../components";
import { useAlert } from "../../contexts/AlertContext";

function NewCompany({ isOpen, onClose, onSuccess, company = null }) {
  const [name, setName] = useState("");
  const [color, setColor] = useState("#007bff");
  const [categoryID, setCategoryID] = useState("");
  const [categoriesList, setCategoriesList] = useState([]);

  const { showAlert } = useAlert();

  useEffect(() => {
    if (isOpen && company) {
      setName(company.name || "");
      setColor(company.color || "");
      setCategoryID(company.categoryID || "");
    } else if (isOpen && !company) {
      setName("");
      setColor("#007bff");
      setCategoryID("");
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
          showAlert(error, "error");
        });
    }
  }, [isOpen]);

  if (!isOpen) return null;


  const handleCreateCompany = async (e) => {
    e.preventDefault();

    if (!categoryID) {
      showAlert("Por favor, selecione uma Categoria para esta Empresa.", "error");
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
      showAlert(error, "error");
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
  );
}

export default NewCompany;
