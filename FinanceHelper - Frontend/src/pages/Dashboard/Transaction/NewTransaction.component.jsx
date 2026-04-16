import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import api from "../../../services/api";
import "../NewModal.css";

function NewTransaction({ isOpen, onClose, onSuccess, transaction = null }) {
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [transactionDate, setTransactionDate] = useState("");
  const [categoryID, setCategoryID] = useState("");
  const [companyID, setCompanyID] = useState("");
  const [categoriesList, setcategoriesList] = useState([]);
  const [companiesList, setcompaniesList] = useState([]);
  const [erro, setErro] = useState("");

  useEffect(() => {
    if (isOpen) {
      api
        .get("/category/all")
        .then((resposta) => {
          setcategoriesList(resposta.data);
        })
        .catch((erro) => {
          console.error("Erro ao buscar categorias", erro);
          setErro("Não foi possível carregar as suas categorias.");
        });

      api
        .get("/company/all")
        .then((resposta) => {
          setcompaniesList(resposta.data);
        })
        .catch((erro) => {
          console.error("Erro ao buscar categorias", erro);
          setErro("Não foi possível carregar as suas categorias.");
        });
    }
  }, [isOpen]);

  useEffect(() => {
    if (isOpen && transaction) {
      setDescription(transaction.description || "");
      setAmount(transaction.amount || "");
      setCategoryID(transaction.categoryID || "");
      setCompanyID(transaction.companyID || "");

      if (transaction.transactionDate) {
        setTransactionDate(
          new Date(transaction.transactionDate).toISOString().split("T")[0],
        );
      }
    } else if (isOpen && !transaction) {
      setDescription("");
      setAmount("");
      setTransactionDate("");
      setCompanyID("");
      setCategoryID("");
    }
  }, [isOpen, transaction]);

  if (!isOpen) return null;

  const handleCreateTransaction = async (e) => {
    e.preventDefault();
    setErro("");

    if (!categoryID) {
      setErro("Por favor, selecione uma Categoria para esta Transação.");
      return;
    }

    if (!companyID) {
      setErro("Por favor, selecione uma empresa para esta Transação.");
      return;
    }

    try {
      if (!transaction) {
        await api.post("/transaction", {
          description: description,
          amount: amount,
          transactionDate: transactionDate,
          companyID: companyID,
          categoryID: categoryID,
        });
      } else {
        await api.put(
          "/transaction/update",
          {
            description: description,
            amount: amount,
            transactionDate: transactionDate,
            companyID: companyID,
            categoryID: categoryID,
          },
          {
            params: { transactionID: transaction.id },
          },
        );
      }

      alert("Transação criada com sucesso!");
      if (onSuccess) onSuccess();

      setDescription("");
      setAmount("");
      setTransactionDate("");
      setCompanyID("");
      setCategoryID("");
      onClose();
    } catch (error) {
      console.error(error);
      setErro("Erro ao criar transação. Verifique se o nome já existe.");
    }
  };

  const handleDelete = async () => {
    if (window.confirm("Deseja apagar esta transação?")) {
      try {
        await api.delete(
          "/transaction/delete",
          {
            params: { transactionID: transaction.id },
          },
        );

        if (onSuccess) onSuccess();
        onClose();
      } catch (err) { setErro("Erro ao apagar."); }
    }
  };

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button className="closeButton" onClick={onClose}>
          ✕
        </button>

        <h2 style={{ marginTop: 0, color: "#333" }}>
          {transaction ? "Editar transação" : "Criar nova transação"}
        </h2>
        {erro && <p style={{ color: "red" }}>{erro}</p>}

        <form className="formModel" onSubmit={handleCreateTransaction}>
          <label className="textLabel">
            Descrição:
            <input
              className="textInput"
              type="text"
              placeholder="Ex: Compra de suplementos"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Valor:
            <input
              className="textInput"
              type="number"
              placeholder="20€"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
            />
          </label>

          <label className="textLabel">
            Data:
            <input
              className="textInput"
              type="date"
              value={transactionDate}
              onChange={(e) => setTransactionDate(e.target.value)}
            />
          </label>

          <label className="textLabel">
            Categoria Associada:
            <select
              className="textInput"
              value={categoryID}
              onChange={(e) => setCategoryID(e.target.value)}
              required
            >
              <option value="" disabled>
                Selecione uma categoria...
              </option>
              {categoriesList.map((categoria) => (
                <option key={categoria.id} value={categoria.id}>
                  {categoria.name}
                </option>
              ))}
            </select>
          </label>

          <label className="textLabel">
            Empresa Associada:
            <select
              className="textInput"
              value={companyID}
              onChange={(e) => setCompanyID(e.target.value)}
            >
              <option value="" disabled>
                Selecione uma empresa...
              </option>
              {companiesList.map((company) => (
                <option key={company.id} value={company.id}>
                  {company.name}
                </option>
              ))}
            </select>
          </label>

          <div style={{ display: "flex", gap: "10px" }}>
            <button className="submitButton" type="submit" style={{ flex: 1 }}>
              {transaction ? "Guardar Alterações" : "Guardar Transação"}
            </button>
            
            {transaction && (
              <button type="button" onClick={handleDelete} className="submitButton" style={{ backgroundColor: "#ff4d4f", flex: 1 }}>
                Apagar
              </button>
            )}
          </div>
        </form>
      </div>
    </div>,
    document.body,
  );
}

export default NewTransaction;
