import './styles/App.css';
import React from 'react';
import Layout from './components/Layout';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";
import Dashboard from './pages/Dashboard';
import Calendar from './pages/Calendar';

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(true);

  return (
    <div className="app-container">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}/>}>
            <Route index element={<Dashboard sidebarOpen={sidebarOpen}/>} />
            <Route path="calendar" element={<Calendar />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
