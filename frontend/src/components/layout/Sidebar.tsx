import { NavLink } from 'react-router-dom';
import { useAppSelector } from '../../app/hooks';
import { UserRole } from '../../types/domain';

const navItems = [
  { to: '/customer', label: 'Customer Home', roles: [UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN] },
  { to: '/customer/apply', label: 'New Application', roles: [UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN] },
  { to: '/customer/update', label: 'Update License', roles: [UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN] },
  { to: '/customer/renew', label: 'Renew License', roles: [UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN] },
  { to: '/reviewer', label: 'Review', roles: [UserRole.REVIEWER, UserRole.ADMIN] },
  { to: '/approver', label: 'Approval', roles: [UserRole.APPROVER, UserRole.ADMIN] },
  { to: '/admin', label: 'Admin', roles: [UserRole.ADMIN] }
];

export function Sidebar() {
  const user = useAppSelector((state) => state.auth.user);
  const allowedItems = navItems.filter((item) => user && item.roles.includes(user.role));

  return (
    <aside className="sidebar" aria-label="Main navigation">
      <div className="brand">
        <span className="brand-mark">TL</span>
        <div>
          <strong>Trade License</strong>
          <small>Workflow</small>
        </div>
      </div>
      <nav className="nav-list">
        {allowedItems.map((item) => (
          <NavLink key={item.to} to={item.to} className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
