import "../styles/CreateHousehold.css";
import { useState } from "react";

function JoinHousehold({ joinHousehold, closeOverlay }) {
  const [householdId, setHouseholdId] = useState("");

  const handleJoinHousehold = (e) => {
    e.preventDefault();
    joinHousehold(householdId, closeOverlay);
  };

  return (
    <div className="create-household-window">
      <h1>Join a household</h1>
      <form
        className="create-household-form"
        action=""
        onSubmit={handleJoinHousehold}
      >
        <label htmlFor="household-name">Invitation Code</label>
        <input
          type="text"
          id="household-name"
          name="household-name"
          value={householdId}
          onChange={(e) => setHouseholdId(e.target.value)}
          placeholder="Code received via email"
          required
        />
        <button type="submit">Join Household</button>
      </form>
    </div>
  );
}

export default JoinHousehold;
