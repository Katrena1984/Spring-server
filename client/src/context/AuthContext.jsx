import { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  
  useEffect(() => {
    const checkAuth = async () => {
      try {
        const response = await api.get('/user/profile');
        if (response.status === 200) {
          const userData = await response.data;
          setUser(userData);
        }
      } catch (error) {
        setUser(null); 
      } finally {
        setLoading(false);
      }
    };
    checkAuth();
  }, []);

  
  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/sign-in', { email, password });
      if (response.status === 200) {
        const profileResponse = await api.get('/user/profile');
        const userData = await profileResponse.data;
        setUser(userData);
        return { success: true };
      }
      return { success: false, error: 'Invalid credentials' };
    } catch (err) {
      return { success: false, error: err.response?.data?.error || 'Ошибка входа' };
    }
  };

  
  const register = async (email, password, username, accessCode) => {
    try {
    const url = accessCode 
      ? `/auth/registration?accessCode=${encodeURIComponent(accessCode.trim())}`
      : '/auth/registration';

    console.log('📤 Отправка на:', url);

    const response = await api.post(url, {
      email,
      password,
      username  
      
    });
    
    if (response.status === 200) {
      const data = await response.data;
      
      if (data.user) {
        setUser(data.user);
      } else {
        const profileResponse = await api.get('/user/profile');
        const userData = await profileResponse.data;
        setUser(userData);
      }
      
      return { success: true };
    }
    return { success: false, error: 'Registration failed' };
  } catch (err) {
    console.error('Register error:', err);
    return { 
      success: false, 
      error: err.response?.data || err.response?.data?.message || 'Ошибка регистрации' 
    };
    }
  };

  
  const logout = async () => {
    try {
      await api.post('/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setUser(null);
    }
  };

  
  const hasRole = (roles) => {
    if (!user) return false;
    if (Array.isArray(roles)) {
      return roles.includes(user.role);
    }
    return user.role === roles;
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, hasRole, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};