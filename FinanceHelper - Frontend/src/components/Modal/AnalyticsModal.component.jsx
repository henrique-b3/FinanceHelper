import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
} from "recharts";
import api from "../../services/api";
import "./NewModal.css";
import { useAlert } from "../../contexts/AlertContext";

function AnalyticsModal({ isOpen, onClose, entity, type }) {
  const [chartData, setChartData] = useState([]);
  const [totalSpent, setTotalSpent] = useState(0);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const { showAlert } = useAlert();

  useEffect(() => {
    if (isOpen) {
      const today = new Date();

      const formatLocal = (date) => {
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, "0");
        const d = String(date.getDate()).padStart(2, "0");
        return `${y}-${m}-${d}`;
      };

      const firstDay = new Date(today.getFullYear(), today.getMonth() - 1, 1);
      const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

      setStartDate(formatLocal(firstDay));
      setEndDate(formatLocal(lastDay));
    }
  }, [isOpen]);

  useEffect(() => {
    if (isOpen && entity && startDate && endDate) {
      const delayDebounceFn = setTimeout(() => {
        fetchAnalyticsData();
      }, 500);

      return () => clearTimeout(delayDebounceFn);
    }
  }, [isOpen, entity, startDate, endDate]);

  if (!isOpen || !entity) return null;

  const fetchAnalyticsData = async () => {
    setIsLoading(true);
    try {
      const params = { startDate, endDate, size: 1000 };
      if (type === "category") params.categoryID = entity.id;
      if (type === "company") params.companyID = entity.id;

      const response = await api.get("/transaction/filter", { params });

      let transactions = [];
      if (response.data && Array.isArray(response.data.content)) {
        transactions = response.data.content; // Suporta Paginação
      } else if (Array.isArray(response.data)) {
        transactions = response.data; // Suporta Lista Direta
      }

      processChartData(transactions);
    } catch (error) {
      showAlert(error, "error");
      setTotalSpent(0);
      setChartData([]);
    } finally {
      setIsLoading(false);
    }
  };

  const processChartData = (transactions) => {
    let total = 0;
    const groupedData = {};

    transactions.forEach((t) => {
      const val = parseFloat(t.amount) || 0;
      total += val;

      const dateObj = new Date(t.transactionDate);
      const dateStr = isNaN(dateObj)
        ? "Desconhecido"
        : dateObj.toLocaleDateString("pt-PT", {
            day: "2-digit",
            month: "short",
          });

      if (groupedData[dateStr]) {
        groupedData[dateStr] += val;
      } else {
        groupedData[dateStr] = val;
      }
    });

    const chartArray = Object.keys(groupedData).map((date) => ({
      date,
      value: groupedData[date],
    }));

    setTotalSpent(total);
    setChartData(chartArray);
  };

  const formatarDinheiro = (valor) => {
    return new Intl.NumberFormat("pt-PT", {
      style: "currency",
      currency: "EUR",
    }).format(valor);
  };

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div
        className="modalContent"
        onClick={(e) => e.stopPropagation()}
        style={{ maxWidth: "600px", width: "95%" }}
      >
        <button className="closeButton" onClick={onClose}>
          &times;
        </button>

        <h2 style={{ marginTop: 0, color: "white" }}>
          Análise:{" "}
          <span style={{ color: entity.color || "#007bff" }}>
            {entity.name}
          </span>
        </h2>

        <div style={{ display: "flex", gap: "10px", marginBottom: "20px" }}>
          <label className="textLabel" style={{ flex: 1 }}>
            De:
            <input
              className="form-input"
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </label>
          <label className="textLabel" style={{ flex: 1 }}>
            Até:
            <input
              className="form-input"
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </label>
        </div>

        <div
          style={{
            background: "rgba(0,0,0,0.2)",
            padding: "15px",
            borderRadius: "12px",
            marginBottom: "20px",
          }}
        >
          <p
            style={{
              margin: 0,
              fontSize: "0.9rem",
              color: "rgba(255,255,255,0.7)",
            }}
          >
            Total Gasto no Período:
          </p>
          <h3
            style={{ margin: "5px 0 0 0", fontSize: "1.8rem", color: "white" }}
          >
            {formatarDinheiro(totalSpent)}
          </h3>
        </div>

        {isLoading ? (
          <p style={{ textAlign: "center", color: "rgba(255,255,255,0.5)" }}>
            A carregar gráfico...
          </p>
        ) : chartData.length === 0 ? (
          <p
            style={{
              textAlign: "center",
              color: "rgba(255,255,255,0.5)",
              padding: "20px 0",
            }}
          >
            Sem gastos registados neste período.
          </p>
        ) : (
          <div style={{ height: "250px", width: "100%" }}>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={chartData}>
                <CartesianGrid
                  strokeDasharray="3 3"
                  stroke="rgba(255,255,255,0.1)"
                  vertical={false}
                />
                <XAxis
                  dataKey="date"
                  stroke="rgba(255,255,255,0.5)"
                  fontSize={12}
                  tickLine={false}
                  axisLine={false}
                />
                <YAxis
                  stroke="rgba(255,255,255,0.5)"
                  fontSize={12}
                  tickLine={false}
                  axisLine={false}
                  tickFormatter={(val) => `€${val}`}
                />
                <Tooltip
                  cursor={{ fill: "rgba(255,255,255,0.05)" }}
                  contentStyle={{
                    backgroundColor: "#1a1a1a",
                    border: "1px solid #333",
                    borderRadius: "8px",
                    color: "white",
                  }}
                  itemStyle={{ color: "#ffffff" }} 
                  labelStyle={{ color: "#cccccc" }} 
                  formatter={(value) => [formatarDinheiro(value), "Gasto"]}
                />
                <Bar
                  dataKey="value"
                  fill={entity.color || "#007bff"}
                  radius={[4, 4, 0, 0]}
                />
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}
      </div>
    </div>,
    document.body,
  );
}

export default AnalyticsModal;
