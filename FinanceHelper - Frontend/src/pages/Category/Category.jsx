import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import * as components from "../../components";
import { images } from "../../svg";
import "./Category.css";

function Category({onCategoryUpdate}) {
  const [categoriesList, setCategoriesList] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [erro, setErro] = useState("");

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = () => {
    api
      .get("category/all")
      .then((resposta) => {
        setCategoriesList(resposta.data);
      })
      .catch((erro) => {
        console.error("Erro ao buscar transações", erro);
        setErro("Não foi possível carregar as suas transações.");
      });
  };


  const handleDelete = async (id) => {
      if (window.confirm("Deseja mesmo apagar esta categoria?")) {
        api
        .delete("category/delete",{
          params: { categoryID: id },
        })
        .then(() => {
            fetchCategories();
            if (onCategoryUpdate) onCategoryUpdate();
        })
        .catch((erro) => console.error("Erro ao apagar categoria", erro));
      }
  }

  const handleOpenModal = (categorie) => {
      setSelectedCategory(categorie);
      setIsModalOpen(true)
  }

  const handleCloseModal = () => {
    setSelectedCategory(null);
    setIsModalOpen(false);
  };

 return (
    <div className="categoryContainer">
      <div className="category-header">
        <h2 className="section-title">As Minhas Categorias</h2>
        <button onClick={() => handleOpenModal(null)} className="new-category-btn">+ Nova Categoria</button>
      </div>

      <nav className="search-nav">
        <input 
          type="text" 
          className="search-input" 
          placeholder="Pesquisar categoria..." 
        />
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
                                className="category-color-dot" 
                                style={{ backgroundColor: t.color || '#007bff' }}
                            ></div>
                        </div>

                        <div className="category-details">
                            <span className="category-name">{t.name}</span>
                            <span className="transactions-associated">
                                {t.transactionsCount || 0} despesas
                            </span>
                        </div>
                    </div>

                    <div className="category-actions">
                        <button onClick={() => handleOpenModal(t)} className="action-btn edit-btn" title="Editar">
                            <img src={images.edit} alt="Editar" />
                        </button>
                        <button onClick={() => handleDelete(t.id)} className="action-btn delete-btn" title="Apagar">
                            <img src={images.deleteItem} alt="Apagar" />
                        </button>
                    </div>
                </li>
            ))}
        </ul>
      )}
      <components.NewCategory 
        isOpen={isModalOpen}
        category={selectedCategory} 
        onClose={() => setIsModalOpen(false)}
        onSuccess={() => {
          fetchCategories();
          if (onCategoryUpdate) onCategoryUpdate();
        }}
      />
    </div>
  );
}

export default Category;
