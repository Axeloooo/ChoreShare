import "../styles/CreateHousehold.css";

function JoinHousehold() {
  return (
    <div className="create-household-window">
      <h1>Join a household</h1>
      <form className="create-household-form">
        <label htmlFor="household-name">Invitation Code</label>
        <input
          type="text"
          id="household-name"
          name="household-name"
          placeholder="Code received via email"
          required
        />
        <button type="submit">Join Household</button>
      </form>
    </div>
  );
}

export default JoinHousehold;
