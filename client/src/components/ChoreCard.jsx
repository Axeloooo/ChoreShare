import "../styles/ChoreCard.css";

function ChoreCard({ chore }) {
  function toTitleCase(str) {
    return str.toLowerCase().replace(/\b(\w)/g, (s) => s.toUpperCase());
  }

  function formatString(str) {
    return toTitleCase(str.replace(/_/g, " "));
  }

  return (
    <div className="chore-card">
      <div className="chore-content">
        <div className="chore-card-header">
          <h3>{chore.title}</h3>
          {chore.userId == null ? (
            <button>Take Chore</button>
          ) : (
            <button disabled>Chore Taken</button>
          )}
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
