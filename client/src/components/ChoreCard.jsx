import '../styles/ChoreCard.css';

function ChoreCard({ chore }) {
  return (
    <div className="chore-card">
        <div className="chore-content">
            <div className="chore-card-header">
                <h3>{chore.title}</h3>
                <p>Take Chore</p>
            </div>
            {chore.comment !== "" ? <p className="comment">{chore.comment}</p> : null}
            <div className="chore-tags">
                <p className="place" style={{backgroundColor: "rgba(248, 183, 124, 0.5)"}}>{chore.place}</p>
                <p className="repeat" style={{backgroundColor: "rgba(182, 124, 248, 0.5)"}}>{chore.repeat}</p>
            </div>
        </div>
    </div>
  );
}

export default ChoreCard;
