import Calendar from 'rsuite/Calendar';
import 'rsuite/Calendar/styles/index.css';

const events = [
    { date: '2024-02-08', title: "Lunch Meeting", start: "12:00 PM", end: "1:00 PM" },
    { date: '2024-02-12', title: "Project Deadline", start: "12:00 PM", end: "1:00 PM"},
    // Add more events as needed
  ];

function CalendarWindow() {
  const renderCell = (date) => {
    const dayEvents = events.filter(event => {
        // Create a Date object from the event's date string
        const eventDate = new Date(event.date);
        // Compare the year, month, and day parts directly
        return eventDate.getFullYear() === date.getFullYear() &&
               eventDate.getMonth() === date.getMonth() &&
               eventDate.getDate() === date.getDate();
    });


      return (
          <div>
            {dayEvents.map((event, index) => (
              <div key={index} style={{ marginTop: 5, fontSize: 12, color: '#0c5460', backgroundColor: '#d1ecf1', borderRadius: 4, padding: '2px 5px' }}>
                {event.title}
                <p>{event.start} - {event.end}</p>
              </div>
            ))}
          </div>
        );
      };
    
      return (
        <div>
          <Calendar renderCell={renderCell} bordered/>
        </div>
      );
}

export default CalendarWindow;
