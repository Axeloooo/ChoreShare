import '../styles/Header.css';
import {Link, useLocation} from "react-router-dom";

function Header() {
  const location = useLocation();
  console.log(location.pathname);

  return (
    <div className="header">
      <ul className="nav-list">
        <li className={location.pathname === "/" ? "nav-item active" : "nav-item"}>
          <Link to="/">Dashboard</Link>
        </li>
        <li className={location.pathname === "/chore-list" ? "nav-item active" : "nav-item"}>
          <Link to="/chore-list">Chore List</Link>
        </li>
        <li className={location.pathname === "/calendar" ? "nav-item active" : "nav-item"}>
          <Link to="/calendar">Calendar</Link>
        </li>
      </ul>
    </div>
  );
}

export default Header;