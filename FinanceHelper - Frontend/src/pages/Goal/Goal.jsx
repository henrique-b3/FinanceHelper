import { useEffect, useState } from "react";
import api from "../../services/api";
import * as components from "../../components";
import { images } from "../../svg";
import "./Goal.css";

function Goal() {
  const [goalsList, setGoalsList] = useState([]);
  const [categoriesList, setCategoriesList] = useState([]);
  const [companiesList, setCompaniesList] = useState([]);
  const [statusList, setStatusList] = useState([]);

  // Estados dos filtros
  const [searchGoal, setSearchGoal] = useState("");
  const [categoryID, setCategoryID] = useState("");
  const [companyID, setCompanyID] = useState("");
  const [status, setStatus] = useState("");
  const [sortOption, setSortOption] = useState("");

  // Modais e Alertas
  const [selectedGoal, setSelectedGoal] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [confirmConfig, setConfirmConfig] = useState({
    isOpen: false,
    idToDelete: null,
  });
  const [alertConfig, setAlertConfig] = useState({
    isOpen: false,
    message: "",
    type: "error",
  });

  const ORDERBY_OPTIONS = [
    { value: "endDate-asc", label: "Data Fim (mais próxima)" },
    { value: "endDate-desc", label: "Data Fim (mais distante)" },
    { value: "limitAmount-desc", label: "Valor (mais alto)" },
    { value: "limitAmount-asc", label: "Valor (mais baixo)" },
  ];

  const statusLabels = {
    Upcoming: "Próximo",
    Active: "Ativo",
    Warning: "Atenção",
    Exceeded: "Excedido",
    Completed: "Concluído",
    Finished: "Finalizado",
  };

  useEffect(() => {
    api
      .get("/category/all")
      .then((answer) => setCategoriesList(answer.data))
      .catch(handleError);

    api
      .get("/company/all")
      .then((answer) => setCompaniesList(answer.data))
      .catch(handleError);

    api
      .get("/goal/status")
      .then((answer) => setStatusList(answer.data))
      .catch(handleError);

    fetchFilteredGoals();
  }, []);

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

  const handleError = (error) => {
    let errorMessage = "Ocorreu um erro inesperado.";
    if (error.response?.data?.message)
      errorMessage = error.response.data.message;
    setAlertConfig({ isOpen: true, message: errorMessage, type: "error" });
  };

  const handleSuccess = (successMessage) => {
    setAlertConfig({ isOpen: true, message: successMessage, type: "success" });
  };

  const fetchFilteredGoals = () => {
    const params = {};
    if (searchGoal.trim() !== "") params.name = searchGoal;
    if (categoryID !== "") params.categoryID = categoryID;
    if (companyID !== "") params.companyID = companyID;
    if (status !== "") params.status = status;
    if (sortOption !== "") {
      const [orderBy, direction] = sortOption.split("-");
      params.orderBy = orderBy;
      params.direction = direction;
    }

    api
      .get("/goal/filter", { params })
      .then((answer) => {
        const data = answer.data.content || answer.data;
        setGoalsList(data);
      })
      .catch(handleError);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchFilteredGoals();
  };

  const clearFilters = () => {
    setSearchGoal("");
    setCategoryID("");
    setCompanyID("");
    setStatus("");
    setSortOption("");
    setTimeout(() => fetchFilteredGoals(), 0);
  };

  const confirmDelete = (id) =>
    setConfirmConfig({ isOpen: true, idToDelete: id });

  const executeDelete = async () => {
    try {
      await api.delete("/goal/delete", {
        params: { goalID: confirmConfig.idToDelete },
      });
      setConfirmConfig({ isOpen: false, idToDelete: null });
      fetchFilteredGoals();
      handleSuccess("Objetivo apagado com sucesso");
    } catch (error) {
      setConfirmConfig({ isOpen: false, idToDelete: null });
      handleError(error);
    }
  };

  const handleOpenModal = (goal) => {
    setSelectedGoal(goal);
    setIsModalOpen(true);
  };

  return (
    <div className="categoryContainer">
      <div className="category-header">
        <h2 className="section-title">Os Meus Objetivos</h2>
        <button
          onClick={() => handleOpenModal(null)}
          className="new-category-btn"
        >
          + Novo Objetivo
        </button>
      </div>

      <nav className="search-nav">
        <form
          onSubmit={handleSearchSubmit}
          style={{
            display: "flex",
            gap: "15px",
            flexWrap: "wrap",
            alignItems: "flex-end",
          }}
        >
          <div style={{ flex: 1, minWidth: "200px" }}>
            <input
              type="text"
              value={searchGoal}
              className="search-input"
              style={{ width: "100%", height: "48px", boxSizing: "border-box" }}
              placeholder="Pesquisar objetivo..."
              onChange={(e) => setSearchGoal(e.target.value)}
            />
          </div>

          <label className="textLabel">
            Categoria:
            <select
              className="form-input"
              value={categoryID}
              onChange={(e) => setCategoryID(e.target.value)}
            >
              <option value="">Todas</option>
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
              <option value="">Todas</option>
              {companiesList.map((company) => (
                <option key={company.id} value={company.id}>
                  {company.name}
                </option>
              ))}
            </select>
          </label>

          <label className="textLabel">
            Estado:
            <select
              className="form-input"
              value={status}
              onChange={(e) => setStatus(e.target.value)}
            >
              <option value="">Todos</option>
              {statusList.map((status) => (
                <option key={status} value={status}>
                  {statusLabels[status] || status}
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

          <button type="submit" className="search-btn">
            Filtrar
          </button>

          <button
            type="button"
            onClick={clearFilters}
            style={{
              background: "rgba(255, 255, 255, 0.1)",
              color: "white",
              border: "1px solid rgba(255, 255, 255, 0.2)",
              borderRadius: "12px",
              padding: "0 24px",
              height: "48px",
              fontWeight: "500",
              cursor: "pointer",
            }}
          >
            Limpar
          </button>
        </form>
      </nav>

      {goalsList.length === 0 ? (
        <div className="empty-state">
          <p>Nenhum objetivo encontrado com estes filtros.</p>
        </div>
      ) : (
        <div className="goals-grid">
          {goalsList.map((goal) => {
            const percentagem = calcularPercentagem(
              goal.spendAmount,
              goal.limitAmount,
            );
            const estourouOrcamento = goal.spendAmount > goal.limitAmount;
            const corBarra = estourouOrcamento
              ? "#ff4d4f"
              : goal.color || "#007bff";

            return (
              <div key={goal.id} className="goal-page-item">
                <div className="goal-header">
                  <div className="goal-title-group">
                    <div className="goal-info">
                      <h4 className="goal-name">{goal.name}</h4>
                      {goal.endDate && (
                        <span className="goal-date">
                          Até{" "}
                          {new Date(goal.endDate).toLocaleDateString("pt-PT")}
                        </span>
                      )}
                    </div>
                  </div>
                  <div className="category-actions">
                    <button
                      onClick={() => handleOpenModal(goal)}
                      className="action-btn edit-btn"
                      title="Editar"
                    >
                      <img src={images.edit} alt="Editar" />
                    </button>
                    <button
                      onClick={() => confirmDelete(goal.id)}
                      className="action-btn delete-btn"
                      title="Apagar"
                    >
                      <img src={images.deleteItem} alt="Apagar" />
                    </button>
                  </div>
                </div>

                <div className="goal-progress-wrapper">
                  <div className="goal-progress-bar-bg">
                    <div
                      className="goal-progress-bar-fill"
                      style={{
                        width: `${percentagem}%`,
                        backgroundColor: corBarra,
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
                    Excedeu em{" "}
                    {formatarDinheiro(Math.abs(goal.remainingAmount))}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}

      <components.ConfirmModel
        isOpen={confirmConfig.isOpen}
        title="Apagar Objetivo"
        message="Tem a certeza que deseja apagar este objetivo? Esta ação pode ser desfeita."
        onConfirm={executeDelete}
        onClose={() => setConfirmConfig({ isOpen: false, idToDelete: null })}
      />

      <components.AlertModel
        isOpen={alertConfig.isOpen}
        title={alertConfig.type === "error" ? "Erro" : "Sucesso"}
        message={alertConfig.message}
        type={alertConfig.type}
        onClose={() => setAlertConfig({ isOpen: false, message: "" })}
      />

      <components.NewGoal
        isOpen={isModalOpen}
        goal={selectedGoal}
        onClose={() => setIsModalOpen(false)}
        onSuccess={() => {
          fetchFilteredGoals();
          handleSuccess("Operação realizada com sucesso!");
        }}
      />
    </div>
  );
}

export default Goal;
