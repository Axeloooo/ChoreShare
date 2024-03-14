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
  setCurrentHousehold,
  currentHousehold,
  username,
  createHousehold,
  households,
}) {
  const [overlayVisible, setOverlayVisible] = useState(false);
  const [overlayContent, setOverlayContent] = useState(null);

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
            createHousehold={createHousehold}
            closeOverlay={closeOverlay}
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
            currentHousehold={currentHousehold}
            households={households}
            setCurrentHousehold={setCurrentHousehold}
          />
        </>
      ) : null}
    </div>
  );
}

export default Layout;
