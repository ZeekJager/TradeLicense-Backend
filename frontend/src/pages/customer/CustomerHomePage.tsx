import { Link } from 'react-router-dom';
import { useAppSelector } from '../../app/hooks';
import { Card } from '../../components/ui/Card';
import { EmptyState, LoadingState } from '../../components/ui/StateViews';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { useListApplicationsQuery } from '../../services/applicationsApi';
import { useMyTradeLicensesQuery } from '../../services/licensesApi';

const actions = [
  { to: '/customer/apply', title: 'Create New Application', text: 'Start a new trade license application and upload required documents.', tone: 'primary' },
  { to: '/customer/update', title: 'Update Trade License', text: 'Edit allowed details for an issued license.', tone: 'accent' },
  { to: '/customer/cancel', title: 'Cancel Trade License', text: 'Cancel an active license you no longer need.', tone: 'danger' },
  { to: '/customer/renew', title: 'Renew Trade License', text: 'Extend the validity period of an active license.', tone: 'warning' }
];

export function CustomerHomePage() {
  const user = useAppSelector((state) => state.auth.user);
  const { data: applications = [], isLoading: loadingApplications } = useListApplicationsQuery();
  const { data: licenses = [], isLoading: loadingLicenses } = useMyTradeLicensesQuery();

  if (loadingApplications || loadingLicenses) {
    return <LoadingState />;
  }

  return (
    <div className="page-stack">
      <section className="dashboard-hero">
        <div>
          <span className="eyebrow">Customer workspace</span>
          <h1>Welcome, {user?.fullName}</h1>
          <p>Apply, manage issued licenses, and track workflow activity from one place.</p>
        </div>
        <Link className="button primary" to="/customer/apply">New Application</Link>
      </section>

      <div className="action-card-grid">
        {actions.map((action) => (
          <Link key={action.to} className={`action-card ${action.tone}`} to={action.to}>
            <span>{action.title}</span>
            <p>{action.text}</p>
          </Link>
        ))}
      </div>

      <div className="content-grid">
        <Card title="Recent applications">
          {applications.length === 0 ? (
            <EmptyState label="No applications have been created yet." />
          ) : (
            <div className="record-list">
              {applications.slice(0, 4).map((application) => (
                <div className="record static" key={application.applicationId}>
                  <strong>{application.tradeName}</strong>
                  <span>{application.tradeLicenseType}</span>
                  <span>{application.commodity}</span>
                  <StatusBadge status={application.status} />
                </div>
              ))}
            </div>
          )}
        </Card>

        <Card title="Issued licenses">
          {licenses.length === 0 ? (
            <EmptyState label="No issued trade license is available yet." />
          ) : (
            <div className="record-list">
              {licenses.slice(0, 4).map((license) => (
                <div className="record static" key={license.licenseId}>
                  <strong>{license.licenseNumber}</strong>
                  <span>{license.tradeName}</span>
                  <span>{license.tradeLicenseType} - {license.commodity}</span>
                  <span>{license.status}</span>
                </div>
              ))}
            </div>
          )}
        </Card>
      </div>
    </div>
  );
}
