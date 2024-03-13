
function Announcement({key, message, author}){
    return (
        <div className="announcement-container">
            <div className="content">
                <p className="message">{message}</p>
                <p className="author">- {author}</p>
            </div>
            <button className="delete-btn">Delete</button>
        </div>
    )
}

export default Announcement;