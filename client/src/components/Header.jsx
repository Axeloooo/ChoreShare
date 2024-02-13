import '../styles/Header.css';

function Header() {
  return (
    <div className="header">
      <ul className="nav-list">
        <li className="nav-item active">Dashboard</li>
        <li className="nav-item">ChoreList</li>
        <li className="nav-item">Calendar</li>
      </ul>
    </div>
  );
}

export default Header;