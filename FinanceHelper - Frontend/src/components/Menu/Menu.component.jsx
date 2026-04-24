import { useEffect, useState } from "react";
import api from "../../services/api";
import "./Menu.css";
import "../../pages/Dashboard/Dashboard.css"
import { images } from "../../svg";

function Menu({ onTransactionCreated }) {
  const [totalAmount, setTotalAmount] = useState(0);
  const [goals, setGoals] = useState(0);
  const [transactions, setTransactions] = useState(0);
  const [showAmount, setShowAmount] = useState(true);

  const displayedAmount = showAmount ? totalAmount.toFixed(2) : "****";

  useEffect(() => {
    api
      .get("/transaction/totalMonth")
      .then((answer) => {
        setTotalAmount(answer.data);
      })
      .catch((erro) => {
        console.error("Erro ao buscar total", erro);
      });

    api
      .get("/transaction/count")
      .then((answer) => {
        setTransactions(answer.data);
      })
      .catch((erro) => {
        console.error("Erro ao buscar total", erro);
      });
  }, [onTransactionCreated]);

  useEffect(() => {
    api
      .get("/goal/status")
      .then((answer) => {
        setGoals(answer.data);
      })
      .catch((erro) => {
        console.error("Erro ao buscar total", erro);
      });
  }, []);
  

  return (
    <div className="menu-container">
      <nav className="menuTop">
        <div className="insight-card highlight-card">
          <div className="insight-header">
            <h3>TOTAL GASTO</h3>
            <div className="icon-wrapper">
              <img src={images.transaction} alt="Transação" />
            </div>
          </div>
          <div className="insight-valueView">
            <p className="insight-value">€ {displayedAmount}</p>
            <button
              className="toggle-amount-btn"
              onClick={() => setShowAmount(!showAmount)}
              title={showAmount ? "Ocultar valores" : "Mostrar valores"}
            >
              <img
                src={showAmount ? images.eyeOff : images.eyeShow}
                alt="Alternar visibilidade"
              />
            </button>
          </div>
        </div>

        <div className="insight-card">
          <div className="insight-header">
            <h3>DESPESAS</h3>
            <div className="icon-wrapper">
              <img src={images.transaction} alt="Despesas" />
            </div>
          </div>
          <p className="insight-value">{transactions}</p>
        </div>

        <div className="insight-card">
          <div className="insight-header">
            <h3>OBJETIVOS</h3>
            <div className="icon-wrapper">
              <img src={images.goal} alt="Objetivos" />
            </div>
          </div>
          <p>Atuais/Finalizados/Total</p>
          <p className="insight-value">{goals.current}/{goals.finished}/{goals.total}</p>
        </div>
      </nav>
    </div>
  );
}

export default Menu;
