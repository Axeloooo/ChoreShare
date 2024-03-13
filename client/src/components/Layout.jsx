import Header from "./Header";
import Sidebar from "./Sidebar";
import Overlay from "./Overlay";
import { Outlet } from "react-router-dom";
import { useState } from "react";
import CreateHousehold from "./CreateHousehold";

function Layout({
  sidebarOpen,
  setSidebarOpen,
  user,
  logout,
  haveHousehold,
  username,
}) {
  const [overlayVisible, setOverlayVisible] = useState(false);
  const [overlayContent, setOverlayContent] = useState(<CreateHousehold />);

  const showOverlay = (content) => {
    setOverlayContent(content);
    setOverlayVisible(true);
  };

  const closeOverlay = () => setOverlayVisible(false);

  return (
    <div className="app-layout">
      {user !== null ? (
        <>
          <Header
            logout={logout}
            showOverlay={showOverlay}
            username={username}
          />
          <Outlet context={{ showOverlay }} />
          {overlayVisible && (
            <Overlay content={overlayContent} closeOverlay={closeOverlay} />
          )}
          <Sidebar
            user={user}
            sidebarOpen={sidebarOpen}
            setSidebarOpen={setSidebarOpen}
            showOverlay={showOverlay}
            haveHousehold={haveHousehold}
          />
        </>
      ) : null}
    </div>
  );
}

export default Layout;
