import './styles/App.css';
import React from 'react';
import Layout from './components/Layout';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";
import Dashboard from './pages/Dashboard';
import Calendar from './pages/Calendar';
import ChoreList from './pages/ChoreList';
import LoginWindow from './pages/LoginWindow';
import SignupWindow from './pages/SignupWindow';

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [user, setUser] = useState('');

  return (
    <div className="app-container">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} user={user} setUser={setUser}/>}>
            <Route index element={<Dashboard sidebarOpen={sidebarOpen}/>} />
            <Route path="calendar" element={<Calendar sidebarOpen={sidebarOpen}/>} />
            <Route path="chore-list" element={<ChoreList sidebarOpen={sidebarOpen}/>} />
          </Route>
          <Route path="login" element={<LoginWindow />} />
          <Route path="register" element={<SignupWindow />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
