import CalendarWindow from "../components/CalendarWindow";
import "../styles/Calendar.css";
import { useOutletContext } from "react-router-dom";
import AddEvent from "../components/AddEvent";

function Calendar({ sidebarOpen, createEvent, data }) {
  const { showOverlay, closeOverlay } = useOutletContext();

  const handleShowCreateEvent = () => {
    showOverlay(
      <AddEvent createEvent={createEvent} closeOverlay={closeOverlay} />
    );
  };

  return (
    <div className={sidebarOpen ? "calendar-page" : "calendar-page full-width"}>
      <div className="calendar-view">
        <div className="calendar-grid">
          <CalendarWindow data={data} />
        </div>
        <button className="add-event-btn" onClick={handleShowCreateEvent}>
          + Add Event
        </button>
      </div>
    </div>
  );
}

export default Calendar;
