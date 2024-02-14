import '../styles/Chorelist.css'
import {useState, useEffect} from "react";
import ChoreCard from "../components/ChoreCard";

function ChoreList({sidebarOpen}){
    const chores = [{title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}, {title : "Take out trash", assignee : "none", comment : "Place bags in every bin!", place : "General", repeat : "Every 2 Weeks"}, {title : "Unload Dishwasher", assignee : "none", comment : "", place : "Bathroom", repeat : "Every Week"}, {title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}, {title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}, {title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}, {title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}, {title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}, {title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}, {title : "Clean Bathroom", assignee : "none", comment : "Wipe mirror, floor and counters. Wash the shower walls please!", place : "Bathroom", repeat : "Every Week"}]
    const [threeColumns, setthreeColumns] = useState([]);

    useEffect (() => {
    const threeColumns = chores.reduce(function (columns, item, index) {
        const columnIndex = index % 3;
        const rowIndex = Math.floor(index / 3);
        if (!columns[columnIndex]) {
            columns[columnIndex] = [];
        }
        columns[columnIndex][rowIndex] = item;
        return columns;
    }, []);
        
        setthreeColumns(threeColumns);
    }, []);

    return (
        <div className={sidebarOpen ? "chore-page" : "chore-page full-width"}>
            <div className="chore-container">
                <div className="title">
                    <h3>11-17 Feb</h3>
                </div>
                <div className="chore-list">
                    {threeColumns.map((column) => (
                    <div className="column">
                        {column.map((chore) => (
                        <ChoreCard chore={chore}/>
                        ))}
                    </div>
                    ))}
                </div>
            </div>
        </div>
    )
}

export default ChoreList;