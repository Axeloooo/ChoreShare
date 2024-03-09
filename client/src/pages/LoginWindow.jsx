import '../styles/LoginWindow.css'
import { useState } from 'react';
import { Link } from 'react-router-dom';

function LoginWindow(){
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    return(
        <div className="auth-window">
            <h1 className="app-title">ChoreShare</h1>
            <p>Already have an account? Login here!</p>
            <form>
                <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required/>
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e)} required/>
                <button>Log in</button>
            </form>
            <span>Don't have an account? <Link to="/register">Sign up</Link> here!</span>
        </div>
    )
}

export default LoginWindow;