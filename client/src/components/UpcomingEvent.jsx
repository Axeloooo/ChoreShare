
function UpcomingEvent({key, index, date, title, time, author}) {
  return (
    <div className="upcoming-event-container">
        <div className="left-container">
            <div className="date">
                <p>{date}</p>
            </div>
            <div className="info">
                <p className="title">{title}</p>
                <p className="time">{time}</p>
            </div>
        </div>
        <div className="right-container">
            <div className="identifier"></div>
            <p className="username">{author}</p>
        </div>
    </div>
  );
}

export default UpcomingEvent;