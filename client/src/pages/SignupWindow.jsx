
import '../styles/LoginWindow.css'
import { useState } from 'react';
import { Link } from 'react-router-dom';

function SignupWindow() {
    const [username, setUsername] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');

    return (
        <div className="auth-window register">
            <h1 className="app-title">ChoreShare</h1>
            <p>Don't have an account yet? Sign Up here!</p>
            <form>
                <div className="row">
                    <input type="text" placeholder="First Name" value={firstName} onChange={(e) => setFirstName(e.target.value)} required/>
                    <input type="text" placeholder="Last Name" value={lastName} onChange={(e) => setLastName(e.target.value)} required/>
                </div>
                <div className="row">
                    <input type="text" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required/>
                    <input type="text" placeholder="Phone Number" value={phone} onChange={(e) => setPhone(e.target.value)} required/>
                </div>
                <div className="row">
                    <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required/>
                    <input type="text" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required/>
                </div>
                <button>Log in</button>
            </form>
            <span>Have an account already? <Link to="/login">Login</Link> here!</span>
        </div>
    );
}

export default SignupWindow;