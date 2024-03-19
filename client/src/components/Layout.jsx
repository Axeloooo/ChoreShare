import Header from "./Header";
import Sidebar from "./Sidebar";
import Overlay from "./Overlay";
import { Outlet } from "react-router-dom";
import { useState } from "react";

function Layout({
  sidebarOpen,
  setSidebarOpen,
  user,
  logout,
  username,
  createHousehold,
  data,
  inviteMember,
  joinHousehold,
  changeHousehold,
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
            joinHousehold={joinHousehold}
          />
          <Outlet context={{ showOverlay, closeOverlay }} />
          {overlayVisible && (
            <Overlay content={overlayContent} closeOverlay={closeOverlay} />
          )}
          <Sidebar
            user={user}
            sidebarOpen={sidebarOpen}
            setSidebarOpen={setSidebarOpen}
            showOverlay={showOverlay}
            closeOverlay={closeOverlay}
            data={data}
            inviteMember={inviteMember}
            changeHousehold={changeHousehold}
          />
        </>
      ) : null}
    </div>
  );
}

export default Layout;
