function UpcomingEvent({ key, index, event, userId, deleteEvent }) {
  const startDate = new Date(event.startTime);
  const endDate = event.endTime ? new Date(event.endTime) : null;

  const formattedDate = startDate.toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });

  const formattedStartTime = new Intl.DateTimeFormat("en-US", {
    hour: "numeric",
    minute: "2-digit",
    hour12: false,
    timeZone: "UTC",
  }).format(startDate);

  const formattedEndTime = endDate
    ? new Intl.DateTimeFormat("en-US", {
        hour: "numeric",
        minute: "2-digit",
        hour12: false,
        timeZone: "UTC",
      }).format(endDate)
    : null;

  const handleDeleteEvent = () => {
    const confirm = window.confirm(
      "Are you sure you want to delete this event?"
    );
    if (confirm) {
      deleteEvent(event.id);
    }
  };

  return (
    <div className="upcoming-event-container">
      <div className="left-container">
        <div className="date">
          <p>{formattedDate}</p>
        </div>
        <div className="info">
          <p className="title">{event.title}</p>
          {endDate ? (
            <p className="time">
              {formattedStartTime} - {formattedEndTime}
            </p>
          ) : (
            <p className="time">{formattedStartTime}</p>
          )}
        </div>
      </div>
      <div className="right-container">
        {event.userId === userId ? (
          <button className="delete" onClick={handleDeleteEvent}>
            Delete
          </button>
        ) : (
          <div className="user">
            <div className="identifier"></div>
            <p className="username">{event.username}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default UpcomingEvent;
