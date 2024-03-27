import Calendar from "rsuite/Calendar";
import "rsuite/Calendar/styles/index.css";

function CalendarWindow({ data }) {
  const renderCell = (date) => {
    if (!data.events) {
      return null;
    }
    const dayEvents = data.events.filter((event) => {
      const eventDate = new Date(event.startTime);

      return (
        eventDate.getFullYear() === date.getFullYear() &&
        eventDate.getMonth() === date.getMonth() &&
        eventDate.getDate() === date.getDate()
      );
    });

    return (
      <div style={{ maxHeight: "100px", overflowY: "auto" }}>
        {dayEvents.map((event, index) => {
          const startTime = new Date(event.startTime);
          const endTime = event.endTime ? new Date(event.endTime) : null;

          const formattedStartTime = new Intl.DateTimeFormat("en-US", {
            hour: "2-digit",
            minute: "2-digit",
            hour12: false,
            timeZone: "UTC",
          }).format(startTime);

          const formattedEndTime = endTime
            ? new Intl.DateTimeFormat("en-US", {
                hour: "2-digit",
                minute: "2-digit",
                hour12: false,
                timeZone: "UTC",
              }).format(endTime)
            : "";

          return (
            <div
              key={index}
              style={{
                marginTop: 5,
                fontSize: 12,
                color: "#0c5460",
                backgroundColor: "#d1ecf1",
                borderRadius: 4,
                padding: "2px 5px",
              }}
            >
              {event.title}
              <p>
                {formattedStartTime}
                {formattedEndTime ? ` - ${formattedEndTime}` : ""}
              </p>
            </div>
          );
        })}
      </div>
    );
  };

  return (
    <div>
      <Calendar renderCell={renderCell} bordered />
    </div>
  );
}

export default CalendarWindow;
