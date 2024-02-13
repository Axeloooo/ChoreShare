import Header from './Header';
import Sidebar from './Sidebar';
import { Outlet } from "react-router-dom";

function Layout({ sidebarOpen, setSidebarOpen }) {

  return (
    <div className="app-layout">
      <Header />
      <Outlet />
      <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}/>
    </div>
  );
}

export default Layout;