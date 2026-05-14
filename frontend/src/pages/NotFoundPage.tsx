import { Link } from 'react-router-dom';
import { Card } from '../components/ui/Card';

export function NotFoundPage({ forbidden = false }: { forbidden?: boolean }) {
  return (
    <main className="page-container">
      <Card title={forbidden ? 'Access denied' : 'Page not found'}>
        <p className="muted">
          {forbidden ? 'Your role cannot access this page.' : 'The page you requested does not exist.'}
        </p>
        <Link className="button primary inline-link" to="/">Go to dashboard</Link>
      </Card>
    </main>
  );
}
