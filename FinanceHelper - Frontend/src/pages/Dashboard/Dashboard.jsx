import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import "./Dashboard.css";
import * as components from "../../components";

function Dashboard() {
  const [profile, setProfile] = useState(null);
  const [greeting, setGreeting] = useState("");
  const [updateTrigger, setUpdateTrigger] = useState(0);

  const refreshData = () => setUpdateTrigger((prev) => prev + 1);
  const navigate = useNavigate();

  useEffect(() => {
    const hora = new Date().getHours();
    if (hora >= 5 && hora < 12) setGreeting("Bom dia");
    else if (hora >= 12 && hora < 18) setGreeting("Boa tarde");
    else setGreeting("Boa noite");

    api
      .get("/user")
      .then((answer) => setProfile(answer.data))
      .catch(() => {
        localStorage.removeItem("token");
        navigate("/");
      });
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  if (!profile)
    return <div className="loadingState">A carregar a sua carteira...</div>;

  return (
    <div className="mainDiv">
      <header className="welcome">
        <div>
          <p>{greeting},</p>
          <h2>
            {profile.name} {profile.lastName}
          </h2>
        </div>
        <button className="logoutBtn" onClick={handleLogout}>
          Sair
        </button>
      </header>
      <components.Menu onTransactionCreated={refreshData} />
      <div className="chartDiv">
        <components.GoalBarChart key={`graphGoals-${updateTrigger}`} />
        <components.CategoriesChart key={`graph-${updateTrigger}`} />
      </div>
      <components.AllTransactions
        key={`list-${updateTrigger}`}
        onTransactionUpdate={refreshData}
      />
    </div>
  );
}

export default Dashboard;
