import EditAnnouncement from "./EditAnnouncement";
function Announcement({
  key,
  announcement,
  showOverlay,
  closeOverlay,
  userId,
  deleteAnnouncement,
  editAnnouncement,
  user,
}) {
  const handleShowEditAnnouncement = () => {
    showOverlay(
      <EditAnnouncement
        closeOverlay={closeOverlay}
        announcement={announcement}
        editAnnouncement={editAnnouncement}
        user={user}
      />
    );
  };

  const handleDeleteAnnouncement = () => {
    const confirmation = window.confirm(
      "Are you sure you want to delete this announcement?"
    );
    if (confirmation) {
      deleteAnnouncement(announcement.id);
    }
  };

  return (
    <div className="announcement-container">
      <div className="content">
        <p className="message">{announcement.message}</p>
        {announcement.author ? (
          <p className="author">- {announcement.author}</p>
        ) : (
          <p className="author">- Anonymous</p>
        )}
      </div>
      {userId === announcement.userId && (
        <div className="buttons">
          <button className="delete" onClick={handleDeleteAnnouncement}>
            Delete
          </button>
          <button className="edit" onClick={handleShowEditAnnouncement}>
            Edit
          </button>
        </div>
      )}
    </div>
  );
}

export default Announcement;
