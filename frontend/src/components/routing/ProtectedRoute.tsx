import { Navigate, useLocation } from 'react-router-dom';
import { useAppSelector } from '../../app/hooks';
import { ROLE_HOME } from '../../constants/workflow';
import { UserRole } from '../../types/domain';

type ProtectedRouteProps = {
  roles?: UserRole[];
  children: React.ReactNode;
};

export function ProtectedRoute({ roles, children }: ProtectedRouteProps) {
  const location = useLocation();
  const { token, user } = useAppSelector((state) => state.auth);

  if (!token || !user) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  if (roles && !roles.includes(user.role)) {
    return <Navigate to={ROLE_HOME[user.role] ?? '/403'} replace />;
  }

  return <>{children}</>;
}
