import "../styles/Header.css";
import { Link, useLocation } from "react-router-dom";
import { FaArrowRightToBracket } from "react-icons/fa6";

function Header({ logout }) {
  const location = useLocation();

  const handleLogout = () => {
    logout();
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
      <div className="user-auth">
        <p className="auth-username">podgaietska9038</p>
        <FaArrowRightToBracket className="exit-icon" onClick={handleLogout} />
      </div>
    </div>
  );
}

export default Header;
