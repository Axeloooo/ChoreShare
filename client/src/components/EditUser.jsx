import '../styles/EditUser.css';

function EditUser(){
    return (
        <div className="edit-user-window">
            <h1>My Info</h1>
            <form className="edit-user-form">
                {/* Name */}
                <div className="row">
                    <div className="row-entry">
                        <label htmlFor="first-name">First Name</label>
                        <input type="text" id="first-name" name="first-name" placeholder='John' required/>
                    </div>
                    <div className="row-entry">
                        <label htmlFor="last-name">Last Name</label>
                        <input type="text" id="last-name" name="last-name" placeholder='Doe' required/>
                    </div>
                </div>
                {/* Username */}
                <label htmlFor="username">Username</label>
                <input type="text" id="username" name="username" placeholder="johndoe2038" required/>
                {/* Email */}
                <label htmlFor="email">Email</label>
                <input type="email" id="email" name="email" placeholder="john@gmail.com" required/>
                {/* Phone */}
                <label htmlFor="phone">Phone</label>
                <input type="text" pattern='^[0-9]*$' id="phone" name="phone" placeholder="123456789" required/>
                
                <button type="submit">Update Info</button>
            </form>
        </div>
    )
}

export default EditUser;
