import { useLocation, useNavigate } from "react-router-dom";
import api from "../../services/api";
import "./NavMenu.css";
import * as pages from "../../pages/";

function NavMenu(){

    const navigate = useNavigate();
    const location = useLocation();

    const isActive = (path) => location.pathname.includes(path);

    return(
        <nav className="navMenu">
            <button 
            className={`optionButton ${isActive("/dashboard") ? "active" : ""}`}
            onClick={() => navigate("/dashboard")}>
                <p>Dashboard</p>
            </button>
            <button 
            className={`optionButton ${isActive("/transaction") ? "active" : ""}`}
            onClick={() => navigate("/transaction")}>
                <p>Transações</p>
            </button>
            <button 
            className={`optionButton ${isActive("/category") ? "active" : ""}`}
            onClick={() => navigate("/category")}>
                <p>Categorias</p>
            </button>
            <button 
            className={`optionButton ${isActive("/company") ? "active" : ""}`}
            onClick={() => navigate("/company")}>
                <p>Empresas</p>
            </button>
            <button 
            className={`optionButton ${isActive("/goal") ? "active" : ""}`}
            onClick={() => navigate("/goal")}>
                <p>Objetivos</p>
            </button>
        </nav>
    )
}

export default NavMenu;
