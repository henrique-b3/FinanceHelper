import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import api from "../../services/api";
import "../Modal/NewModal.css";
import * as components from "../../components";
import { useAlert } from "../../contexts/AlertContext";

function NewTransaction({ isOpen, onClose, onSuccess, transaction = null }) {
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [transactionDate, setTransactionDate] = useState("");
  const [categoryID, setCategoryID] = useState("");
  const [companyID, setCompanyID] = useState("");
  const [categoriesList, setcategoriesList] = useState([]);
  const [companiesList, setcompaniesList] = useState([]);

  const [confirmConfig, setConfirmConfig] = useState({ isOpen: false });

  const { showAlert } = useAlert();

  useEffect(() => {
    if (isOpen) {
      api
        .get("/category/all")
        .then((answer) => {
          setcategoriesList(answer.data);
        })
        .catch((error) => showAlert(error, "error"));

      api
        .get("/company/all")
        .then((answer) => {
          setcompaniesList(answer.data);
        })
        .catch((error) => showAlert(error, "error"));
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

    if (!categoryID) {
      showAlert("Por favor, selecione uma Categoria para esta Transação.", "error");
      return;
    }

    try {
      if (!transaction) {
        await api.post("/transaction", {
          description: description,
          amount: amount,
          transactionDate: transactionDate,
          companyID: companyID !== "" ? companyID : null,
          categoryID: categoryID,
        });
      } else {
        await api.put(
          "/transaction/update",
          {
            description: description,
            amount: amount,
            transactionDate: transactionDate,
            companyID: companyID !== "" ? companyID : null,
            categoryID: categoryID,
          },
          {
            params: { transactionID: transaction.id },
          },
        );
      }

      if (onSuccess) onSuccess();

      setDescription("");
      setAmount("");
      setTransactionDate("");
      setCompanyID("");
      setCategoryID("");
      onClose();
    } catch (error) {
      showAlert(error, "error");
    }
  };

  const confirmDelete = () => setConfirmConfig({ isOpen: true });

  const executeDelete = async () => {
    try {
      await api.delete("/transaction/delete", {
        params: { transactionID: transaction.id },
      });
      setConfirmConfig({ isOpen: false });
      if (onSuccess) onSuccess();
      onClose();
    } catch (error) {
      setConfirmConfig({ isOpen: false });
      showAlert(error, "error");
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

        <form className="formModel" onSubmit={handleCreateTransaction}>
          <label className="textLabel">
            Descrição:
            <input
              className="form-input"
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
              className="form-input"
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
              className="form-input"
              type="date"
              value={transactionDate}
              onChange={(e) => setTransactionDate(e.target.value)}
            />
          </label>

          <label className="textLabel">
            Categoria Associada:
            <select
              className="form-input"
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
              className="form-input"
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
            <button className="btn-primary" type="submit" style={{ flex: 1 }}>
              {transaction ? "Guardar Alterações" : "Criar Transação"}
            </button>

            {transaction && (
              <button
                type="button"
                onClick={confirmDelete}
                className="btn-primary"
                style={{ backgroundColor: "#ff4d4f", flex: 1 }}
              >
                Apagar
              </button>
            )}
          </div>
        </form>
      </div>
      <components.ConfirmModel
        isOpen={confirmConfig.isOpen}
        title="Apagar Transação"
        message="Tem a certeza que deseja apagar esta transação? Esta ação não pode ser desfeita."
        onConfirm={executeDelete}
        onClose={() => setConfirmConfig({ isOpen: false })}
      />
    </div>,
    document.body,
  );
}

export default NewTransaction;
