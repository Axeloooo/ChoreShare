import "../styles/CreateAnnouncement.css";
import Select from "react-select";
import { useState } from "react";
import { toast } from "react-toastify";

function CreateAnnouncement({ user, createAnnouncement, closeOverlay }) {
  const [message, setMessage] = useState("");
  const [selectedOption, setSelectedOption] = useState(null);

  const options = [
    { value: null, label: "Submit Anonymously" },
    { value: "username", label: "Show Author" },
  ];

  const handleCreateAnnouncement = (e) => {
    e.preventDefault();
    if (!selectedOption) {
      toast.warn("Please select an option");
      return;
    }
    const author = selectedOption.value === "username" ? user.username : null;
    createAnnouncement(message, author, closeOverlay);
  };

  return (
    <div className="create-announce-window">
      <h1>Add an announcement</h1>
      <label htmlFor="announce-options" className="label">
        Options
      </label>
      <Select
        options={options}
        value={selectedOption}
        onChange={setSelectedOption}
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
      <form
        className="create-announce-form"
        onSubmit={handleCreateAnnouncement}
      >
        <label htmlFor="message">Message</label>
        <textarea
          id="message"
          name="message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Message for the household..."
          required
        />
        <button type="submit">Create Announcement</button>
      </form>
    </div>
  );
}

export default CreateAnnouncement;
