import "../styles/Sidebar.css";
import HouseholdMember from "./HouseholdMember";
import AddMember from "../components/AddMember";
import CreateHousehold from "../components/CreateHousehold";

function Sidebar({
  user,
  sidebarOpen,
  setSidebarOpen,
  showOverlay,
  haveHousehold,
}) {
  const members = [
    { name: "Smith Jhon", username: "smith2849" },
    { name: "John Doe", username: "jhonny2784" },
    { name: "Jane Doe", username: "janey2784" },
    { name: "John Smith", username: "johnny2784" },
    { name: "Jane Smith", username: "jane2784" },
    { name: "John Johnson", username: "johnson2784" },
  ];

  const handleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

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
            <p className="fullname">
              {user.firstName} {user.lastName}
            </p>
            <p className="username">{user.username}</p>
          </div>
        </div>
        <div className="user-info">
          <p className="user-email">Email: {user.email}</p>
          <p className="user-phone">Phone: {user.phone}</p>
        </div>
      </div>
      {haveHousehold ? (
        <div className="household-container">
          <div className="household-container-header">
            <h3>My Household</h3>
            <p onClick={handleShowAddMember}>+Add Members</p>
          </div>
          <div className="members-list">
            {members.map((member, index) => {
              return (
                <HouseholdMember
                  key={index}
                  index={index}
                  name={member.name}
                  username={member.username}
                />
              );
            })}
          </div>
        </div>
      ) : (
        <div className="no-household-container">
          <h3>No Household Found</h3>
          <h3>Could not get household info</h3>
        </div>
      )}
    </div>
  );
}

export default Sidebar;
