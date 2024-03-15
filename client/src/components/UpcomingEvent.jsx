function UpcomingEvent({ key, index, date, title, time, author }) {
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
        {/* IF EVENT IS YOURS, ONLY DISPLAY DELETE BUTTON (NO USER), IF EVENT IS NOT YOURS ONLY DISPLAY USER (NO DELETE BUTTON)  */}
        <div className="user">
          <div className="identifier"></div>
          <p className="username">{author}</p>
        </div>
        <button className="delete">Delete</button>
      </div>
    </div>
  );
}

export default UpcomingEvent;
