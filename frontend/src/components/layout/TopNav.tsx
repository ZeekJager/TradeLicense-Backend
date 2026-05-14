import { Link, useNavigate } from 'react-router-dom';
import { useAppSelector } from '../../app/hooks';
import { useLogoutMutation } from '../../services/authApi';

export function TopNav() {
  const navigate = useNavigate();
  const user = useAppSelector((state) => state.auth.user);
  const [logout, { isLoading }] = useLogoutMutation();

  async function handleLogout() {
    await logout().unwrap().catch(() => undefined);
    navigate('/login', { replace: true });
  }

  return (
    <header className="topnav">
      <div>
        <span className="eyebrow">Role</span>
        <strong>{user?.role}</strong>
      </div>
      <div className="topnav-user">
        <Link className="inline-link" to="/verify">Verify</Link>
        <span>{user?.fullName}</span>
        <button className="button ghost" type="button" onClick={handleLogout} disabled={isLoading}>
          Logout
        </button>
      </div>
    </header>
  );
}
