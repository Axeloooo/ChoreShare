import { FaPencil, FaTrashCan } from "react-icons/fa6";
import EditChoreStatus from "./EditChoreStatus";

function MyChore({
  index,
  chore,
  unassignChore,
  showOverlay,
  closeOverlay,
  editChoreStatus,
}) {
  console.log(chore);

  function getStatusColor(status) {
    switch (status) {
      case "COMPLETED":
        return "rgba(111, 191, 131, 0.3)"; // Greenish
      case "PENDING":
        return "rgba(188, 204, 205, 0.3)"; // Grayish
      case "IN_PROGRESS":
        return "rgba(255, 193, 7, 0.3)"; // Yellowish
      default:
        return "rgba(188, 204, 205, 0.3)"; // Default color (light redish) if none of the above
    }
  }

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
      unassignChore(chore.id);
    }
  };

  const handleShowEditStatus = () => {
    showOverlay(
      <EditChoreStatus
        chore={chore}
        editChoreStatus={editChoreStatus}
        closeOverlay={closeOverlay}
      />
    );
  };

  return (
    <div className="my-chore-container" key={index}>
      <p className="my-chore-title">{chore.title}</p>
      <div className="progress-edit">
        <p
          className="progress"
          style={{ backgroundColor: getStatusColor(chore.status) }}
        >
          {formatString(chore.status)}
        </p>
        <FaPencil className="icon" onClick={handleShowEditStatus} />
        <FaTrashCan className="icon" onClick={handleUnassign} />
      </div>
    </div>
  );
}

export default MyChore;
