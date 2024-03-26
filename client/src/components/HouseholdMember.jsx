function HouseholdMember({ key, index, member }) {
  return (
    <div className="member-container">
      <div
        className="member-identifier"
        style={{ backgroundColor: `var(--user-${index})` }}
      ></div>
      <div className="member-info">
        <p className="fullname">
          {member.user.firstName} {member.user.lastName}
        </p>
        <p className="username">{member.user.username}</p>
      </div>
    </div>
  );
}

export default HouseholdMember;
