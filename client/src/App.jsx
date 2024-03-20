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
    localStorage.getItem("user") && localStorage.getItem("user") !== "undefined"
      ? JSON.parse(localStorage.getItem("user"))
      : null
  );
  const [token, setToken] = useState(null);
  const [userId, setUserId] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [loginSuccess, setLoginSuccess] = useState(false);
  const [registerSuccess, setRegisterSuccess] = useState(false);
  const [data, setData] = useState(
    localStorage.getItem("data") && localStorage.getItem("data") !== "undefined"
      ? JSON.parse(localStorage.getItem("data"))
      : null
  );
  const navigate = useNavigate();

  console.log(data);
  console.log(user);
  console.log(userId);
  console.log(token);

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      console.log("storedUserId", storedUserId);
      setUserId(JSON.parse(storedUserId));
    }

    const storedToken = localStorage.getItem("token");
    if (storedToken) {
      console.log("storedToken", storedToken);
      setToken(storedToken);
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
    const fetchData = async () => {
      console.log("fetching data");
      if (userId && token && (loginSuccess || registerSuccess)) {
        try {
          await fetchUser(userId, token);
          await fetchHousehold();

          if (loginSuccess) {
            setIsLoading(false);
            toast.success("Login Successful!");
            setLoginSuccess(false);
          } else if (registerSuccess) {
            setIsLoading(false);
            toast.success("Registration Successful!");
            setRegisterSuccess(false);
          }
          navigate("/");
        } catch (error) {
          console.error(error);
        }
      }
    };
    fetchData();
  }, [userId, token]);

  const fetchUser = async (userId, token) => {
    if (!userId || !token || token === "null" || userId === "null") {
      return;
    }
    try {
      const res = await fetch(`http://localhost:8888/api/v1/user/${userId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const data = await res.json();
      setUser(data);
      localStorage.setItem("user", JSON.stringify(data));

      fetchHousehold(userId);
    } catch (error) {
      console.error(error);
    }
  };

  const editUser = async (firstName, lastName, email, phone, closeOverlay) => {
    setIsLoading(true);
    try {
      const res = await fetch(`http://localhost:8888/api/v1/user/${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          firstName: firstName,
          lastName: lastName,
          email: email,
          phone: phone,
        }),
      });

      if (!res.ok) {
        const response = await res.json();
        setIsLoading(false);
        toast.error(response.message);
        return;
      }

      const updatedUser = await res.json();
      setUser(updatedUser);
      localStorage.setItem("user", JSON.stringify(updatedUser));

      setIsLoading(false);
      closeOverlay();
      toast.success("User updated successfully!");
    } catch (error) {
      console.log(error);
    }
  };

  const joinHousehold = async (householdId, closeOverlay) => {
    setIsLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/userhousehold/join/${householdId}/user/${userId}`,
        {
          method: "POST",
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

      const newHousehold = await res.json();
      const fetchSuccess = await fetchHouseholdById(newHousehold.id);

      console.log("fetchSuccess", fetchSuccess);

      if (fetchSuccess) {
        setIsLoading(false);
        toast.success("You've successfully joined a household!");
        setData((prevData) => {
          const updatedData = {
            ...prevData,
            households: [...prevData.households, newHousehold],
            currentHousehold: newHousehold,
          };

          localStorage.setItem("data", JSON.stringify(updatedData));

          return updatedData;
        });

        closeOverlay();
      } else {
        setIsLoading(false);
        toast.error("Oops, something went wrong. Please try again.");
      }
    } catch (error) {
      console.error(error);
    }
  };

  const changeHousehold = async (household) => {
    setIsLoading(true);
    try {
      const fetchSuccess = await fetchHouseholdById(household.id);
      if (fetchSuccess) {
        setIsLoading(false);
        setData((prev) => ({ ...prev, currentHousehold: household }));
        toast.success("Household changed successfully!");
      } else {
        setIsLoading(false);
        toast.error("Oops, something went wrong. Please try again.");
      }
    } catch (error) {
      console.error(error);
    }
  };

  const fetchHouseholdById = async (householdId) => {
    try {
      const householdRes = await fetch(
        `http://localhost:8888/api/v1/household/${householdId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!householdRes.ok) {
        const response = await householdRes.json();
        console.log(response);
        return;
      }

      const householdData = await householdRes.json();

      const choresRes = await fetch(
        `http://localhost:8888/api/v1/task/household/${householdData.id}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!choresRes.ok) {
        const response = await choresRes.json();
        console.log(response);
        return;
      }

      const allChoresData = await choresRes.json();

      const myChoresData = allChoresData.filter(
        (task) => task.userId === userId
      );

      const membersRes = await fetch(
        `http://localhost:8888/api/v1/userhousehold/household/${householdData.id}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!membersRes.ok) {
        const response = await membersRes.json();
        console.log(response);
        return;
      }

      const membersData = await membersRes.json();

      const announcementsRes = await fetch(
        `http://localhost:8888/api/v1/announcement/household/${householdData.id}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!announcementsRes.ok) {
        const response = await announcementsRes.json();
        console.log(response);
        return;
      }

      const announcementsData = await announcementsRes.json();

      const eventsRes = await fetch(
        `http://localhost:8888/api/v1/event/household/${householdData.id}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!eventsRes.ok) {
        const response = await eventsRes.json();
        console.log(response);
        return;
      }

      const eventsData = await eventsRes.json();

      const fetchedData = {
        households: data.households,
        currentHousehold: householdData,
        allChores: allChoresData,
        myChores: myChoresData,
        members: membersData,
        announcements: announcementsData,
        events: eventsData,
      };
      setData(fetchedData);

      localStorage.setItem("data", JSON.stringify(fetchedData));
      return true;
    } catch (error) {
      console.error(error);
      return false;
    }
  };

  const fetchHousehold = async () => {
    console.log("fetching household");
    try {
      const householdRes = await fetch(
        `http://localhost:8888/api/v1/userhousehold/user/${userId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!householdRes.ok) {
        const response = await householdRes.json();
        console.log(response);
        return;
      }

      const householdsData = (await householdRes.json()).map(
        (item) => item.household
      );

      if (householdsData.length > 0) {
        console.log("householdsData has length");
        const choresRes = await fetch(
          `http://localhost:8888/api/v1/task/household/${householdsData[0].id}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!choresRes.ok) {
          const response = await choresRes.json();
          console.log(response);
          return;
        }

        const allChoresData = await choresRes.json();

        const myChoresData = allChoresData.filter(
          (task) => task.userId === userId
        );

        const membersRes = await fetch(
          `http://localhost:8888/api/v1/userhousehold/household/${householdsData[0].id}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!membersRes.ok) {
          const response = await membersRes.json();
          console.log(response);
          return;
        }

        const membersData = await membersRes.json();

        const announcementsRes = await fetch(
          `http://localhost:8888/api/v1/announcement/household/${householdsData[0].id}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!announcementsRes.ok) {
          const response = await announcementsRes.json();
          console.log(response);
          return;
        }

        const announcementsData = await announcementsRes.json();

        const eventsRes = await fetch(
          `http://localhost:8888/api/v1/event/household/${householdsData[0].id}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!eventsRes.ok) {
          const response = await eventsRes.json();
          console.log(response);
          return;
        }

        const eventsData = await eventsRes.json();

        const fetchedData = {
          households: householdsData,
          currentHousehold: householdsData[0],
          allChores: allChoresData,
          myChores: myChoresData,
          members: membersData,
          announcements: announcementsData,
          events: eventsData,
        };
        setData(fetchedData);

        localStorage.setItem("data", JSON.stringify(fetchedData));
      } else {
        console.log("householdsData has no length");
        const fetchedData = {
          households: [],
          currentHousehold: null,
          allChores: [],
          myChores: [],
          members: [],
          announcements: [],
          events: [],
        };
        setData(fetchedData);

        localStorage.setItem("data", JSON.stringify(fetchedData));
      }
    } catch (error) {
      console.error(error);
    }
  };

  const createHousehold = async (householdName, closeOverlay) => {
    setIsLoading(true);
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
        console.log(response);
        return;
      }

      const fetchData = await res.json();
      const newHouseholdData = fetchData.household;

      setData((prevData) => {
        const updatedData = {
          ...prevData,
          households: [...prevData.households, newHouseholdData],
          currentHousehold: newHouseholdData,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Household created successfully!");
      closeOverlay();
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
    setIsLoading(true);
    try {
      const res = await fetch("http://localhost:8888/api/v1/task", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          title: title,
          householdId: data.currentHousehold.id,
          description: description,
          status: null,
          frequency: frequence,
          tag: tag,
          userId: null,
        }),
      });

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const fetchData = await res.json();

      setData((prevData) => {
        const updatedData = {
          ...prevData,
          allChores: [...prevData.allChores, fetchData],
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Chore created successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const assignChore = async (choreId) => {
    console.log("choreId", choreId);
    console.log("UserID", userId);
    setIsLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/task/${choreId}/assignee/${userId}`,
        {
          method: "PUT",
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

      const updatedChore = await res.json();

      setData((prevData) => {
        const updatedAllChores = prevData.allChores.map((chore) =>
          chore.id === choreId ? updatedChore : chore
        );

        const updatedMyChores = [...prevData.myChores, updatedChore];

        const updatedData = {
          ...prevData,
          allChores: updatedAllChores,
          myChores: updatedMyChores,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Chore assigned successfully!");
    } catch (error) {
      console.error(error);
    }
  };

  const unassignChore = async (choreId) => {
    setIsLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/task/${choreId}/unassign/${userId}`,
        {
          method: "PUT",
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

      const updatedChore = await res.json();

      setData((prevData) => {
        const updatedAllChores = prevData.allChores.map((chore) =>
          chore.id === choreId ? updatedChore : chore
        );

        const updatedMyChores = prevData.myChores.filter(
          (chore) => chore.id !== choreId
        );

        const updatedData = {
          ...prevData,
          allChores: updatedAllChores,
          myChores: updatedMyChores,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Chore unassigned successfully!");
    } catch (error) {
      console.error(error);
    }
  };

  const deleteChore = async (choreId) => {
    setIsLoading(true);
    try {
      const res = await fetch(`http://localhost:8888/api/v1/task/${choreId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      setData((prevData) => {
        const updatedAllChores = prevData.allChores.filter(
          (chore) => chore.id !== choreId
        );

        const updatedMyChores = prevData.myChores.filter(
          (chore) => chore.id !== choreId
        );

        const updatedData = {
          ...prevData,
          allChores: updatedAllChores,
          myChores: updatedMyChores,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Chore deleted successfully!");
    } catch (error) {
      console.error(error);
    }
  };

  const editChore = async (
    choreId,
    title,
    description,
    frequency,
    tag,
    closeOverlay
  ) => {
    setIsLoading(true);
    try {
      const res = await fetch(`http://localhost:8888/api/v1/task/${choreId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          title: title,
          description: description,
          frequency: frequency,
          tag: tag,
        }),
      });

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const updatedChore = await res.json();

      setData((prevData) => {
        const updatedAllChores = prevData.allChores.map((chore) =>
          chore.id === choreId ? updatedChore : chore
        );

        const updatedMyChores = prevData.myChores.map((chore) =>
          chore.id === choreId ? updatedChore : chore
        );

        const updatedData = {
          ...prevData,
          allChores: updatedAllChores,
          myChores: updatedMyChores,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Chore updated successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const editChoreStatus = async (choreId, status, closeOverlay) => {
    setIsLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/task/${choreId}/status`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            status: status,
            userId: userId,
          }),
        }
      );

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const updatedChore = await res.json();

      setData((prevData) => {
        const updatedAllChores = prevData.allChores.map((chore) =>
          chore.id === choreId ? updatedChore : chore
        );

        const updatedMyChores = prevData.myChores.map((chore) =>
          chore.id === choreId ? updatedChore : chore
        );

        const updatedData = {
          ...prevData,
          allChores: updatedAllChores,
          myChores: updatedMyChores,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Chore status updated successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const createAnnouncement = async (message, author, closeOverlay) => {
    setIsLoading(true);
    try {
      const res = await fetch("http://localhost:8888/api/v1/announcement", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          message: message,
          householdId: data.currentHousehold.id,
          author: author,
          userId: userId,
        }),
      });

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const newAnnouncement = await res.json();

      setData((prevData) => {
        const updatedAnnouncements = [
          ...prevData.announcements,
          newAnnouncement,
        ];

        const updatedData = {
          ...prevData,
          announcements: updatedAnnouncements,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Announcement created successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const editAnnouncement = async (
    announcementId,
    message,
    author,
    closeOverlay
  ) => {
    console.log("author", author);
    setIsLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/announcement/${announcementId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            message: message,
            householdId: data.currentHousehold.id,
            author: author,
            userId: userId,
          }),
        }
      );

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const updatedAnnouncement = await res.json();

      setData((prevData) => {
        const updatedAnnouncements = prevData.announcements.map(
          (announcement) =>
            announcement.id === announcementId
              ? updatedAnnouncement
              : announcement
        );

        const updatedData = {
          ...prevData,
          announcements: updatedAnnouncements,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Chore status updated successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const deleteAnnouncement = async (announcementId) => {
    setIsLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8888/api/v1/announcement/${announcementId}`,
        {
          method: "DELETE",
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

      setData((prevData) => {
        const updatedAnnouncements = prevData.announcements.filter(
          (announcement) => announcement.id !== announcementId
        );

        const updatedData = {
          ...prevData,
          announcements: updatedAnnouncements,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Announcement deleted successfully!");
    } catch (error) {
      console.error(error);
    }
  };

  const createEvent = async (title, startTime, endTime, closeOverlay) => {
    setIsLoading(true);
    console.log("startTime", startTime);
    console.log("endTime", endTime);
    try {
      const res = await fetch("http://localhost:8888/api/v1/event", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          householdId: data.currentHousehold.id,
          title: title,
          userId: userId,
          username: user.username,
          startTime: startTime,
          endTime: endTime,
        }),
      });

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      const newEvent = await res.json();

      setData((prevData) => {
        const updatedEvents = [...prevData.events, newEvent];

        const updatedData = {
          ...prevData,
          events: updatedEvents,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Event created successfully!");
      closeOverlay();
    } catch (error) {
      console.error(error);
    }
  };

  const deleteEvent = async (eventId) => {
    setIsLoading(true);
    try {
      const res = await fetch(`http://localhost:8888/api/v1/event/${eventId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        const response = await res.json();
        console.log(response);
        return;
      }

      setData((prevData) => {
        const updatedEvents = prevData.events.filter(
          (event) => event.id !== eventId
        );

        const updatedData = {
          ...prevData,
          events: updatedEvents,
        };

        localStorage.setItem("data", JSON.stringify(updatedData));

        return updatedData;
      });

      setIsLoading(false);
      toast.success("Event deleted successfully!");
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
        console.log(response);
        return;
      }

      const token = await res.text();
      localStorage.setItem("token", token);
      setToken(token);

      const decoded = jwtDecode(token);
      localStorage.setItem("userId", JSON.stringify(decoded.sub));
      setUserId(decoded.sub);

      setLoginSuccess(true);
      fetchUser(decoded.sub);
    } catch (error) {
      setIsLoading(false);
      toast.error(error);
      console.error(error);
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
        console.log(response);
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
      setUserId(null);
      setToken(null);
      setData(null);

      localStorage.removeItem("user");
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      localStorage.removeItem("data", null);

      navigate("/login");
    }
  };

  const inviteMember = async (email) => {
    try {
      const res = await fetch("http://localhost:8888/api/v1/email/send", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          to: email,
          householdId: data.currentHousehold.id,
        }),
      });

      if (!res.ok) {
        if (res.status === 405) {
          toast.success("Email sent successfully!");
        } else {
          toast.error("Oops! Something went wrong. Please try again.");
        }
        return;
      }
    } catch (error) {
      console.error(error);
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
                username={user.username}
                createHousehold={createHousehold}
                data={data}
                inviteMember={inviteMember}
                joinHousehold={joinHousehold}
                changeHousehold={changeHousehold}
                editUser={editUser}
              />
            }
          >
            <Route
              index
              element={
                <Dashboard
                  sidebarOpen={sidebarOpen}
                  data={data}
                  unassignChore={unassignChore}
                  editChoreStatus={editChoreStatus}
                  userId={userId}
                  user={user}
                  createAnnouncement={createAnnouncement}
                  deleteAnnouncement={deleteAnnouncement}
                  editAnnouncement={editAnnouncement}
                  deleteEvent={deleteEvent}
                />
              }
            />
            <Route
              path="calendar"
              element={
                <Calendar
                  sidebarOpen={sidebarOpen}
                  createEvent={createEvent}
                  data={data}
                />
              }
            />
            <Route
              path="chore-list"
              element={
                <ChoreList
                  sidebarOpen={sidebarOpen}
                  data={data}
                  createChore={createChore}
                  assignChore={assignChore}
                  deleteChore={deleteChore}
                  editChore={editChore}
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
