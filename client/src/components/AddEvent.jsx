import '../styles/AddEvent.css';

function AddEvent(){
    return (
        <div className="add-event-window">
            <h1>Add an event</h1>
            <form className="add-event-form">
                <label htmlFor="member-email">Description</label>
                <input type="text" id="member-email" name="member-email" placeholder="Event name / description" required/>
                <div className="row">
                    <div className="row-entry">
                        <label htmlFor="member-email">Start Time</label>
                        <input type="time" id="member-email" name="member-email" required/>
                    </div>
                    <div className="row-entry">
                        <label htmlFor="member-email">End Time (optional)</label>
                        <input type="time" id="member-email" name="member-email" />
                    </div>
                </div>
                <button type="submit">Create Event</button>
            </form>
        </div>
    )
}

export default AddEvent;
