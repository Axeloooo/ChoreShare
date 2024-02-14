import '../styles/Calendar.css';

function Calendar({sidebarOpen}) {
    let days = [];

    for (let i = 0; i < 35; i++) {
      days.push({day: i + 1, events: []});
    }

    return (
        <div className={sidebarOpen ? "calendar-page" : "calendar-page full-width"}>
            <p className="calendar-month">February 2024</p>
            <div className="calendar-view">
                <div className="calendar-week-days">
                    <div className="calendar-week-day">Sun</div>
                    <div className="calendar-week-day">Mon</div>
                    <div className="calendar-week-day">Tue</div>
                    <div className="calendar-week-day">Wed</div>
                    <div className="calendar-week-day">Thu</div>
                    <div className="calendar-week-day">Fri</div>
                    <div className="calendar-week-day">Sat</div>
                </div>
                <div className="calendar-grid">
                    {days.map((day, index) => {
                        return (
                            <div key={index} className="calendar-day">
                            </div>
                        )
                    })}
                </div>
            </div>
        </div>
    );
}

export default Calendar;