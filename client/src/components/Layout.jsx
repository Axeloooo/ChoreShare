import Header from './Header';
import Sidebar from './Sidebar';
import Overlay from './Overlay';
import { Outlet } from "react-router-dom";
import { useState } from "react";

function Layout({ sidebarOpen, setSidebarOpen, user, setUser }) {
  const [overlayVisible, setOverlayVisible] = useState(false);
  const [overlayContent, setOverlayContent] = useState(null);

  const showOverlay = (content) => {
    setOverlayContent(content);
    setOverlayVisible(true);
  };

  const closeOverlay = () => setOverlayVisible(false);

  return (
    <div className="app-layout">
      {user !== null ? 
      <>
        <Header setUser={setUser}/> 
        <Outlet context={{ showOverlay }}/>
        {overlayVisible && <Overlay content={overlayContent} closeOverlay={closeOverlay}/>}
        <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} showOverlay={showOverlay}/>
      </>
      : null}
    </div>
  );
}

export default Layout;