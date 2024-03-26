import "../styles/CreateAnnouncement.css";
import Select from "react-select";
import { useState } from "react";
import { toast } from "react-toastify";

function EditAnnouncement({
  closeOverlay,
  announcement,
  editAnnouncement,
  user,
}) {
  const options = [
    { value: null, label: "Submit Anonymously" },
    { value: "username", label: "Show Author" },
  ];

  const [selectedOption, setSelectedOption] = useState(
    announcement.author ? options[1] : options[0]
  );
  const [message, setMessage] = useState(announcement.message);

  console.log("selected option:", selectedOption);

  const handleEditAnnouncement = (e) => {
    e.preventDefault();
    if (!selectedOption) {
      toast.warn("Please select an option");
      return;
    }
    const author = selectedOption.value === "username" ? user.username : null;
    console.log(author);
    editAnnouncement(announcement.id, message, author, closeOverlay);
  };

  return (
    <div className="create-announce-window">
      <h1>Edit announcement</h1>
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
        action=""
        onSubmit={handleEditAnnouncement}
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
        <button type="submit">Update Announcement</button>
      </form>
    </div>
  );
}

export default EditAnnouncement;
