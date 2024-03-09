function HouseholdMember({key, index, name, username}) {

  console.log(index);

  return (
    <div className="member-container">
      <div className="member-identifier" style={{backgroundColor: `var(--user-${index})`}}></div>
      <div className="member-info">
        <p className="fullname">{name}</p>
        <p className="username">{username}</p>
      </div>
    </div>
  );
}

export default HouseholdMember;