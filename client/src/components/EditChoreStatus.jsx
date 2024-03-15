import "../styles/EditChoreStatus.css";
import Select from "react-select";
import { useState } from "react";

const options = [
  { value: "PENDING", label: "Pending" },
  { value: "IN_PROGRESS", label: "In Progress" },
  { value: "COMPLETED", label: "Completed" },
];

const customStyles = {
  control: (provided) => ({
    ...provided,
    width: "25rem",
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

function EditChoreStatus({ chore, editChoreStatus, closeOverlay }) {
  const [selectedOption, setSelectedOption] = useState(() =>
    options.find((option) => option.value === chore.status)
  );

  const handleChange = (selectedOption) => {
    setSelectedOption(selectedOption);
  };

  console.log(chore);

  const handleEditStatus = (e) => {
    e.preventDefault();
    editChoreStatus(chore.id, selectedOption.value, closeOverlay);
  };

  return (
    <div className="edit-status-window">
      <h1>Update Status</h1>
      <form className="edit-status-form">
        <label htmlFor="announce-options" className="label">
          Title
        </label>
        <input type="text" id="chore-title" value={chore.title} readOnly />
        <label htmlFor="announce-options" className="label">
          Status
        </label>
        <Select
          options={options}
          value={selectedOption}
          onChange={handleChange}
          styles={customStyles}
        />
        <button type="submit" onClick={handleEditStatus}>
          Update Status
        </button>
      </form>
    </div>
  );
}

export default EditChoreStatus;
