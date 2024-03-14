import "../styles/Dashboard.css";
import { Link } from "react-router-dom";
import { FaPencil } from "react-icons/fa6";
import { useOutletContext } from "react-router-dom";
import CreateAnnouncement from "../components/CreateAnnouncement";
import Announcement from "../components/Announcement";
import UpcomingEvent from "../components/UpcomingEvent";

function Dashboard({ sidebarOpen, currentHousehold }) {
  const tasks = [
    { title: "Wash Dishes", status: "Not Started" },
    { title: "Clean Bathroom", status: "Complete" },
    { title: "Vacuum", status: "Not Started" },
    { title: "Wash Dishes", status: "Not Started" },
    { title: "Wash Dishes", status: "Not Started" },
    { title: "Wash Dishes", status: "Not Started" },
  ];
  const announcements = [
    {
      message:
        "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Earum quidem nobis asperiores qui in voluptatem? Gvuwif hrbfiwojs xwifnoqmwkd. Earum quidem nobis asperiores qui in voluptatem? ",
      author: "John Doe",
    },
    {
      message: "Lorem ipsum dolor sit amet, consectetur.",
      author: "Anonymous",
    },
    {
      message: "Lorem ipsum dolor sit amet, consectetur.",
      author: "Anonymous",
    },
    {
      message: "Lorem ipsum dolor sit amet, consectetur.",
      author: "Anonymous",
    },
  ];
  const events = [
    {
      date: "5 Feb, 2024",
      title: "Family Dinner",
      time: "6:00 PM",
      author: "jhon2937",
    },
    {
      date: "5 Feb, 2024",
      title: "Family Dinner",
      time: "6:00 PM",
      author: "jhon2937",
    },
    {
      date: "5 Feb, 2024",
      title: "Family Dinner",
      time: "6:00 PM",
      author: "jhon2937",
    },
  ];
  const { showOverlay } = useOutletContext();

  const handleShowCreateAnnouncement = () => {
    showOverlay(<CreateAnnouncement />);
  };

  return (
    <>
      {currentHousehold != null ? (
        <div
          className={
            sidebarOpen ? "dashboard-page" : "dashboard-page full-width"
          }
        >
          <div className="grid-container">
            <div className="todo-view container">
              <div className="container-header space-between">
                <h3>My ToDo</h3>
                <Link to="/chore-list">
                  <p>View All Chores</p>
                </Link>
              </div>
              {tasks.length === 0 ? (
                <div className="container-contents empty">
                  No Chores Assigned
                </div>
              ) : (
                <div className="container-contents">
                  {tasks.map((task, index) => {
                    return (
                      <div className="my-chore-container" key={index}>
                        <p className="my-chore-title">{task.title}</p>
                        <div className="progress-edit">
                          <p
                            className="progress"
                            style={{
                              backgroundColor:
                                task.status === "Complete"
                                  ? "rgba(111, 191, 131, 0.5)"
                                  : "rgba(188, 204, 205, 0.5)",
                            }}
                          >
                            {task.status}
                          </p>
                          <FaPencil className="edit" />
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
            <div className="upcoming-events-view container">
              <div className="container-header space-between">
                <h3>Upcoming Events</h3>
                <Link to="/calendar">
                  <p>View Calendar</p>
                </Link>
              </div>
              {events.length === 0 ? (
                <div className="container-contents empty">
                  No Upcoming Events
                </div>
              ) : (
                <div className="container-contents">
                  {events.map((event, index) => {
                    return (
                      <UpcomingEvent
                        key={index}
                        date={event.date}
                        title={event.title}
                        time={event.time}
                        author={event.author}
                      />
                    );
                  })}
                </div>
              )}
            </div>
            <div className="announcements-view container">
              <div className="container-header space-between">
                <h3>Announcements</h3>
                <p onClick={handleShowCreateAnnouncement}>+ Create</p>
              </div>
              {announcements.length === 0 ? (
                <div className="container-contents empty">No Announcements</div>
              ) : (
                <div className="container-contents">
                  {announcements.map((announcement, index) => {
                    return (
                      <Announcement
                        key={index}
                        message={announcement.message}
                        author={announcement.author}
                      />
                    );
                  })}
                </div>
              )}
            </div>
            <div className="stats-view container">
              <div className="container-header center">
                <h3>Progress</h3>
              </div>
              <div className="container-contents stats">
                <div className="stat">
                  <h3>0/3</h3>
                  <p>My Chores Completed</p>
                </div>
                <div className="stat">
                  <h3>0/11</h3>
                  <p>Household Chores Completed</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div
          className={
            sidebarOpen
              ? "dashboard-page no-household"
              : "dashboard-page full-width no-household"
          }
        >
          <p className="text">
            You do not appear to be a member of any household
          </p>
          <p className="instructions">
            If you are the first member to create an account, click the 'Create
            Household' button in the header.
          </p>
          <p className="instructions">
            If you have been invited by a friend, you can join his/her household
            by clicking the 'Join Household' button in the header.
          </p>
        </div>
      )}
    </>
  );
}

export default Dashboard;
