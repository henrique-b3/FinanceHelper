import { useEffect, useState } from "react";
import api from "../../../services/api";
import "./NavMenu.css";

function NavMenu(){
    return(
        <nav className="navMenu">
            <button className="optionButton" onClick={handler}>
                <p>Perfil</p>
            </button>
            <button className="optionButton" onClick={handler}>
                <p>Transações</p>
            </button>
            <button className="optionButton" onClick={handler}>
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
