import "../styles/Dashboard.css";
import { Link } from "react-router-dom";
import { useOutletContext } from "react-router-dom";
import { useState, useEffect } from "react";
import CreateAnnouncement from "../components/CreateAnnouncement";
import Announcement from "../components/Announcement";
import UpcomingEvent from "../components/UpcomingEvent";
import MyChore from "../components/MyChore";

function Dashboard({
  sidebarOpen,
  data,
  unassignChore,
  editChoreStatus,
  user,
  userId,
  createAnnouncement,
  deleteAnnouncement,
  editAnnouncement,
  deleteEvent,
}) {
  const [myChoresProgress, setMyChoresProgress] = useState("");
  const [allChoresProgress, setAllChoresProgress] = useState("");
  const { showOverlay, closeOverlay } = useOutletContext();

  const handleShowCreateAnnouncement = () => {
    showOverlay(
      <CreateAnnouncement
        user={user}
        createAnnouncement={createAnnouncement}
        closeOverlay={closeOverlay}
      />
    );
  };

  useEffect(() => {
    const myChoresCompleted = data.myChores.reduce(
      (acc, chore) => acc + (chore.status === "COMPLETED" ? 1 : 0),
      0
    );
    const totalMyChores = data.myChores.length;
    setMyChoresProgress(`${myChoresCompleted}/${totalMyChores}`);

    const allChoresCompleted = data.allChores.reduce(
      (acc, chore) => acc + (chore.status === "COMPLETED" ? 1 : 0),
      0
    );
    const totalAllChores = data.allChores.length;
    setAllChoresProgress(`${allChoresCompleted}/${totalAllChores}`);
  }, [data.myChores, data.allChores]);

  return (
    <>
      {data.currentHousehold != null ? (
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
              {data.myChores.length === 0 ? (
                <div className="container-contents empty">
                  No Chores Assigned
                </div>
              ) : (
                <div className="container-contents">
                  {data.myChores.map((chore, index) => {
                    return (
                      <MyChore
                        index={index}
                        chore={chore}
                        unassignChore={unassignChore}
                        showOverlay={showOverlay}
                        closeOverlay={closeOverlay}
                        editChoreStatus={editChoreStatus}
                      />
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
              {data.events.length === 0 ? (
                <div className="container-contents empty">
                  No Upcoming Events
                </div>
              ) : (
                <div className="container-contents">
                  {data.events
                    .sort(
                      (a, b) => new Date(a.startTime) - new Date(b.startTime)
                    )
                    .map((event, index) => {
                      return (
                        <UpcomingEvent
                          key={index}
                          event={event}
                          userId={userId}
                          deleteEvent={deleteEvent}
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
              {data.announcements.length === 0 ? (
                <div className="container-contents empty">No Announcements</div>
              ) : (
                <div className="container-contents">
                  {data.announcements.map((announcement, index) => {
                    return (
                      <Announcement
                        key={index}
                        announcement={announcement}
                        showOverlay={showOverlay}
                        closeOverlay={closeOverlay}
                        userId={userId}
                        deleteAnnouncement={deleteAnnouncement}
                        editAnnouncement={editAnnouncement}
                        user={user}
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
                  <h3>{myChoresProgress}</h3>
                  <p>My Chores Completed</p>
                </div>
                <div className="stat">
                  <h3>{allChoresProgress}</h3>
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
