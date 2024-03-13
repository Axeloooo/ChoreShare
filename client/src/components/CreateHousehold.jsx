import "../styles/CreateHousehold.css";

function CreateHousehold() {
  return (
    <div className="create-household-window">
      <h1>Create a household</h1>
      <form className="create-household-form">
        <label htmlFor="household-name">Household Name</label>
        <input
          type="text"
          id="household-name"
          name="household-name"
          placeholder="Household Name"
          required
        />
        <button type="submit">Create Household</button>
      </form>
    </div>
  );
}

export default CreateHousehold;
