import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import * as components from "../../components";
// import { images } from "../../svg";
import "./Transaction.css";
import { useAlert } from "../../contexts/AlertContext";

function Transaction() {
  const [transactionsList, setTransactionsList] = useState([]);
  const [companiesList, setCompaniesList] = useState([]);
  const [categoriesList, setCategoriesList] = useState([]);

  const [selectedTransaction, setSelectedTransaction] = useState([null]);

  const [searchTransaction, setSearchTransaction] = useState("");
  const [categoryID, setCategoryID] = useState("");
  const [companyID, setCompanyID] = useState("");

  const [sortOption, setSortOption] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false);

  const { showAlert } = useAlert();

  const ORDERBY_OPTIONS = [
    { value: "data-asc", label: "Data (antigo)" },
    { value: "data-desc", label: "Data (novo)" },
    { value: "valor-desc", label: "Valor (mais alto)" },
    { value: "valor-asc", label: "Valor (mais baixo)" },
  ];


  useEffect(() => {
    api.get("/category/all")
      .then((answer) => setCategoriesList(answer.data))
      .catch((error) => showAlert(error, "error"));

    api.get("/company/all")
      .then((answer) => setCompaniesList(answer.data))
      .catch((error) => showAlert(error, "error"));

    fetchFilteredTransactions();
  }, []);

  const fetchFilteredTransactions = () => {
    const params = {
      page: 0,
      size: 20
    };

    if (searchTransaction.trim() !== "") params.description = searchTransaction;
    if (categoryID !== "") params.categoryID = categoryID;
    if (companyID !== "") params.companyID = companyID;


    if (sortOption !== "") {
      const [orderBy, direction] = sortOption.split("-");
      params.orderBy = orderBy;
      params.direction = direction;
    }

    api.get("/transaction/filter", { params })
      .then((answer) => {
        setTransactionsList(answer.data.content);
      })
      .catch((error) => {
        showAlert(error, "error");
      });
  };


  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchFilteredTransactions();
  };

  const handleOpenModal = (transaction) => {
    setSelectedTransaction(transaction);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedTransaction(null);
    setIsModalOpen(false);
  };

  return (
    <div className="categoryContainer">
      <div className="category-header">
        <h2 className="section-title">As Minhas Transações</h2>
        <button onClick={() => handleOpenModal(null)} className="new-category-btn">
          + Nova Transação
        </button>
      </div>

      <nav className="search-nav">
        <form onSubmit={handleSearchSubmit} style={{ display: 'flex', gap: '15px', flexWrap: 'wrap' }}>

          <input
            type="text"
            value={searchTransaction}
            className="search-input"
            placeholder="Pesquisar transação..."
            onChange={(e) => setSearchTransaction(e.target.value)}
          />

          <label className="textLabel">
            Categoria:
            <select
              className="form-input"
              value={categoryID}
              onChange={(e) => setCategoryID(e.target.value)}
            >
              <option value="">Todas as categorias</option>
              {categoriesList.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </label>

          <label className="textLabel">
            Empresa:
            <select
              className="form-input"
              value={companyID}
              onChange={(e) => setCompanyID(e.target.value)}
            >
              <option value="">Todas as empresas</option>
              {companiesList.map((company) => (
                <option key={company.id} value={company.id}>
                  {company.name}
                </option>
              ))}
            </select>
          </label>

          <label className="textLabel">
            Ordenar por:
            <select
              className="form-input"
              value={sortOption}
              onChange={(e) => setSortOption(e.target.value)}
            >
              <option value="">Padrão</option>
              {ORDERBY_OPTIONS.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </label>

          <button type="submit" className="search-btn">Filtrar</button>

          <button type="button" onClick={() => {
            setSearchTransaction("");
            setCategoryID("");
            setCompanyID("");
            setSortOption("");
            setTimeout(() => fetchFilteredTransactions(), 0);
          }}>
            Limpar
          </button>
        </form>
      </nav>


      <div className="transactions-container glass-card">
        <div className="transactions-header">
          <h3 className="section-title">Transações Recentes</h3>
        </div>

        {transactionsList.length === 0 ? (
          <div className="empty-state">
            <p>Ainda não registou nada este mês. 🧊</p>
          </div>
        ) : (
          <ul className="transaction-list">
            {transactionsList.map((t) => (
              <li key={t.id} className="transaction-item" onClick={() => handleOpenModal(t)}>
                <div className="transaction-info">
                  <div
                    className="transaction-icon"
                    style={{
                      background: t.categoryColor
                        ? `${t.categoryColor}33`
                        : "rgba(255, 255, 255, 0.1)",
                    }}
                  >
                    💳
                  </div>

                  <div className="transaction-details">
                    <span className="transaction-name">
                      {t.companyName || t.categoryName || "Despesa"}
                    </span>
                    <span className="transaction-date">
                      {t.categoryName} •{" "}
                      {new Date(t.transactionDate).toLocaleDateString("pt-PT")}
                    </span>
                  </div>
                </div>

                <span className="transaction-amount">- {t.amount} €</span>
              </li>
            ))}
          </ul>
        )}
        <components.NewTransaction
          isOpen={isModalOpen}
          transaction={selectedTransaction}
          onClose={() => setIsModalOpen(false)}
          onSuccess={() => {
            fetchFilteredTransactions();
          }}
        />

      </div>
    </div>
  );
}

export default Transaction;