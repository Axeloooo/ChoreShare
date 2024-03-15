import EditAnnouncement from "./EditAnnouncement";
function Announcement({ key, message, author, showOverlay, closeOverlay }) {
  const handleShowEditAnnouncement = () => {
    showOverlay(<EditAnnouncement closeOverlay={closeOverlay} />);
  };

  return (
    <div className="announcement-container">
      <div className="content">
        <p className="message">{message}</p>
        <p className="author">- {author}</p>
      </div>
      {/* ONLY DISPLAY BUTTONS IF YOU'RE THE OWNER OF THE ANNOUNCEMENT */}
      <div className="buttons">
        <button className="delete">Delete</button>
        <button className="edit" onClick={handleShowEditAnnouncement}>
          Edit
        </button>
      </div>
    </div>
  );
}

export default Announcement;
