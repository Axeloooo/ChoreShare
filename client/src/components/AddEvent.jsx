import "../styles/AddEvent.css";
import { useState } from "react";

// Add event
function AddEvent({ createEvent, closeOverlay }) {
  const [title, setTitle] = useState("");
  const [date, setDate] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const handleCreateEvent = (e) => {
    e.preventDefault();

    const formattedStartTime = date && startTime ? `${date}T${startTime}:00Z` : null;
    const formattedEndTime = date && endTime ? `${date}T${endTime}:00Z` : null;

    createEvent(title, formattedStartTime, formattedEndTime, closeOverlay);
  };

  return (
    <div className="add-event-window">
      <h1>Add an event</h1>
      <form className="add-event-form" action="" onSubmit={handleCreateEvent}>
        <label htmlFor="description">Description</label>
        <input
          type="text"
          id="description"
          name="description"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Event name / description"
          required
        />
        <label htmlFor="event-date">Event Date</label>
        <input
          type="date"
          id="event-date"
          name="event-date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          required
        />
        <div className="row">
          <div className="row-entry">
            <label htmlFor="start-time">Start Time</label>
            <input
              type="time"
              id="start-time"
              name="start-time"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
              required
            />
          </div>
          <div className="row-entry">
            <label htmlFor="end-time">End Time (optional)</label>
            <input
              type="time"
              id="end-time"
              name="mend-time"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
            />
          </div>
        </div>
        <button type="submit">Create Event</button>
      </form>
    </div>
  );
}

export default AddEvent;
