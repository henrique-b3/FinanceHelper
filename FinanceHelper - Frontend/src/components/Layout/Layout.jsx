import { Outlet } from "react-router-dom";
import NavMenu from "../NavMenu/NavMenu.component";
import "./Layout.css";

function Layout() {
  return (
    <div className="app-layout">
      <div className="layout-wrapper">
        
        <NavMenu />
        
        <main className="main-content">
          <Outlet /> 
        </main>

      </div>
    </div>
  );
}

export default Layout;