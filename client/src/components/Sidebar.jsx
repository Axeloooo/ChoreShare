import "../styles/Sidebar.css";
import HouseholdMember from "./HouseholdMember";
import AddMember from "../components/AddMember";
import Select from "react-select";
import { useState, useEffect } from "react";

function Sidebar({
  user,
  sidebarOpen,
  setSidebarOpen,
  showOverlay,
  currentHousehold,
  households,
  setCurrentHousehold,
}) {
  const [selectedOption, setSelectedOption] = useState(() => {
    return currentHousehold
      ? { value: currentHousehold.id, label: currentHousehold.name }
      : null;
  });

  useEffect(() => {
    if (currentHousehold) {
      setSelectedOption({
        value: currentHousehold.id,
        label: currentHousehold.name,
      });
    }
  }, [currentHousehold]);

  const members = [
    { name: "Smith Jhon", username: "smith2849" },
    { name: "John Doe", username: "jhonny2784" },
    { name: "Jane Doe", username: "janey2784" },
    { name: "John Smith", username: "johnny2784" },
    { name: "Jane Smith", username: "jane2784" },
    { name: "John Johnson", username: "johnson2784" },
  ];

  const householdOptions = households.map((household) => ({
    value: household.id,
    label: household.name,
  }));

  const customStyles = {
    control: (provided) => ({
      ...provided,
      width: "16rem",
      fontSize: "13px",
    }),
    option: (provided) => ({
      ...provided,
      fontSize: "13px",
    }),
    menu: (provided) => ({
      ...provided,
      width: "100%",
    }),
  };

  const toggleHousehold = (selectedOption) => {
    console.log("selectedOption", selectedOption);
    console.log("currentHousehold", currentHousehold);
    console.log(selectedOption.value === currentHousehold.id);
    if (selectedOption.value === currentHousehold?.id) return;

    const isConfirmed = window.confirm(
      "Are you sure you want to change the household?"
    );
    console.log("isConfirmed", isConfirmed);
    if (isConfirmed) {
      setSelectedOption(selectedOption);
      const newCurrentHousehold = {
        name: selectedOption.label,
        id: selectedOption.value,
      };
      setCurrentHousehold(newCurrentHousehold);
    }
  };

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
      {currentHousehold != null ? (
        <div className="household-container">
          <div className="household-container-header">
            <Select
              options={householdOptions}
              styles={customStyles}
              value={selectedOption}
              onChange={toggleHousehold}
            />
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
