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
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [user, setUser] = useState(
    localStorage.getItem("user")
      ? JSON.parse(localStorage.getItem("user"))
      : null
  );
  const [token, setToken] = useState(null);
  const [userId, setUserId] = useState(null);
  const [households, setHouseholds] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [loginSuccess, setLoginSuccess] = useState(false);
  const [registerSuccess, setRegisterSuccess] = useState(false);
  const [haveHousehold, setHaveHousehold] = useState(false);
  const navigate = useNavigate();

  localStorage.removeItem("user");
  localStorage.removeItem("token");
  localStorage.removeItem("userId");
  localStorage.removeItem("households");
  console.log(userId);

  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    const storedUserId = localStorage.getItem("userId");
    const storedHouseholds = localStorage.getItem("households");

    if (storedToken && storedUserId) {
      setToken(storedToken);
      setUserId(JSON.parse(storedUserId));
    }
    if (storedHouseholds) {
      const householdsData = JSON.parse(storedHouseholds);
      setHouseholds(householdsData);

      if (householdsData.length > 0) {
        console.log("have household");
        setHaveHousehold(true);
      } else {
        console.log("no household");
        setHaveHousehold(false);
      }
    }
  }, []);

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
    if (userId && (loginSuccess || registerSuccess)) {
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
          setUser(data);
          localStorage.setItem("user", JSON.stringify(data));
          console.log(data);
        };
        fetchUser();
      } catch (error) {
        setIsLoading(false);
        toast.error(error);
        console.error(error);
      } finally {
        fetchHousehold();
      }
    } else {
      setIsLoading(false);
      toast.error("Oops, there has been an issue. Please try again later.");
    }
  }, [userId]);

  const fetchHousehold = async () => {
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/userhousehold/user/${userId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const data = await res.json();
      const householdsData = data.map((item) => item.household);
      setHouseholds(householdsData);
      localStorage.setItem("households", JSON.stringify(householdsData));

      if (householdsData.length > 0) {
        setHaveHousehold(true);
      }

      setIsLoading(false);

      if (loginSuccess) {
        toast.success("Login successful!");
      } else if (registerSuccess) {
        toast.success("Registration successful!");
      }

      navigate("/");
    } catch (error) {
      console.error(error);
    } finally {
      setLoginSuccess(false);
    }
  };

  const login = (username, password) => {
    console.log("logging in");
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
        console.log(decoded.sub);
      };
      fetchLogin();
    } catch (error) {
      setIsLoading(false);
      toast.error(error);
      console.error(error);
    } finally {
      setLoginSuccess(true);
    }
  };

  const register = (firstName, lastName, username, password, email, phone) => {
    setIsLoading(true);
    try {
      const fetchRegister = async () => {
        const res = await fetch("http://localhost:8888/api/v1/auth/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            username: username,
            password: password,
            email: email,
            phone: phone,
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
        console.log(decoded.sub);
      };
      fetchRegister();
    } catch (error) {
      setIsLoading(false);
      toast.error(error);
      console.error(error);
    } finally {
      setRegisterSuccess(true);
    }
  };

  const logout = () => {
    const answer = window.confirm("Are you sure you want to log out?");
    if (answer) {
      setUser(null);
      setToken(null);
      setUserId(null);
      setHouseholds([]);
      setHaveHousehold(false);
      localStorage.removeItem("user");
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      localStorage.removeItem("households");
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
                haveHousehold={haveHousehold}
                username={user.username}
              />
            }
          >
            <Route
              index
              element={
                <Dashboard
                  sidebarOpen={sidebarOpen}
                  haveHousehold={haveHousehold}
                />
              }
            />
            <Route
              path="calendar"
              element={<Calendar sidebarOpen={sidebarOpen} />}
            />
            <Route
              path="chore-list"
              element={
                <ChoreList
                  sidebarOpen={sidebarOpen}
                  haveHousehold={haveHousehold}
                />
              }
            />
          </Route>
        ) : (
          <Route path="*" element={<Navigate to="/login" replace />} />
        )}
        <Route path="login" element={<LoginWindow login={login} />} />
        <Route path="register" element={<SignupWindow register={register} />} />
      </Routes>
    </div>
  );
}

export default App;
