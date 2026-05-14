import { FormEvent, useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { useAppSelector } from '../app/hooks';
import { Card } from '../components/ui/Card';
import { FormField } from '../components/ui/FormField';
import { useToast } from '../components/ui/ToastProvider';
import { LICENSE_TYPE_OPTIONS } from '../constants/licenseTypes';
import { ROLE_HOME } from '../constants/workflow';
import { useRegisterMutation } from '../services/authApi';

export function RegisterPage() {
  const navigate = useNavigate();
  const { notify } = useToast();
  const { token, user } = useAppSelector((state) => state.auth);
  const [register, { isLoading }] = useRegisterMutation();
  const [form, setForm] = useState({
    tinNumber: 'TIN-987654',
    fullName: 'Zekarias Tesfaye',
    email: 'zekarias@example.com',
    region: 'Addis Ababa',
    businessAddress: 'Addis Ababa, Bole Sub City, Woreda 03',
    password: 'password',
    legalCondition: 'PLC',
    bankAccountNumber: '1000200030004000'
  });

  if (token && user) {
    return <Navigate to={ROLE_HOME[user.role]} replace />;
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    try {
      const response = await register(form).unwrap();
      notify('Account registered', 'success');
      navigate(ROLE_HOME[response.user.role], { replace: true });
    } catch {
      notify('Registration failed. Check duplicate email, TIN, or bank account number.', 'error');
    }
  }

  return (
    <main className="login-page">
      <Card title="Create Customer Account">
        <form className="form-grid two" onSubmit={handleSubmit}>
          <FormField label="TIN number" htmlFor="register-tin">
            <input id="register-tin" value={form.tinNumber} onChange={(event) => setForm({ ...form, tinNumber: event.target.value })} />
          </FormField>
          <FormField label="Full name" htmlFor="register-name">
            <input id="register-name" value={form.fullName} onChange={(event) => setForm({ ...form, fullName: event.target.value })} />
          </FormField>
          <FormField label="Email" htmlFor="register-email">
            <input id="register-email" type="email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} />
          </FormField>
          <FormField label="Region" htmlFor="register-region">
            <input id="register-region" value={form.region} onChange={(event) => setForm({ ...form, region: event.target.value })} />
          </FormField>
          <FormField label="Location of the business address" htmlFor="register-business-address">
            <input
              id="register-business-address"
              value={form.businessAddress}
              onChange={(event) => setForm({ ...form, businessAddress: event.target.value })}
            />
          </FormField>
          <FormField label="Legal condition / license type" htmlFor="register-legal-condition">
            <select
              id="register-legal-condition"
              value={form.legalCondition}
              onChange={(event) => setForm({ ...form, legalCondition: event.target.value })}
            >
              {LICENSE_TYPE_OPTIONS.map((licenseType) => (
                <option key={licenseType.code} value={licenseType.code}>{licenseType.name}</option>
              ))}
            </select>
          </FormField>
          <FormField label="Bank account number" htmlFor="register-bank">
            <input id="register-bank" value={form.bankAccountNumber} onChange={(event) => setForm({ ...form, bankAccountNumber: event.target.value })} />
          </FormField>
          <FormField label="Password" htmlFor="register-password">
            <input id="register-password" type="password" value={form.password} onChange={(event) => setForm({ ...form, password: event.target.value })} />
          </FormField>
          <div className="action-panel">
            <button className="button primary" type="submit" disabled={isLoading}>
              {isLoading ? 'Creating account...' : 'Create Account'}
            </button>
            <Link className="inline-link" to="/login">Sign in instead</Link>
          </div>
        </form>
      </Card>
    </main>
  );
}
