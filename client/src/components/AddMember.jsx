import '../styles/AddMember.css';

function AddMember(){
    return (
        <div className="add-member-window">
            <h1>Invite a friend</h1>
            <form className="add-member-form">
                <label htmlFor="member-email">Email Address</label>
                <input type="text" id="member-email" name="member-email" />
                <button type="submit">Send Invite</button>
            </form>
        </div>
    )
}

export default AddMember;