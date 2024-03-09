import "./styles/App.css";
import React from "react";
import { jwtDecode } from "jwt-decode";
import Layout from "./components/Layout";
import { Routes, Route, Navigate, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import Dashboard from "./pages/Dashboard";
import Calendar from "./pages/Calendar";
import ChoreList from "./pages/ChoreList";
import LoginWindow from "./pages/LoginWindow";
import SignupWindow from "./pages/SignupWindow";
import { toast } from "react-toastify";

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [user, setUser] = useState(
    localStorage.getItem("user")
      ? JSON.parse(localStorage.getItem("user"))
      : null
  );
  const [token, setToken] = useState(
    localStorage.getItem("token") ? localStorage.getItem("token") : null
  );
  const [userId, setUserId] = useState(
    localStorage.getItem("userId")
      ? JSON.parse(localStorage.getItem("userId"))
      : null
  );
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  localStorage.removeItem("user");
  localStorage.removeItem("token");
  localStorage.removeItem("userId");

  useEffect(() => {
    let loadingToast = null;

    if (isLoading) {
      loadingToast = toast.loading("Loading...");
    } else {
      if (loadingToast !== null) {
        toast.dismiss(loadingToast);
      }
    }

    return () => {
      if (loadingToast !== null) {
        toast.dismiss(loadingToast);
      }
    };
  }, [isLoading]);

  useEffect(() => {
    if (userId) {
      try {
        const fetchUser = async () => {
          const res = await fetch(
            `http://localhost:8888/api/v1/user/${userId}`,
            {
              method: "GET",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (!res.ok) {
            setIsLoading(false);
            const response = await res.json();
            toast.error(response.message);
            return;
          }

          const data = await res.json();
          setIsLoading(false);
          toast.success("Login successful!");
          console.log(data);
          setUser(data);

          navigate("/");
        };
        fetchUser();
      } catch (error) {
        setIsLoading(false);
        toast.error(error);
        console.error(error);
      }
    }
  }, [userId]);

  const login = (username, password) => {
    setIsLoading(true);
    try {
      const fetchLogin = async () => {
        const res = await fetch("http://localhost:8888/api/v1/auth/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: username,
            password: password,
          }),
        });

        if (!res.ok) {
          setIsLoading(false);
          const response = await res.json();
          toast.error(response.message);
          return;
        }

        const token = await res.text();
        localStorage.setItem("token", token);
        setToken(token);

        const decoded = jwtDecode(token);
        localStorage.setItem("userId", JSON.stringify(decoded.sub));
        setUserId(decoded.sub);
      };
      fetchLogin();
    } catch (error) {
      setIsLoading(false);
      toast.error(error);
      console.error(error);
    }
  };

  const logout = () => {
    const answer = window.confirm("Are you sure you want to log out?");
    if (answer) {
      setUser(null);
      setToken(null);
      setUserId(null);
      localStorage.removeItem("user");
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      navigate("/login");
    }
  };

  return (
    <div className="app-container">
      <Routes>
        {user ? (
          <Route
            path="/"
            element={
              <Layout
                sidebarOpen={sidebarOpen}
                setSidebarOpen={setSidebarOpen}
                user={user}
                logout={logout}
              />
            }
          >
            <Route index element={<Dashboard sidebarOpen={sidebarOpen} />} />
            <Route
              path="calendar"
              element={<Calendar sidebarOpen={sidebarOpen} />}
            />
            <Route
              path="chore-list"
              element={<ChoreList sidebarOpen={sidebarOpen} />}
            />
          </Route>
        ) : (
          <Route path="*" element={<Navigate to="/login" replace />} />
        )}
        <Route path="login" element={<LoginWindow login={login} />} />
        <Route path="register" element={<SignupWindow />} />
      </Routes>
    </div>
  );
}

export default App;
