import '../styles/CreateAnnouncement.css';
import { useState } from 'react';
import Select from 'react-select';

const options = [
    { value: 'anonymous', label: 'Submit Anonymously' },
    { value: 'names', label: 'Show Author' },
  ]

function CreateAnnouncement(){

    return (
        <div className="create-announce-window">
            <h1>Add an announcement</h1>
            <label htmlFor="announce-options" className="label">Options</label>
            <Select 
            options={options}
            defaultValue={options[0]}
            />
            <form className="create-announce-form">
                <label htmlFor="message">Message</label>
                <textarea id="message" name="message" />
                <button type="submit">Create Announcement</button>
            </form>
        </div>
    )
}

export default CreateAnnouncement;