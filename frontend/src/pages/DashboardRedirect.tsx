import { Navigate } from 'react-router-dom';
import { useAppSelector } from '../app/hooks';
import { ROLE_HOME } from '../constants/workflow';

export function DashboardRedirect() {
  const user = useAppSelector((state) => state.auth.user);
  return <Navigate to={user ? ROLE_HOME[user.role] : '/login'} replace />;
}
