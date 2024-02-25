import '../styles/Sidebar.css';
import HouseholdMember from './HouseholdMember';
import AddMember from '../components/AddMember';

function Sidebar({sidebarOpen, setSidebarOpen, showOverlay}) {

    const handleSidebar = () => {
        setSidebarOpen(!sidebarOpen);
    }

    const email = "podgaietska9038@gmail.com"
    const phone = "123-456-7890"

    const handleShowAddMember = () => {
        showOverlay(<AddMember />); 
    };

  return (
    <div className={sidebarOpen ? "sidebar" : "sidebar closed"}>
        <div className="hamburger-menu" onClick={handleSidebar}>
            &#9776;
        </div>
        <div className="user-container">
            <div className="user-identity">
                <div className="user-identifier"></div>
                <div className="user-name">
                    <p className="fullname">Mariia Podgaietska</p>
                    <p className="username">podgaietska29384</p>
                </div>
            </div>
            <div className="user-info">
                <p className="user-email">Email: {email}</p>
                <p className="user-phone">Phone: {phone}</p>
            </div>
        </div>
        <div className="household-container">
            <div className="household-container-header">
                <h3>My Household</h3>
                <p onClick={handleShowAddMember}>+Add Members</p>
            </div>
            <div className="members-list">
                <HouseholdMember />
                <HouseholdMember />
                <HouseholdMember />
                <HouseholdMember />
                <HouseholdMember />
                <HouseholdMember />
            </div>
        </div>
    </div>
  );
}

export default Sidebar;