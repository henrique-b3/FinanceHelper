import { createPortal } from "react-dom";
import "./NewModal.css";

function AlertModal({ isOpen, title = "Aviso", message, type = "error", onClose }) {
  if (!isOpen) return null;

  const color = type === "error" ? "#ff393c" : "#09ff00";

  return createPortal(
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()} style={{ borderTop: `4px solid ${color}` }}>
        <button className="closeButton" onClick={onClose}>
          &times;
        </button>
        <h2 style={{ marginTop: 0, color: "white" }}>{title}</h2>
        <div style={{ 
            padding: "15px", 
            borderRadius: "8px",
            marginBottom: "20px",
            color: "white"
        }}>
          {message}
        </div>
      </div>
    </div>,
    document.body
  );
}

export default AlertModal;