import "../styles/EditUser.css";
import { useState } from "react";

function EditUser({ closeOverlay, user, editUser }) {
  const [firstName, setFirstName] = useState(user.firstName || "");
  const [lastName, setLastName] = useState(user.lastName || "");
  const [email, setEmail] = useState(user.email || "");
  const [phone, setPhone] = useState(user.phone || "");

  const handleEditUser = (e) => {
    e.preventDefault();
    editUser(firstName, lastName, email, phone, closeOverlay);
  };

  return (
    <div className="edit-user-window">
      <h1>My Info</h1>
      <form className="edit-user-form" action="" onSubmit={handleEditUser}>
        {/* Name */}
        <div className="row">
          <div className="row-entry">
            <label htmlFor="first-name">First Name</label>
            <input
              type="text"
              id="first-name"
              name="first-name"
              placeholder="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              required
            />
          </div>
          <div className="row-entry">
            <label htmlFor="last-name">Last Name</label>
            <input
              type="text"
              id="last-name"
              name="last-name"
              placeholder="Last Name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              required
            />
          </div>
        </div>
        {/* Username */}
        <label htmlFor="username">Username</label>
        <input
          type="text"
          id="username"
          name="username"
          placeholder="Username"
          value={user.username}
          readOnly
          required
        />
        {/* Email */}
        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="Your Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        {/* Phone */}
        <label htmlFor="phone">Phone</label>
        <input
          type="text"
          pattern="^[0-9]*$"
          id="phone"
          name="phone"
          placeholder="Your Phone Number"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
          required
        />
        <button type="submit">Update Info</button>
      </form>
    </div>
  );
}

export default EditUser;
