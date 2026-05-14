import { Outlet } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { TopNav } from './TopNav';

export function AppLayout() {
  return (
    <div className="app-shell">
      <Sidebar />
      <div className="app-main">
        <TopNav />
        <main className="page-container">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
