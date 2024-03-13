import "../styles/LoginWindow.css";
import { useState } from "react";
import { Link } from "react-router-dom";

function LoginWindow({ login }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const onLogin = (e) => {
    e.preventDefault();
    login(username, password);
  };

  return (
    <div className="auth-window">
      <h1 className="app-title">ChoreShare</h1>
      <p>Already have an account? Login here!</p>
      <form action="" onSubmit={onLogin}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Log in</button>
      </form>
      <span>
        Don't have an account? <Link to="/register">Sign up</Link> here!
      </span>
    </div>
  );
}

export default LoginWindow;
