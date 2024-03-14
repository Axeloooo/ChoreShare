import { useState } from "react";
import Select from "react-select";
import "../styles/CreateChore.css";

const frequencyOptions = [
  { value: "ONCE_A_WEEK", label: "Once a week", category: "frequency" },
  { value: "TWICE_A_WEEK", label: "Twice a week", category: "frequency" },
  { value: "EVERYDAY", label: "Every day", category: "frequency" },
  { value: "EVERY_OTHER_DAY", label: "Every other day", category: "frequency" },
];

const tagOptions = [
  { value: "KITCHEN", label: "Kitchen", category: "tag" },
  { value: "BATHROOM", label: "Bathroom", category: "tag" },
  { value: "LIVING_ROOM", label: "Living Room", category: "tag" },
  { value: "GENERAL", label: "General", category: "tag" },
];

const groupedOptions = [
  {
    label: "Frequencies",
    options: frequencyOptions,
  },
  {
    label: "Labels",
    options: tagOptions,
  },
];

const groupStyles = {
  display: "flex",
  alignItems: "center",
  justifyContent: "space-between",
};

const groupBadgeStyles = {
  backgroundColor: "#EBECF0",
  borderRadius: "2em",
  color: "#172B4D",
  display: "inline-block",
  fontSize: 12,
  fontWeight: "normal",
  lineHeight: "1",
  minWidth: 1,
  padding: "0.16666666666667em 0.5em",
  textAlign: "center",
};

const formatGroupLabel = (data) => (
  <div style={groupStyles}>
    <span>{data.label}</span>
    <span style={groupBadgeStyles}>{data.options.length}</span>
  </div>
);

function CreateChore({ createChore, closeOverlay }) {
  const [selectedOptions, setSelectedOptions] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleChange = (selected) => {
    const lastAddedOption = selected[selected.length - 1];

    if (lastAddedOption && selected.length >= selectedOptions.length) {
      const filteredOptions = selectedOptions.filter((option) => {
        return option.category !== lastAddedOption.category;
      });

      const newSelectedOptions = [...filteredOptions, lastAddedOption];

      setSelectedOptions(newSelectedOptions);
    } else {
      setSelectedOptions(selected);
    }
  };

  const handleCreateChore = (e) => {
    e.preventDefault();
    let frequency = "";
    let tag = "";
    selectedOptions.forEach((option) => {
      if (option.category === "frequency") {
        frequency = option.value;
      } else if (option.category === "tag") {
        tag = option.value;
      }
    });
    console.log(title, description, frequency, tag);
    createChore(title, description, frequency, tag, closeOverlay);
  };

  return (
    <div className="create-chore-window">
      <h1>Add a chore</h1>
      <label htmlFor="chore-tags" className="label">
        Tags
      </label>
      <Select
        className="select"
        id="chore-tags"
        isMulti
        options={groupedOptions}
        formatGroupLabel={formatGroupLabel}
        onChange={handleChange}
        value={selectedOptions}
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
        className="create-chore-form"
        action=""
        onSubmit={handleCreateChore}
      >
        <label htmlFor="chore-title">Title</label>
        <input
          type="text"
          id="chore-title"
          name="chore-title"
          placeholder="Chore Title"
          onChange={(e) => setTitle(e.target.value)}
          required
        />
        <label htmlFor="chore-description">Description (optional)</label>
        <textarea
          id="chore-description"
          name="chore-description"
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Description of the chore..."
        />
        <button type="submit">Create Chore</button>
      </form>
    </div>
  );
}

export default CreateChore;
