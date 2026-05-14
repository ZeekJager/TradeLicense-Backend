import { FormEvent, useState } from 'react';
import { Link } from 'react-router-dom';
import { Card } from '../components/ui/Card';
import { EmptyState } from '../components/ui/StateViews';
import { useLazyVerifyTradeLicenseQuery } from '../services/licensesApi';

export function LicenseVerificationPage() {
  const [licenseId, setLicenseId] = useState('');
  const [verify, { data, isFetching, error }] = useLazyVerifyTradeLicenseQuery();

  function handleSubmit(event: FormEvent) {
    event.preventDefault();
    if (licenseId.trim()) {
      verify(licenseId.trim());
    }
  }

  return (
    <main className="public-page compact">
      <div className="public-topbar">
        <Link className="brand compact-brand" to="/">
          <span className="brand-mark">TL</span>
          <strong>Trade License</strong>
        </Link>
        <Link className="button ghost" to="/login">Sign in</Link>
      </div>

      <Card title="Trade License Verification">
        <form className="form-grid" onSubmit={handleSubmit}>
          <label className="field" htmlFor="license-id">
            <span>Trade license ID</span>
            <input
              id="license-id"
              placeholder="Paste license UUID"
              value={licenseId}
              onChange={(event) => setLicenseId(event.target.value)}
            />
          </label>
          <button className="button primary" type="submit" disabled={isFetching}>
            {isFetching ? 'Checking...' : 'Verify License'}
          </button>
        </form>
      </Card>

      {error && <div className="state-box error">Trade license was not found.</div>}

      {data ? (
        <Card title="Verification result">
          <div className="summary-list">
            <span>Status</span><strong>{data.status}</strong>
            <span>License number</span><strong>{data.licenseNumber}</strong>
            <span>Trade name</span><strong>{data.tradeName}</strong>
            <span>Holder</span><strong>{data.fullName}</strong>
            <span>TIN</span><strong>{data.tinNumber}</strong>
            <span>License type</span><strong>{data.tradeLicenseType}</strong>
            <span>Commodity</span><strong>{data.commodity}</strong>
            <span>Valid until</span><strong>{formatDate(data.validTo)}</strong>
          </div>
        </Card>
      ) : (
        <EmptyState label="Enter a trade license ID to verify its status." />
      )}
    </main>
  );
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, { dateStyle: 'medium' }).format(new Date(value));
}
