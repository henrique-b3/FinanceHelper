import { useEffect, useState } from "react";
import api from "../../../services/api";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import "./Goals.chart.css";

function GoalBarChart() {
  const [goals, setGoals] = useState([]);

  const formatarDinheiro = (valor) => {
    if (valor == null) return "0,00 €";
    return new Intl.NumberFormat("pt-PT", {
      style: "currency",
      currency: "EUR",
    }).format(valor);
  };

  const calcularPercentagem = (spendAmount, limitAmount) => {
    if (!limitAmount || limitAmount === 0) return 0;
    const percent = (spendAmount / limitAmount) * 100;
    return percent > 100 ? 100 : percent;
  };

  useEffect(() => {
    api
      .get("/goal/all")
      .then((answer) => {
        const data = answer.data.content || answer.data; 
        setGoals(data);
      })
      .catch((error) => showAlert("Erro ao carregar gráfico", "error"));
  }, []);

  return (
    <div className="goal-card-container glass-card">
      <h3 className="section-title">Os Meus Objetivos</h3>

      {goals.length === 0 ? (
        <p className="empty-graph-message">Sem objetivos criados. Crie um novo objetivo para começar!</p>
      ) : (
        <div className="goals-list">
          {goals.map((goal) => {
            const percentagem = calcularPercentagem(goal.spendAmount, goal.limitAmount);
            const estourouOrcamento = goal.spendAmount > goal.limitAmount;
            
            const corBarra = estourouOrcamento ? goal.color : ( "#ff4d4f" || "#007bff");

            return (
              <div key={goal.id} className="goal-item">
                <div className="goal-header">
                  <div className="goal-title-group">
                    {/*{goal.icon && <span className="goal-icon">{goal.icon}</span>}*/}
                    <div className="goal-info">
                      <h4 className="goal-name">{goal.name}</h4>
                      {goal.endDate && (
                        <span className="goal-date">
                          Até {new Date(goal.endDate).toLocaleDateString('pt-PT')}
                        </span>
                      )}
                    </div>
                  </div>
                  {goal.status && (
                    <span className={`goal-status ${goal.status.toLowerCase()}`}>
                      {goal.status}
                    </span>
                  )}
                </div>

                <div className="goal-progress-wrapper">
                  <div className="goal-progress-bar-bg">
                    <div 
                      className="goal-progress-bar-fill" 
                      style={{ 
                        width: `${percentagem}%`, 
                        backgroundColor: corBarra 
                      }}
                    ></div>
                  </div>
                </div>

                <div className="goal-footer">
                  <span className="goal-spent">
                    {formatarDinheiro(goal.spendAmount)} gastos
                  </span>
                  <span className="goal-limit">
                    de {formatarDinheiro(goal.limitAmount)}
                  </span>
                </div>
                
                {goal.remainingAmount >= 0 ? (
                  <div className="goal-remaining">
                    Restam {formatarDinheiro(goal.remainingAmount)}
                  </div>
                ) : (
                  <div className="goal-exceeded">
                    Excedeu em {formatarDinheiro(Math.abs(goal.remainingAmount))}
                  </div>
                )}
                
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
export default GoalBarChart;
