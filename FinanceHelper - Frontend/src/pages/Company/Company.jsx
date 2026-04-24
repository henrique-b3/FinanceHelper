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
  const [erro, setErro] = useState("");

  useEffect(() => {
    fetchCompanies();
  }, []);

  const fetchCompanies = () => {
    api
      .get("company/all")
      .then((answer) => {
        setCompaniesList(answer.data);
      })
      .catch((erro) => {
        console.error("Erro ao buscar transações", erro);
        setErro("Não foi possível carregar as suas transações.");
      });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Deseja mesmo apagar esta empresa?")) {
      api
        .delete("company/delete", {
          params: { companyID: id },
        })
        .then(() => {
          fetchCompanies();
          if (onCategoryUpdate) onCategoryUpdate();
        })
        .catch((erro) => console.error("Erro ao apagar categoria", erro));
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
      .catch((erro) => console.error("Erro ao pesquisar categoria", erro));
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
            <li key={t.id} className="category-item">
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
                  onClick={() => handleDelete(t.id)}
                  className="action-btn delete-btn"
                  title="Apagar"
                >
                  <img src={images.deleteItem} alt="Apagar" />
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
      <components.NewCompany
        isOpen={isModalOpen}
        company={selectedCompany}
        onClose={() => setIsModalOpen(false)}
        onSuccess={() => {
          fetchCompanies();
          //if (onCategoryUpdate) onCategoryUpdate();
        }}
      />
    </div>
  );
}

export default Company;
