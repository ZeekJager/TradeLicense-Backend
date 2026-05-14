import { FormEvent, useState } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import { useAppSelector } from '../app/hooks';
import { ROLE_HOME } from '../constants/workflow';
import { useLoginMutation } from '../services/authApi';
import { Card } from '../components/ui/Card';
import { FormField } from '../components/ui/FormField';
import { useToast } from '../components/ui/ToastProvider';

const demoAccounts = [
  'admin@tradelicense.test',
  'customer@tradelicense.test',
  'reviewer@tradelicense.test',
  'approver@tradelicense.test'
];

export function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { notify } = useToast();
  const { token, user } = useAppSelector((state) => state.auth);
  const [login, { isLoading }] = useLoginMutation();
  const [email, setEmail] = useState('customer@tradelicense.test');
  const [password, setPassword] = useState('password');

  if (token && user) {
    return <Navigate to={ROLE_HOME[user.role]} replace />;
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    try {
      const response = await login({ email, password }).unwrap();
      notify(`Signed in as ${response.user.role}`, 'success');
      const redirectTo = (location.state as { from?: Location } | null)?.from?.pathname ?? ROLE_HOME[response.user.role];
      navigate(redirectTo, { replace: true });
    } catch {
      notify('Invalid login credentials', 'error');
    }
  }

  return (
    <main className="login-page">
      <Card title="Trade License Workflow">
        <p className="muted">Sign in with one of the seeded accounts. Default password is <strong>password</strong>.</p>
        <form className="form-grid" onSubmit={handleSubmit}>
          <FormField label="Email" htmlFor="email">
            <input id="email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} />
          </FormField>
          <FormField label="Password" htmlFor="password">
            <input id="password" type="password" value={password} onChange={(event) => setPassword(event.target.value)} />
          </FormField>
          <button className="button primary" type="submit" disabled={isLoading}>
            {isLoading ? 'Signing in...' : 'Sign in'}
          </button>
          <Link className="inline-link" to="/register">Create a customer account</Link>
        </form>
        <div className="demo-accounts">
          {demoAccounts.map((account) => (
            <button className="chip" type="button" key={account} onClick={() => setEmail(account)}>
              {account}
            </button>
          ))}
        </div>
      </Card>
    </main>
  );
}
