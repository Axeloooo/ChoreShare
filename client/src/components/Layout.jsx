import Header from './Header';
import Sidebar from './Sidebar';
import { Outlet } from "react-router-dom";

function Layout({ sidebarOpen, setSidebarOpen, user, setUser }) {

  return (
    <div className="app-layout">
      {user !== null ? <Header setUser={setUser}/> : null}
      <Outlet />
      {user !== null ? <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}/> : null}
    </div>
  );
}

export default Layout;