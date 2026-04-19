import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import "./Dashboard.css";
import * as pages from "..";

function Dashboard() {
  const [perfil, setPerfil] = useState(null);
  const [saudacao, setSaudacao] = useState("");
  const navigate = useNavigate();
  const [updateTrigger, setUpdateTrigger] = useState(0);
  const [erro, setErro] = useState("");
  const refreshData = () => setUpdateTrigger((prev) => prev + 1);

  useEffect(() => {
    const hora = new Date().getHours();
    if (hora >= 5 && hora < 12) setSaudacao("Bom dia");
    else if (hora >= 12 && hora < 18) setSaudacao("Boa tarde");
    else setSaudacao("Boa noite");

    api
      .get("/user")
      .then((resposta) => setPerfil(resposta.data))
      .catch(() => {
        localStorage.removeItem("token");
        navigate("/");
      });
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  if (!perfil)
    return <div className="loadingState">A carregar a sua carteira...</div>;

  return (
    <div className="mainDiv">
      <pages.NavMenu/>
      <header className="welcome">
        <div>
          <p>{saudacao},</p>
          <h2>
            {perfil.name} {perfil.lastName}
          </h2>
        </div>
        <button className="logoutBtn" onClick={handleLogout}>
          Sair
        </button>
      </header>
      <pages.Menu onTransactionCreated={refreshData} />
      <pages.GoalBarChart key={`graph-${updateTrigger}`} />
      <pages.AllTransactions
        key={`list-${updateTrigger}`}
        onTransactionUpdate={refreshData}
      />
    </div>
  );
}

export default Dashboard;
