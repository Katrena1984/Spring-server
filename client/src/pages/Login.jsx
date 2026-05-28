import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const result = await login(email, password);
      
      if (result.success) {
        navigate('/dashboard');
      } else {
        setError(result.error || 'Ошибка входа');
      }
    } catch (err) {
      console.error('Login error:', err);
      setError(err.response?.data?.error || err.response?.data?.message || 'Ошибка входа');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="wrapper">
        <form onSubmit={handleSubmit}>
          <h1>Вход</h1>
          
          {error && <div className="error-message">{error}</div>}

          <div className="input-box">
            <input 
              type="email" 
              placeholder="Email" 
              required 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <i className='bx bx-user'></i>
          </div>

          <div className="input-box">
            <input 
              type="password" 
              placeholder="Пароль" 
              required 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <i className='bx bxs-lock-alt'></i>
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? 'Вход...' : 'Войти'}
          </button>

          <div className="register-link">
            <p>Нет аккаунта? <Link to="/register">Зарегистрироваться</Link></p>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;