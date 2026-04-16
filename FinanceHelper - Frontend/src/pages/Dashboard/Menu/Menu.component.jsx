import { useEffect, useState } from "react";
import api from "../../../services/api";
import "../Dashboard.css";
import { images } from "../../../svg";
import * as pages from "../..";

function Menu({ onTransactionCreated }) {
  const [totalAmount, setTotalAmount] = useState(0);
  const [isCategoryModalOpen, setIsCategoryModalOpen] = useState(false);
  const [isCompanyModalOpen, setIsCompanyModalOpen] = useState(false);
  const [isTransactionModalOpen, setIsTransactionModalOpen] = useState(false);
  const [isGoalModalOpen, setIsGoalModalOpen] = useState(false);

  useEffect(() => {
    api
      .get("/transaction/totalMonth")
      .then((resposta) => {
        setTotalAmount(resposta.data);
      })
      .catch((erro) => {
        console.error("Erro ao buscar categorias", erro);
        setErro("Não foi possível carregar as suas categorias.");
      });
  });

  return(
    <div className="menu">
      <pages.NewCategory
        isOpen={isCategoryModalOpen}
        onClose={() => setIsCategoryModalOpen(false)}
      />
      <pages.NewCompany
        isOpen={isCompanyModalOpen}
        onClose={() => setIsCompanyModalOpen(false)}
      />
      <pages.NewGoal
        isOpen={isGoalModalOpen}
        onClose={() => setIsGoalModalOpen(false)}
      />
      <pages.NewTransaction
        isOpen={isTransactionModalOpen}
        onClose={() => setIsTransactionModalOpen(false)}
        onSuccess={onTransactionCreated}
      />
      <header className="menuTop">
        <div className="balanceText">
          <h1>Total gasto este mês</h1>
          <p>€ {totalAmount}</p>
        </div>

        <button className="menuBalanceButton">
          <img src={images.eyeShow} alt="mostrar saldo" />
        </button>
      </header>

      <nav className="menuBottom">
        <div className="actions">
          <div onClick={() => setIsTransactionModalOpen(true)}>
            <BotaoAcao
              icone={images.transaction}
              texto="Despesa"
              cor="#ffeaea"
            />
          </div>
          <div onClick={() => setIsCategoryModalOpen(true)}>
            <BotaoAcao icone={images.goal} texto="Categoria" cor="#eaf2ff" />
          </div>
          <div onClick={() => setIsCompanyModalOpen(true)}>
            <BotaoAcao icone={images.store} texto="Empresa" cor="#f4eaff" />
          </div>
          <div onClick={() => setIsGoalModalOpen(true)}>
            <BotaoAcao
              icone={images.categories}
              texto="Objetivo"
              cor="#eaffea"
            />
          </div>
        </div>
      </nav>
    </div>
  );
}

function BotaoAcao({ icone, texto, cor }) {
  return (
    <div className="containerBotaoAcao">
      <div className="iconeWrapper" style={{ backgroundColor: cor }}>
        <img src={icone} alt={texto} />
      </div>
      <span className="textoAcao">{texto}</span>
    </div>
  );
}

export default Menu;
