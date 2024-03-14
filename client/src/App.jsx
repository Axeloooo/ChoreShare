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
  const [households, setHouseholds] = useState(
    localStorage.getItem("households")
      ? JSON.parse(localStorage.getItem("households"))
      : []
  );
  const [currentHousehold, setCurrentHousehold] = useState(
    localStorage.getItem("currentHousehold")
      ? JSON.parse(localStorage.getItem("currentHousehold"))
      : null
  );
  const [isLoading, setIsLoading] = useState(false);
  const [loginSuccess, setLoginSuccess] = useState(false);
  const [registerSuccess, setRegisterSuccess] = useState(false);
  const [allChores, setAllChores] = useState(
    localStorage.getItem(
      "allChores" ? JSON.parse(localStorage.getItem("allChores")) : []
    )
  );
  const navigate = useNavigate();

  localStorage.removeItem("user");
  localStorage.removeItem("token");
  localStorage.removeItem("userId");
  localStorage.removeItem("households");
  console.log(userId);
  console.log(households);
  console.log(currentHousehold);
  console.log(allChores);

  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    const storedUserId = localStorage.getItem("userId");

    if (storedToken && storedUserId) {
      setToken(storedToken);
      setUserId(JSON.parse(storedUserId));
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
        setCurrentHousehold(householdsData[0]);
      }

      localStorage.setItem(
        "currentHousehold",
        JSON.stringify(currentHousehold)
      );

      console.log("householdsData", householdsData);

      fetchChores(currentHousehold.id);
    } catch (error) {
      console.error(error);
    } finally {
    }
  };

  const createHousehold = async (householdName, closeOverlay) => {
    try {
      const res = await fetch("http://localhost:8888/api/v1/userhousehold", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          userId: userId,
          name: householdName,
        }),
      });

      if (!res.ok) {
        const response = await res.json();
        toast.error(response.message);
        return;
      }

      const data = await res.json();
      const householdData = data.household;
      setHouseholds([...households, householdData]);
      localStorage.setItem(
        "households",
        JSON.stringify([...households, householdData])
      );
      setCurrentHousehold(householdData);
      localStorage.setItem(
        "currentHousehold",
        JSON.stringify(currentHousehold)
      );
      toast.success("Household created successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const fetchChores = async (householdId) => {
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/task/household/${householdId}`,
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
        toast.error(response.message);
        return;
      }

      const data = await res.json();
      setAllChores(data);
      localStorage.setItem("allChores", JSON.stringify(data));

      setIsLoading(false);

      if (loginSuccess) {
        toast.success("Login successful!");
      } else if (registerSuccess) {
        toast.success("Registration successful!");
      }

      navigate("/");
    } catch (error) {
      console.error(error);
    }
  };

  const createChore = async (
    title,
    description,
    frequence,
    tag,
    closeOverlay
  ) => {
    try {
      console.log(
        JSON.stringify({
          title: title,
          householdId: currentHousehold.id,
          description: description,
          status: null,
          frequency: frequence,
          tag: tag,
          userId: null,
        })
      );
      const res = await fetch("http://localhost:8888/api/v1/task", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          title: title,
          householdId: currentHousehold.id,
          description: description,
          status: null,
          frequency: frequence,
          tag: tag,
          userId: null,
        }),
      });

      if (!res.ok) {
        const response = await res.json();
        toast.error(response.message);
        return;
      }

      const data = await res.json();
      setAllChores([...allChores, data]);
      localStorage.setItem("allChores", JSON.stringify([...allChores, data]));
      toast.success("Chore created successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const login = async (username, password) => {
    setIsLoading(true);
    try {
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
    } catch (error) {
      setIsLoading(false);
      toast.error(error);
      console.error(error);
    } finally {
      setLoginSuccess(true);
    }
  };

  const register = async (
    firstName,
    lastName,
    username,
    password,
    email,
    phone
  ) => {
    setIsLoading(true);
    try {
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
    } catch (error) {
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
      setCurrentHousehold(null);
      setAllChores([]);
      localStorage.removeItem("user");
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      localStorage.removeItem("households");
      localStorage.removeItem("currentHousehold");
      localStorage.removeItem("allChores");
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
                currentHousehold={currentHousehold}
                setCurrentHousehold={setCurrentHousehold}
                username={user.username}
                createHousehold={createHousehold}
                households={households}
              />
            }
          >
            <Route
              index
              element={
                <Dashboard
                  sidebarOpen={sidebarOpen}
                  currentHousehold={currentHousehold}
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
                  currentHousehold={currentHousehold}
                  allChores={allChores}
                  createChore={createChore}
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
