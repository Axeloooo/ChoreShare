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
  closeOverlay,
  data,
  inviteMember,
  changeHousehold,
}) {
  const [selectedOption, setSelectedOption] = useState(() => {
    return data.currentHousehold
      ? { value: data.currentHousehold.id, label: data.currentHousehold.name }
      : null;
  });

  useEffect(() => {
    if (data.currentHousehold) {
      setSelectedOption({
        value: data.currentHousehold.id,
        label: data.currentHousehold.name,
      });
    }
  }, [data.currentHousehold]);

  const members = [
    { name: "Smith Jhon", username: "smith2849" },
    { name: "John Doe", username: "jhonny2784" },
    { name: "Jane Doe", username: "janey2784" },
    { name: "John Smith", username: "johnny2784" },
    { name: "Jane Smith", username: "jane2784" },
    { name: "John Johnson", username: "johnson2784" },
  ];

  const householdOptions = data.households.map((household) => ({
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
    if (selectedOption.value === data.currentHousehold?.id) return;

    const isConfirmed = window.confirm(
      "Are you sure you want to change the household?"
    );

    if (isConfirmed) {
      setSelectedOption(selectedOption);
      const newCurrentHousehold = {
        name: selectedOption.label,
        id: selectedOption.value,
      };
      changeHousehold(newCurrentHousehold);
    }
  };

  const handleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  const handleShowAddMember = () => {
    showOverlay(
      <AddMember inviteMember={inviteMember} closeOverlay={closeOverlay} />
    );
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
      {data.currentHousehold != null ? (
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
            {data.members.filter((member) => member.user.id !== user.id)
              .length > 0 ? (
              data.members
                .filter((member) => member.user.id !== user.id)
                .map((member, index) => (
                  <HouseholdMember key={index} index={index} member={member} />
                ))
            ) : (
              <div className="household-empty">
                <p>No other members in the household</p>
              </div>
            )}
          </div>
          <button className="add-member-btn" onClick={handleShowAddMember}>
            +
          </button>
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
