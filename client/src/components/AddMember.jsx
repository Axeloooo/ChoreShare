import "../styles/AddMember.css";
import { useState } from "react";

function AddMember({ inviteMember, closeOverlay }) {
  const [email, setEmail] = useState("");

  const handleInviteMember = async (e) => {
    e.preventDefault();
    const email = e.target["member-email"].value;
    await inviteMember(email);
    closeOverlay();
  };

  return (
    <div className="add-member-window">
      <h1>Invite a friend</h1>
      <form className="add-member-form" action="" onSubmit={handleInviteMember}>
        <label htmlFor="member-email">Email Address</label>
        <input
          type="text"
          id="member-email"
          name="member-email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Invitee's Email Adress"
          required
        />
        <button type="submit">Send Invite</button>
      </form>
    </div>
  );
}

export default AddMember;
