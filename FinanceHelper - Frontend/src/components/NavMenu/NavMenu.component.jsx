import { useEffect, useState } from "react";
import api from "../../services/api";
import "./NavMenu.css";
import * as pages from "../../pages/";
import { useNavigate } from "react-router-dom";

function NavMenu(){

    const navigate = useNavigate();

    return(
        <nav className="navMenu">
            <button className="optionButton" onClick={() => navigate("/dashboard")}>
                <p>Dashboard</p>
            </button>
            <button className="optionButton" onClick={handler}>
                <p>Transações</p>
            </button>
            <button className="optionButton" onClick={() => navigate("/category")}>
                <p>Categorias</p>
            </button>
            <button className="optionButton" onClick={handler}>
                <p>Empresas</p>
            </button>
            <button className="optionButton" onClick={handler}>
                <p>Objetivos</p>
            </button>
        </nav>
    )
}

function handler(){

}

export default NavMenu;
