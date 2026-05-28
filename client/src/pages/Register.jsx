import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    accessCode: '' 
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const result = await register(
        formData.email, 
        formData.password, 
        formData.username,
        formData.accessCode 
      );
      
      if (result.success) {
        navigate('/dashboard');
      } else {
        setError(result.error || 'Ошибка регистрации');
      }
    } catch (err) {
      console.error('Register error:', err);
      setError(err.response?.data || err.message || 'Ошибка регистрации');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="wrapper">
        <form onSubmit={handleSubmit}>
          <h1>Регистрация</h1>
          
          {error && <div className="error-message">{error}</div>}

          <div className="input-box">
            <input 
              type="text" 
              name="username"
              placeholder="Имя" 
              required 
              value={formData.username}
              onChange={handleChange}
            />
            <i className='bx bx-user'></i>
          </div>

          <div className="input-box">
            <input 
              type="email" 
              name="email"
              placeholder="Email" 
              required 
              value={formData.email}
              onChange={handleChange}
            />
            <i className='bx bx-user'></i>
          </div>

          <div className="input-box">
            <input 
              type="password" 
              name="password"
              placeholder="Пароль" 
              required 
              value={formData.password}
              onChange={handleChange}
            />
            <i className='bx bxs-lock-alt'></i>
          </div>

          <div className="input-box">
            <input 
              type="password" 
              name="accessCode"
              placeholder="Код доступа (выдаётся администратором)" 
              required 
              value={formData.accessCode}
              onChange={handleChange}
            />
            <i className='bx bx-key'></i>
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? 'Регистрация...' : 'Зарегистрироваться'}
          </button>

          <div className="register-link">
            <p>Уже есть аккаунт? <Link to="/login">Войти</Link></p>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;