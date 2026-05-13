import { createPortal } from "react-dom";
import "./NewModal.css"; 

function ConfirmModal({ isOpen, title, message, onConfirm, onClose, confirmText = "Confirmar", cancelText = "Cancelar" }) {
  if (!isOpen) return null;

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <button className="closeButton" onClick={onClose}>
          &times;
        </button>
        <h2 style={{ marginTop: 0, color: "white" }}>{title}</h2>
        <p style={{ color: "rgba(255, 255, 255, 0.8)", marginBottom: "20px" }}>{message}</p>
        
        <div style={{ display: "flex", gap: "10px" }}>
          <button 
            className="btn-primary" 
            onClick={onConfirm} 
            style={{ backgroundColor: "#ff4d4f", flex: 1 }}
          >
            {confirmText}
          </button>
          <button 
            className="btn-primary" 
            onClick={onClose} 
            style={{ backgroundColor: "rgba(255, 255, 255, 0.1)", flex: 1 }}
          >
            {cancelText}
          </button>
        </div>
      </div>
    </div>,
    document.body
  );
}

export default ConfirmModal;