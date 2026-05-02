import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import * as components from "../../components";
import { images } from "../../svg";
import "../Category/Category.css";

function Company() {
  const [companiesList, setCompaniesList] = useState([]);
  const [searchCompany, setSearchCompany] = useState([]);
  const [selectedCompany, setSelectedCompany] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [analyticsCategory, setAnalyticsCategory] = useState(null);
  const [isAnalyticsOpen, setIsAnalyticsOpen] = useState(false);

  const handleOpenAnalytics = (category) => {
    setAnalyticsCategory(category);
    setIsAnalyticsOpen(true);
  };

  const [confirmConfig, setConfirmConfig] = useState({
    isOpen: false,
    idToDelete: null,
  });

  const [alertConfig, setAlertConfig] = useState({
    isOpen: false,
    message: "",
    type: "error",
  });

  useEffect(() => {
    fetchCompanies();
  }, []);

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

  const fetchCompanies = () => {
    api
      .get("company/all")
      .then((answer) => {
        setCompaniesList(answer.data);
      })
      .catch((error) => {
        handleError(error);
      });
  };

  const confirmDelete = (id) => {
    setConfirmConfig({ isOpen: true, idToDelete: id });
  };

  const executeDelete = async () => {
    try {
      await api.delete("company/delete", {
        params: { companyID: confirmConfig.idToDelete },
      });
      setConfirmConfig({ isOpen: false, idToDelete: null });
      fetchCompanies();
      handleSuccess("Categoria apagada com sucesso");
    } catch (error) {
      setConfirmConfig({ isOpen: false, idToDelete: null });
      console.error("Erro ao apagar categoria", error);
      handleError(error);
    }
  };

  const handleSearchCategories = (e) => {
    e.preventDefault();

    if (searchCompany.trim() === "") {
      fetchCompanies();
      return;
    }

    api
      .get("company/all/byName", {
        params: { companyName: searchCompany },
      })
      .then((answer) => {
        setCompaniesList(answer.data);
      })
      .catch((error) => {
        handleError(error);
      });
  };

  const handleOpenModal = (company) => {
    setSelectedCompany(company);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedCompany(null);
    setIsModalOpen(false);
  };

  return (
    <div className="categoryContainer">
      <div className="category-header">
        <h2 className="section-title">As Minhas Empresas</h2>
        <button
          onClick={() => handleOpenModal(null)}
          className="new-category-btn"
        >
          + Nova Empresa
        </button>
      </div>

      <nav className="search-nav">
        <form onSubmit={handleSearchCategories}>
          <input
            type="text"
            value={searchCompany}
            className="search-input"
            placeholder="Pesquisar categoria..."
            onChange={(e) => {
              setSearchCompany(e.target.value);
              if (e.target.value.trim() === "") {
                fetchCompanies();
              }
            }}
          />
        </form>
      </nav>

      {companiesList.length === 0 ? (
        <div className="empty-state">
          <p>Sem empresas criadas</p>
        </div>
      ) : (
        <ul className="categories-list">
          {companiesList.map((t) => (
            <li
              key={t.id}
              className="category-item"
            >
              <div className="category-info-left">
                <div className="category-icon-bg">
                  <div
                    className="category-color-dot"
                    style={{ backgroundColor: t.color || "#007bff" }}
                  ></div>
                </div>

                <div className="category-details">
                  <span className="category-name">{t.name}</span>
                </div>
              </div>

              <div className="category-actions">
                <button
                  onClick={() => handleOpenModal(t)}
                  className="action-btn edit-btn"
                  title="Editar"
                >
                  <img src={images.edit} alt="Editar" />
                </button>
                <button
                  onClick={() => confirmDelete(t.id)}
                  className="action-btn delete-btn"
                  title="Apagar"
                >
                  <img src={images.deleteItem} alt="Apagar" />
                </button>
                <button
                  onClick={() => handleOpenAnalytics(t)}
                  className="action-btn"
                  title="Estatísticas"
                >
                  <img src={images.stats} alt="Stats" />
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}

      <components.ConfirmModel
        isOpen={confirmConfig.isOpen}
        title="Apagar Empresa"
        message="Tem a certeza que deseja apagar esta empresa? Esta ação não pode ser desfeita."
        onConfirm={executeDelete}
        onClose={() => setConfirmConfig({ isOpen: false, idToDelete: null })}
      />

      <components.AlertModel
        isOpen={alertConfig.isOpen}
        title={alertConfig.type === "error" ? "Erro" : "Sucesso"}
        message={alertConfig.message}
        type={alertConfig.type}
        onClose={() => setAlertConfig({ isOpen: false, message: "" })}
      />

      <components.NewCompany
        isOpen={isModalOpen}
        company={selectedCompany}
        onClose={() => setIsModalOpen(false)}
        onSuccess={() => {
          fetchCompanies();
          handleSuccess("Operação realizada com sucesso!");
        }}
      />

      <components.AnalyticsModal
        isOpen={isAnalyticsOpen}
        onClose={() => setIsAnalyticsOpen(false)}
        entity={analyticsCategory}
        type="company"
      />
    </div>
  );
}

export default Company;
