import "../styles/CreateHousehold.css";
import { useState } from "react";

function CreateHousehold({ createHousehold }) {
  const [householdName, setHouseholdName] = useState("");

  const handleCreateHousehold = (e) => {
    e.preventDefault();
    createHousehold(householdName);
  };

  return (
    <div className="create-household-window">
      <h1>Create a household</h1>
      <form className="create-household-form" onSubmit={handleCreateHousehold}>
        <label htmlFor="household-name">Household Name</label>
        <input
          type="text"
          id="household-name"
          name="household-name"
          placeholder="Household Name"
          value={householdName}
          onChange={(e) => setHouseholdName(e.target.value)}
          required
        />
        <button type="submit">Create Household</button>
      </form>
    </div>
  );
}

export default CreateHousehold;
