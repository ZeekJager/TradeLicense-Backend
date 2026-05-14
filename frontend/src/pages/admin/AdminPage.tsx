import { FormEvent, useMemo, useState } from 'react';
import { useAppSelector } from '../../app/hooks';
import { Card } from '../../components/ui/Card';
import { REQUIRED_TRADE_LICENSE_DOCUMENTS } from '../../constants/licenseTypes';
import { EmptyState, LoadingState } from '../../components/ui/StateViews';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { useToast } from '../../components/ui/ToastProvider';
import { API_BASE_URL } from '../../services/api';
import {
  applicationReportUrl,
  useAdminApplicationsQuery,
  useAdminLicenseTypesQuery,
  useCreateLicenseTypeMutation,
  useCreateUserMutation,
  useStatusSummaryQuery,
  useUpdateLicenseTypeMutation,
  useUpdateUserRoleMutation,
  useUsersQuery
} from '../../services/adminApi';
import { ApplicationStatus, UserRole } from '../../types/domain';

export function AdminPage() {
  const token = useAppSelector((state) => state.auth.token);
  const { notify } = useToast();
  const [statusFilter, setStatusFilter] = useState<ApplicationStatus | ''>('');
  const { data: users = [], isLoading: usersLoading } = useUsersQuery();
  const { data: applications = [] } = useAdminApplicationsQuery(statusFilter || undefined);
  const { data: statusSummary = [] } = useStatusSummaryQuery();
  const { data: licenseTypes = [] } = useAdminLicenseTypesQuery();
  const [createUser] = useCreateUserMutation();
  const [updateUserRole] = useUpdateUserRoleMutation();
  const [createLicenseType] = useCreateLicenseTypeMutation();
  const [updateLicenseType] = useUpdateLicenseTypeMutation();
  const [userForm, setUserForm] = useState({ fullName: '', email: '', password: 'password', role: UserRole.CUSTOMER });
  const [licenseForm, setLicenseForm] = useState({ code: '', name: '', requiredDocuments: REQUIRED_TRADE_LICENSE_DOCUMENTS.join(', ') });

  const filteredApplications = useMemo(() => applications, [applications]);

  async function handleCreateUser(event: FormEvent) {
    event.preventDefault();
    try {
      await createUser(userForm).unwrap();
      setUserForm({ fullName: '', email: '', password: 'password', role: UserRole.CUSTOMER });
      notify('User created', 'success');
    } catch {
      notify('Could not create user', 'error');
    }
  }

  async function handleCreateLicenseType(event: FormEvent) {
    event.preventDefault();
    try {
      await createLicenseType({
        code: licenseForm.code,
        name: licenseForm.name,
        requiredDocuments: licenseForm.requiredDocuments.split(',').map((value) => value.trim()).filter(Boolean)
      }).unwrap();
      setLicenseForm({ code: '', name: '', requiredDocuments: '' });
      notify('License type saved', 'success');
    } catch {
      notify('Could not save license type', 'error');
    }
  }

  async function downloadReport() {
    const response = await fetch(applicationReportUrl(), {
      headers: token ? { Authorization: `Bearer ${token}` } : undefined
    });
    if (!response.ok) {
      notify('Report download failed', 'error');
      return;
    }
    const blob = await response.blob();
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = 'trade-license-applications.csv';
    anchor.click();
    URL.revokeObjectURL(url);
  }

  if (usersLoading) {
    return <LoadingState />;
  }

  return (
    <div className="page-stack">
      <div className="page-title">
        <div>
          <span className="eyebrow">Administration</span>
          <h1>Manage Users, Roles, and Settings</h1>
        </div>
        <button className="button primary" type="button" onClick={downloadReport}>Export report</button>
      </div>

      <div className="metric-grid">
        {statusSummary.map((item) => (
          <Card key={item.status}>
            <span className="metric-value">{item.count}</span>
            <StatusBadge status={item.status} />
          </Card>
        ))}
      </div>

      <div className="content-grid">
        <Card title="Users and roles">
          <form className="form-grid three" onSubmit={handleCreateUser}>
            <input placeholder="Full name" value={userForm.fullName} onChange={(event) => setUserForm({ ...userForm, fullName: event.target.value })} />
            <input placeholder="Email" type="email" value={userForm.email} onChange={(event) => setUserForm({ ...userForm, email: event.target.value })} />
            <input placeholder="Password" value={userForm.password} onChange={(event) => setUserForm({ ...userForm, password: event.target.value })} />
            <select value={userForm.role} onChange={(event) => setUserForm({ ...userForm, role: event.target.value as UserRole })}>
              {Object.values(UserRole).map((role) => <option key={role} value={role}>{role}</option>)}
            </select>
            <button className="button primary" type="submit">Create user</button>
          </form>
          <div className="table-wrap">
            <table>
              <thead>
                <tr><th>Name</th><th>Email</th><th>Role</th></tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id}>
                    <td>{user.fullName}</td>
                    <td>{user.email}</td>
                    <td>
                      <select
                        value={user.role}
                        onChange={(event) => updateUserRole({ userId: user.id, role: event.target.value as UserRole })}
                      >
                        {Object.values(UserRole).map((role) => <option key={role} value={role}>{role}</option>)}
                      </select>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>

        <Card title="License type requirements">
          <form className="form-grid" onSubmit={handleCreateLicenseType}>
            <input placeholder="Code" value={licenseForm.code} onChange={(event) => setLicenseForm({ ...licenseForm, code: event.target.value })} />
            <input placeholder="Name" value={licenseForm.name} onChange={(event) => setLicenseForm({ ...licenseForm, name: event.target.value })} />
            <input placeholder="Required documents, comma separated" value={licenseForm.requiredDocuments} onChange={(event) => setLicenseForm({ ...licenseForm, requiredDocuments: event.target.value })} />
            <button className="button primary" type="submit">Add license type</button>
          </form>
          <div className="record-list compact">
            {licenseTypes.map((licenseType) => (
              <div className="record static" key={licenseType.id}>
                <strong>{licenseType.name}</strong>
                <span>{licenseType.requiredDocuments.join(', ') || 'No required documents'}</span>
                <button
                  className="button ghost"
                  type="button"
                  onClick={() => updateLicenseType({
                    id: licenseType.id,
                    name: licenseType.name,
                    requiredDocuments: licenseType.requiredDocuments,
                    active: !licenseType.active
                  })}
                >
                  {licenseType.active ? 'Disable' : 'Enable'}
                </button>
              </div>
            ))}
          </div>
        </Card>
      </div>

      <Card
        title="Application monitor"
        actions={
          <select value={statusFilter} onChange={(event) => setStatusFilter(event.target.value as ApplicationStatus | '')}>
            <option value="">All statuses</option>
            {Object.values(ApplicationStatus).map((status) => <option key={status} value={status}>{status}</option>)}
          </select>
        }
      >
        {filteredApplications.length === 0 ? <EmptyState label="No applications match the current filter." /> : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr><th>Applicant</th><th>License type</th><th>Commodity</th><th>Status</th></tr>
              </thead>
              <tbody>
                {filteredApplications.map((application) => (
                  <tr key={application.applicationId}>
                    <td>{application.fullName}</td>
                    <td>{application.tradeLicenseType}</td>
                    <td>{application.commodity}</td>
                    <td><StatusBadge status={application.status} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Card>

      <Card title="System settings">
        <div className="settings-grid">
          <div>
            <span className="eyebrow">API</span>
            <strong>{API_BASE_URL}</strong>
          </div>
          <div>
            <span className="eyebrow">Auth</span>
            <strong>Bearer session token</strong>
          </div>
          <div>
            <span className="eyebrow">Reports</span>
            <strong>CSV export enabled</strong>
          </div>
        </div>
      </Card>
    </div>
  );
}
