import { useEffect, useState } from "react";
import api from "../../services/api";
import { createPortal } from "react-dom";
import "./GetAllTransactions.css";
import NewTransaction from "./NewTransaction.component";

function GetAllTransaction({ onTransactionUpdate }) {
  const [transactionsList, setTransactionsList] = useState([]);
  const [erro, setErro] = useState("");
  const [limitAmount, setLimitAmount] = useState(5);

  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchTransactions = () => {
    api
      .get("/transaction/all", {
        params: {
          limit: limitAmount,
        }
      })
      .then((resposta) => {
        setTransactionsList(resposta.data);
      })
      .catch((erro) => {
        console.error("Erro ao buscar transações", erro);
        setErro("Não foi possível carregar as suas transações.");
      });
  };

  useEffect(() => {
    fetchTransactions();
  }, [limitAmount]);

  const handleOpenModal = (transaction) => {
    setSelectedTransaction(transaction);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedTransaction(null);
    setIsModalOpen(false);
  };

  const handleSuccess = () => {
    fetchTransactions(); 
    handleCloseModal();
  };

  return (
    <div className="transactions-container glass-card">
      <div className="transactions-header">
        <h3 className="section-title">Transações Recentes</h3>
      </div>

      {erro && <div className="error-message">{erro}</div>}

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
      <NewTransaction 
        isOpen={isModalOpen}
        transaction={selectedTransaction} 
        onClose={() => setIsModalOpen(false)}
        onSuccess={() => {
          fetchTransactions();
          if (onTransactionUpdate) onTransactionUpdate();
        }}
      />
    </div>
  );
}

export default GetAllTransaction;
