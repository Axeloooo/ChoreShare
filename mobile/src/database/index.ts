import * as SQLite from "expo-sqlite";

const dbName: string = "sessions.db";
const createSessionsTableSql: string = `
CREATE TABLE IF NOT EXISTS sessions (
  id INTEGER PRIMARY KEY NOT NULL,
  userId TEXT NOT NULL,
  token TEXT NOT NULL
)`;
const insertSessionSql = `
          INSERT INTO sessions (userId, token) VALUES (?, ?)
        `;
const deleteSessionSql = `
          DELETE FROM sessions WHERE userId = ?
        `;
const getSessionSql = `
          SELECT * FROM sessions
        `;

const db = SQLite.openDatabase(dbName);

export const createTables = async (): Promise<void> => {
  db.transaction((tx) => {
    tx.executeSql(createSessionsTableSql);
  });
};

export const insertSession = async (
  userId: string,
  token: string
): Promise<void> => {
  db.transaction((tx) => {
    tx.executeSql(insertSessionSql, [userId, token]);
  });
};

export const fetchSession = async (): Promise<SQLite.SQLResultSet> => {
  return new Promise((resolve, reject) => {
    db.transaction((tx) => {
      tx.executeSql(
        getSessionSql,
        [],
        (_, result) => {
          resolve(result);
        },
        (_, error) => {
          reject(error);
          return true;
        }
      );
    });
  });
};

export const deleteSession = async (userId: string): Promise<void> => {
  db.transaction((tx) => {
    tx.executeSql(deleteSessionSql, [userId]);
  });
};
