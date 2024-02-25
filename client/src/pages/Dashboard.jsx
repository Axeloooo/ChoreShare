import '../styles/Dashboard.css';
import {Link} from "react-router-dom";
import { FaPencil } from "react-icons/fa6";
import { useOutletContext } from 'react-router-dom';
import CreateAnnouncement from '../components/CreateAnnouncement';

function Dashboard ({sidebarOpen}) {
const tasks = [{title : "Wash Dishes", status : "Not Started"}, {title : "Clean Bathroom", status : "Complete"}, {title : "Vacuum", status : "Not Started"}, {title : "Wash Dishes", status : "Not Started"}, {title : "Wash Dishes", status : "Not Started"}, {title : "Wash Dishes", status : "Not Started"}]
const announcements = []
const events = []
const { showOverlay } = useOutletContext();

const handleShowCreateAnnouncement = () => {
    showOverlay(<CreateAnnouncement />); 
};

  return (
    <div className={sidebarOpen ? "dashboard-page" : "dashboard-page full-width"}>
        <div className="grid-container">
            <div className="todo-view container">
                <div className="container-header space-between">
                    <h3>My ToDo</h3>
                    <Link to="/chore-list"><p>View All Chores</p></Link>
                </div>
                {tasks.length === 0 ? 
                <div className="container-contents empty">
                    No Chores Assigned
                </div> :
                <div className="container-contents">
                {tasks.map((task, index) => {
                    return (
                        <div className="my-chore-container" key={index}>
                                <p className="my-chore-title">
                                    {task.title}
                                </p>
                            <div className="progress-edit">
                                <p className="progress" style={{backgroundColor: task.status === "Complete" ? "rgba(39, 148, 30, 0.3)" : "rgba(248, 183, 124, 0.4)"}}>
                                    {task.status}
                                </p>
                                <FaPencil className="edit" />
                            </div>
                        </div>
                    )
                })}
                </div>
                }
            </div>
            <div className="announcements-view container">
                <div className="container-header space-between">
                    <h3>Announcements</h3>
                    <p onClick={handleShowCreateAnnouncement}>+ Create</p>
                </div>
                {announcements.length === 0 ? 
                <div className="container-contents empty">
                    No Announcements
                </div> :
                <div className="container-contents">
                    
                </div>
                }
            </div>
            <div className="upcoming-events-view container">
                <div className="container-header space-between">
                    <h3>Upcoming Events</h3>
                    <Link to="/calendar"><p>View Calendar</p></Link>
                </div>
                {events.length === 0 ? 
                <div className="container-contents empty">
                    No Upcoming Events
                </div> :
                <div className="container-contents">
                    
                </div>
                }
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
  );
}

export default Dashboard;