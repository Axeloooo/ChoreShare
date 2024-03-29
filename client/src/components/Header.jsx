import "../styles/Header.css";
import { Link, useLocation } from "react-router-dom";
import { FaArrowRightToBracket } from "react-icons/fa6";
import CreateHousehold from "./CreateHousehold";
import JoinHousehold from "./JoinHousehold";

function Header({
  logout,
  showOverlay,
  username,
  createHousehold,
  closeOverlay,
  joinHousehold,
}) {
  const location = useLocation();

  const handleLogout = () => {
    const answer = window.confirm("Are you sure you want to log out?");
    if (answer) {
      logout();
    }
  };

  const handleShowCreateHousehold = () => {
    showOverlay(
      <CreateHousehold
        createHousehold={createHousehold}
        closeOverlay={closeOverlay}
      />
    );
  };

  const handleShowJoinHousehold = () => {
    showOverlay(
      <JoinHousehold
        joinHousehold={joinHousehold}
        closeOverlay={closeOverlay}
      />
    );
  };

  return (
    <div className="header">
      <ul className="nav-list">
        <li
          className={location.pathname === "/" ? "nav-item active" : "nav-item"}
        >
          <Link to="/">Dashboard</Link>
        </li>
        <li
          className={
            location.pathname === "/chore-list" ? "nav-item active" : "nav-item"
          }
        >
          <Link to="/chore-list">Chore List</Link>
        </li>
        <li
          className={
            location.pathname === "/calendar" ? "nav-item active" : "nav-item"
          }
        >
          <Link to="/calendar">Household Calendar</Link>
        </li>
      </ul>
      <div className="functionality-container">
        <div className="household-buttons">
          <button
            className="household-button"
            onClick={handleShowCreateHousehold}
          >
            Create Household
          </button>
          <button
            className="household-button"
            onClick={handleShowJoinHousehold}
          >
            Join Household
          </button>
        </div>
        <div className="user-auth">
          <p className="auth-username">{username}</p>
          <FaArrowRightToBracket className="exit-icon" onClick={handleLogout} />
        </div>
      </div>
    </div>
  );
}

export default Header;
