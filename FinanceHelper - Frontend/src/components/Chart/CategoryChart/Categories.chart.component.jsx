import { useEffect, useState } from "react";
import api from "../../../services/api";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import "./Categories.chart.css";

function GoalBar() {
  const [dadosGrafico, setDadosGrafico] = useState([]);
  
  const formatarDinheiro = (valor) => {
    return new Intl.NumberFormat('pt-PT', {
      style: 'currency',
      currency: 'EUR'
    }).format(valor);
  };

  useEffect(() => {
    api
      .get("/transaction/chartData")
      .then((resposta) => {
        setDadosGrafico(resposta.data);
      })
      .catch((erro) => console.error("Erro ao carregar gráfico", erro));
  }, []);

  return (
    <div className="categories-card-container glass-card">
      <h3 className="section-title">Gastos mensal</h3>
      
      {dadosGrafico.length === 0 ? (
        <p className="empty-graph-message">Sem dados este mês.</p>
      ) : (
        <div className="content-wrapper">
          <div className="graph-container">
            <ResponsiveContainer width="100%" height="100%" minHeight={220}>
              <PieChart>
                <Pie 
                  data={dadosGrafico} 
                  cx="50%" 
                  cy="50%" 
                  innerRadius={60} 
                  outerRadius={80} 
                  paddingAngle={5} 
                  dataKey="value"
                >
                  {dadosGrafico.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color || '#cccccc'} />
                  ))}
                </Pie>
                <Tooltip 
                  formatter={(value) => formatarDinheiro(value)}
                  contentStyle={{ backgroundColor: '#1a1a1a', border: '1px solid #333', borderRadius: '8px' }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className="legend-container">
            {dadosGrafico.map((cat, idx) => (
              <div key={idx} className="legend-item">
                <div className="legend-label-group">
                  <div 
                    className="legend-color-indicator" 
                    style={{ backgroundColor: cat.color || '#ccc' }}
                  ></div>
                  <span>{cat.name}</span>
                </div>
                <span className="legend-value">{formatarDinheiro(cat.value)}</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default GoalBar;