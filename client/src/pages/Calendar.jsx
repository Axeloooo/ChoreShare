import CalendarWindow from '../components/CalendarWindow';
import '../styles/Calendar.css';
import { useOutletContext } from 'react-router-dom';
import AddEvent from '../components/AddEvent';


function Calendar({sidebarOpen}) {
    const { showOverlay } = useOutletContext();

    const handleShowCreateEvent = () => {
        showOverlay(<AddEvent />); 
    };

    return (
        <div className={sidebarOpen ? "calendar-page" : "calendar-page full-width"}>
            <div className="calendar-view">
                <div className="calendar-grid">
                    <CalendarWindow />
                </div>
                <button className="add-event-btn" onClick={handleShowCreateEvent}>+ Add Event</button>
            </div>
        </div>
    );
}

export default Calendar;