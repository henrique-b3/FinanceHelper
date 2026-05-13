import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import * as components from "../../components";
import { images } from "../../svg";
import "./Category.css";
import { useAlert } from "../../contexts/AlertContext";

function Category() {
  const [categoriesList, setCategoriesList] = useState([]);
  const [searchCategory, setSearchCategory] = useState("");
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [analyticsCategory, setAnalyticsCategory] = useState(null);
  const [isAnalyticsOpen, setIsAnalyticsOpen] = useState(false);

  const { showAlert } = useAlert();

  const handleOpenAnalytics = (category) => {
    setAnalyticsCategory(category);
    setIsAnalyticsOpen(true);
  };

  const [confirmConfig, setConfirmConfig] = useState({
    isOpen: false,
    idToDelete: null,
  });

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = () => {
    api
      .get("category/all")
      .then((answer) => {
        setCategoriesList(answer.data);
      })
      .catch((error) => {
        showAlert(error, "error");
      });
  };

  const confirmDelete = (id) => {
    setConfirmConfig({ isOpen: true, idToDelete: id });
  };

  const executeDelete = async () => {
    try {
      await api.delete("category/delete", {
        params: { categoryID: confirmConfig.idToDelete },
      });
      setConfirmConfig({ isOpen: false, idToDelete: null });
      fetchCategories();
      showAlert("Categoria apagada com sucesso", "success");
    } catch (error) {
      setConfirmConfig({ isOpen: false, idToDelete: null });
      showAlert(error, "error");
    }
  };

  const handleSearchCategories = (e) => {
    e.preventDefault();

    if (searchCategory.trim() === "") {
      fetchCategories();
      return;
    }

    api
      .get("category/all/byName", {
        params: { categoryName: searchCategory },
      })
      .then((answer) => {
        setCategoriesList(answer.data);
      })
      .catch((error) => {
        showAlert(error, "error");
      });
  };

  const handleOpenModal = (categorie) => {
    setSelectedCategory(categorie);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedCategory(null);
    setIsModalOpen(false);
  };

  return (
    <div className="categoryContainer">
      <div className="category-header">
        <h2 className="section-title">As Minhas Categorias</h2>
        <button
          onClick={() => handleOpenModal(null)}
          className="new-category-btn"
        >
          + Nova Categoria
        </button>
      </div>

      <nav className="search-nav">
        <form onSubmit={handleSearchCategories}>
          <input
            type="text"
            value={searchCategory}
            className="search-input"
            placeholder="Pesquisar categoria..."
            onChange={(e) => {
              setSearchCategory(e.target.value);
              if (e.target.value.trim() === "") {
                fetchCategories();
              }
            }}
          />
        </form>
      </nav>

      {categoriesList.length === 0 ? (
        <div className="empty-state">
          <p>Sem categorias criadas</p>
        </div>
      ) : (
        <ul className="categories-list">
          {categoriesList.map((t) => (
            <li key={t.id} className="category-item">
              <div className="category-info-left">
                <div className="category-icon-bg">
                  <div
                    className="category-icon-bg"
                    style={{ overflow: "hidden" }}
                  >
                    {t.image &&
                    t.image !== "default.png" &&
                    t.image !== "icone_padrao.png" ? (
                      <img
                        src={`http://localhost:8080/uploads/${t.image}`}
                        alt={t.name}
                        style={{
                          width: "100%",
                          height: "100%",
                          objectFit: "cover",
                        }}
                        onError={(e) => {
                          e.target.style.display = "none";
                          e.target.nextElementSibling.style.display = "block";
                        }}
                      />
                    ) : (
                      <div
                        className="category-color-dot"
                        style={{ backgroundColor: t.color || "#007bff" }}
                      ></div>
                    )}
                  </div>
                </div>

                <div className="category-details">
                  <span className="category-name">{t.name}</span>
                  <span className="transactions-associated">
                    {t.transactionsCount || 0} despesas
                  </span>
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
                  className="action-btn edit-btn"
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
        title="Apagar Categoria"
        message="Tem a certeza que deseja apagar esta categoria? Esta ação não pode ser desfeita."
        onConfirm={executeDelete}
        onClose={() => setConfirmConfig({ isOpen: false, idToDelete: null })}
      />

      <components.NewCategory
        isOpen={isModalOpen}
        category={selectedCategory}
        onClose={() => setIsModalOpen(false)}
        onSuccess={() => {
          fetchCategories();
        }}
      />

      <components.AnalyticsModal
        isOpen={isAnalyticsOpen}
        onClose={() => setIsAnalyticsOpen(false)}
        entity={analyticsCategory}
        type="category"
      />
    </div>
  );
}

export default Category;
