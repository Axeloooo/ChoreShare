import "../styles/ChoreCard.css";
import { FaPencil, FaTrashCan } from "react-icons/fa6";
import EditChore from "./EditChore";

function ChoreCard({
  chore,
  assignChore,
  deleteChore,
  editChore,
  showOverlay,
  closeOverlay,
}) {
  function toTitleCase(str) {
    return str.toLowerCase().replace(/\b(\w)/g, (s) => s.toUpperCase());
  }

  function formatString(str) {
    return toTitleCase(str.replace(/_/g, " "));
  }

  const handleAssign = () => {
    const confirmation = window.confirm(
      "Are you sure you want to take this chore?"
    );
    if (confirmation) {
      assignChore(chore.id);
    }
  };

  const handleDeleteChore = () => {
    const confirmation = window.confirm(
      "Are you sure you want to delete this chore?"
    );
    if (confirmation) {
      deleteChore(chore.id);
    }
  };

  const handleShowEditChore = () => {
    showOverlay(
      <EditChore
        chore={chore}
        editChore={editChore}
        closeOverlay={closeOverlay}
      />
    );
  };

  return (
    <div className="chore-card">
      <div className="chore-content">
        <div className="chore-card-header">
          <h3>{chore.title}</h3>
          <div className="chore-actions">
            <FaPencil className="icon" onClick={handleShowEditChore} />
            <FaTrashCan className="icon" onClick={handleDeleteChore} />
            {chore.userId == null ? (
              <button onClick={handleAssign}>Grab Chore</button>
            ) : (
              <button className="disabled" disabled>
                Taken
              </button>
            )}
          </div>
        </div>
        {chore.description !== "" ? (
          <p className="comment">{chore.description}</p>
        ) : null}
        <div className="chore-tags">
          <p
            className="place"
            style={{ backgroundColor: "rgba(248, 183, 124, 0.5)" }}
          >
            {formatString(chore.tag)}
          </p>
          <p
            className="repeat"
            style={{ backgroundColor: "rgba(182, 124, 248, 0.5)" }}
          >
            {formatString(chore.frequency)}
          </p>
        </div>
      </div>
    </div>
  );
}

export default ChoreCard;
