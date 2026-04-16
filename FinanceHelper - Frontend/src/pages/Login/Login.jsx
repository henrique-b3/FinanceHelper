import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import api from '../../services/api';
import './Login.css';
import { images } from "../../svg";

function Login(){
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [erro, setErro] = useState('');
    
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setErro('');

        try{
            const resposta = await api.post('/auth/login', {
                email: email,
                password: password
            });

            const token = resposta.data.token;
            console.log("Authenticated: " + token);

            localStorage.setItem('token', token);
            navigate('/dashboard');
        }catch(error){
            setErro("Error");
            console.error(error);
        }
    };

    return (
        <div className="login-container">
            <img className='logo' src={images.logo} alt={images.logo} />
            <div className="login-card">
                <h2>Login</h2>
                {erro && <div className="login-error">{erro}</div>}

                <form onSubmit={handleLogin} className="login-form">
                    <div className="input-group">
                        <label>Email</label>
                        <input 
                            type="email" 
                            placeholder="O seu Email" 
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    
                    <div className="input-group">
                        <label>Password</label>
                        <input 
                            type="password" 
                            placeholder="A sua Password" 
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    
                    <button type="submit" className="login-button">Entrar</button>
                </form>
            </div>
        </div>
    );
}

export default Login;