import "../styles/Chorelist.css";
import { useState, useEffect } from "react";
import ChoreCard from "../components/ChoreCard";
import CreateChore from "../components/CreateChore";
import { useOutletContext } from "react-router-dom";

function ChoreList({
  sidebarOpen,
  data,
  createChore,
  assignChore,
  deleteChore,
  editChore,
}) {
  const [threeColumns, setthreeColumns] = useState([]);
  const { showOverlay, closeOverlay } = useOutletContext();

  useEffect(() => {
    const threeColumns = data.allChores.reduce(function (columns, item, index) {
      const columnIndex = index % 3;
      const rowIndex = Math.floor(index / 3);
      if (!columns[columnIndex]) {
        columns[columnIndex] = [];
      }
      columns[columnIndex][rowIndex] = item;
      return columns;
    }, []);

    setthreeColumns(threeColumns);
  }, [data.allChores]);

  const handleShowCreateChore = () => {
    showOverlay(
      <CreateChore createChore={createChore} closeOverlay={closeOverlay} />
    );
  };

  return (
    <>
      {data.currentHousehold != null ? (
        <div className={sidebarOpen ? "chore-page" : "chore-page full-width"}>
          <div className="chore-container">
            <div className="title">
              <h3>11-17 Feb</h3>
              <p onClick={handleShowCreateChore}>+ Add Chore</p>
            </div>
            {data.events.length > 0 ? (
              <div className="chore-list">
                {threeColumns.map((column) => (
                  <div className="column">
                    {column.map((chore) => (
                      <ChoreCard
                        chore={chore}
                        assignChore={assignChore}
                        deleteChore={deleteChore}
                        editChore={editChore}
                        showOverlay={showOverlay}
                        closeOverlay={closeOverlay}
                      />
                    ))}
                  </div>
                ))}
              </div>
            ) : (
              <div className="chore-list-empty">
                <p>Household does not have any chores yet</p>
              </div>
            )}
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

export default ChoreList;
