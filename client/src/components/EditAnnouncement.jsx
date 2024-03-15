import "../styles/CreateAnnouncement.css";
import Select from "react-select";

const options = [
  { value: null, label: "Submit Anonymously" },
  { value: "id", label: "Show Author" },
];

function EditAnnouncement({ closeOverlay }) {
  return (
    <div className="create-announce-window">
      <h1>Edit announcement</h1>
      <label htmlFor="announce-options" className="label">
        Options
      </label>
      <Select
        options={options}
        defaultValue={options[0]}
        styles={{
          control: (base) => ({
            ...base,
            fontSize: "0.8rem",
          }),
          menu: (base) => ({
            ...base,
            fontSize: "0.8rem",
          }),
          option: (base) => ({
            ...base,
            fontSize: "0.8rem",
          }),
        }}
      />
      <form className="create-announce-form">
        <label htmlFor="message">Message</label>
        <textarea
          id="message"
          name="message"
          placeholder="Message for the household..."
          required
        />
        <button type="submit">Update Announcement</button>
      </form>
    </div>
  );
}

export default EditAnnouncement;
