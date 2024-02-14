import '../styles/Dashboard.css';

function Dashboard ({sidebarOpen}) {
const tasks = [{title : "Wash Dishes", status : "Not Started"}, {title : "Clean Bathroom", status : "Complete"}, {title : "Vacuum", status : "Not Started"}, {title : "Wash Dishes", status : "Not Started"}, {title : "Wash Dishes", status : "Not Started"}, {title : "Wash Dishes", status : "Not Started"}]


  return (
    <div className={sidebarOpen ? "dashboard-page" : "dashboard-page full-width"}>
        <div className="grid-container">
            <div className="todo-view container">
                <div className="container-header together">
                    <h3>My ToDo</h3>
                    <p>View All Chores</p>
                </div>
                <div className="container-contents">
                    {tasks.map((task, index) => {
                        return (
                            <div className="my-chore-container" key={index}>
                                <div className="my-chore-info">
                                    <p className="my-chore-title">
                                        {task.title}
                                    </p>
                                    <p className="progress" style={{backgroundColor: task.status === "Complete" ? "rgba(39, 148, 30, 0.3)" : "rgba(248, 183, 124, 0.3)"}}>
                                        {task.status}
                                    </p>
                                </div>
                                <div className="progress-edit">
                                    Update
                                </div>
                            </div>
                        )
                    })}
                </div>
            </div>
            <div className="all-chores-view container">
                <div className="container-header center">
                    <h3>Announecements</h3>
                </div>
                <div className="container-contents">
                    
                </div>
            </div>
            <div className="upcoming-events-view container">
                <div className="container-header space-between">
                    <h3>Upcoming Events</h3>
                    <p>View Full</p>
                </div>
                <div className="container-contents">
                    
                </div>
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