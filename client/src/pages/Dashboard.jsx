import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const { user, logout, hasRole } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const getRoleInfo = (role) => {
    const roles = {
      'ADMIN': { label: 'Администратор', color: 'bg-purple-100 text-purple-800' },
      'EDITOR': { label: 'Редактор', color: 'bg-blue-100 text-blue-800' },
      'VIEWER': { label: 'Просмотр', color: 'bg-gray-100 text-gray-800' }
    };
    return roles[role] || { label: role, color: 'bg-gray-100 text-gray-800' };
  };

  if (!user) {
    return <div className="dashboard">Загрузка...</div>;
  }

  const roleInfo = getRoleInfo(user.role);

  return (
    <div className="dashboard">
      <div className="dashboard-content">
        <h1>Добро пожаловать!</h1>
        
        <div className="user-info">
          <p><strong>Email:</strong> {user.email}</p>
          <p><strong>Имя:</strong> {user.username}</p>
          <p>
            <strong>Роль:</strong>{' '}
            <span className={`px-3 py-1 rounded text-sm ${roleInfo.color}`}>
              {roleInfo.label}
            </span>
          </p>
        </div>

        <div className="role-content">
          {hasRole('ADMIN') && (
            <div className="admin-panel" style={{ background: 'linear-gradient(135deg, #fee2e2 0%, #fecaca 100%)', borderLeft: '5px solid #dc2626', padding: '20px', borderRadius: '10px', marginBottom: '20px' }}>
              <h2>Панель администратора</h2>
              <p>Вам доступен полный функционал</p>
            </div>
          )}

          {hasRole('EDITOR') && (
            <div className="editor-panel" style={{ background: 'linear-gradient(135deg, #fef3c7 0%, #fde68a 100%)', borderLeft: '5px solid #d97706', padding: '20px', borderRadius: '10px', marginBottom: '20px' }}>
              <h2>Панель редактора</h2>
              <p>Вы можете редактировать контент</p>
            </div>
          )}

          {hasRole('VIEWER') && (
            <div className="viewer-panel" style={{ background: 'linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%)', borderLeft: '5px solid #059669', padding: '20px', borderRadius: '10px', marginBottom: '20px' }}>
              <h2>Режим просмотра</h2>
              <p>У вас права только на просмотр</p>
            </div>
          )}
        </div>

        <button onClick={handleLogout} className="logout-btn">
          Выйти
        </button>
      </div>
    </div>
  );
};

export default Dashboard;