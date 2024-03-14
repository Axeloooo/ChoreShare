import { FaPencil, FaTrashCan } from "react-icons/fa6";

function MyChore({ index, task, unassignChore }) {
  function toTitleCase(str) {
    return str.toLowerCase().replace(/\b(\w)/g, (s) => s.toUpperCase());
  }

  function formatString(str) {
    return toTitleCase(str.replace(/_/g, " "));
  }

  const handleUnassign = () => {
    const confirmation = window.confirm(
      "Are you sure you want to dump this chore?"
    );
    if (confirmation) {
      unassignChore(task.id);
    }
  };

  return (
    <div className="my-chore-container" key={index}>
      <p className="my-chore-title">{task.title}</p>
      <div className="progress-edit">
        <p
          className="progress"
          style={{
            backgroundColor:
              task.status === "Complete"
                ? "rgba(111, 191, 131, 0.5)"
                : "rgba(188, 204, 205, 0.5)",
          }}
        >
          {formatString(task.status)}
        </p>
        <FaTrashCan className="icon" onClick={handleUnassign} />
      </div>
    </div>
  );
}

export default MyChore;
