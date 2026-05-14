import { useMemo, useState } from 'react';
import { LICENSE_TYPE_OPTIONS } from '../../constants/licenseTypes';
import { useAppSelector } from '../../app/hooks';
import { Card } from '../../components/ui/Card';
import { EmptyState, LoadingState } from '../../components/ui/StateViews';
import { useToast } from '../../components/ui/ToastProvider';
import {
  useCancelTradeLicenseMutation,
  useMyTradeLicensesQuery,
  useRenewTradeLicenseMutation,
  useUpdateTradeLicenseMutation
} from '../../services/licensesApi';
import { LicenseStatus, UserRole } from '../../types/domain';

type Mode = 'update' | 'cancel' | 'renew';

type CustomerLicenseActionPageProps = {
  mode: Mode;
};

const modeText: Record<Mode, { title: string; intro: string; button: string }> = {
  update: {
    title: 'Update Trade License',
    intro: 'Select an issued license and update the allowed business details.',
    button: 'Update Trade License'
  },
  cancel: {
    title: 'Cancel Trade License',
    intro: 'Select an active license and confirm cancellation.',
    button: 'Cancel Trade License'
  },
  renew: {
    title: 'Renew Trade License',
    intro: 'Select an active license to extend its validity by one year.',
    button: 'Renew Trade License'
  }
};

export function CustomerLicenseActionPage({ mode }: CustomerLicenseActionPageProps) {
  const user = useAppSelector((state) => state.auth.user);
  const { notify } = useToast();
  const { data: licenses = [], isLoading } = useMyTradeLicensesQuery();
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [form, setForm] = useState({ tradeLicenseType: 'PLC', commodity: '' });
  const [updateTradeLicense, { isLoading: updating }] = useUpdateTradeLicenseMutation();
  const [cancelTradeLicense, { isLoading: cancelling }] = useCancelTradeLicenseMutation();
  const [renewTradeLicense, { isLoading: renewing }] = useRenewTradeLicenseMutation();

  const selected = useMemo(
    () => licenses.find((license) => license.licenseId === selectedId) ?? licenses[0],
    [licenses, selectedId]
  );

  function chooseLicense(licenseId: string) {
    const license = licenses.find((item) => item.licenseId === licenseId);
    setSelectedId(licenseId);
    if (license) {
      setForm({ tradeLicenseType: license.tradeLicenseType, commodity: license.commodity });
    }
  }

  async function submitAction() {
    if (!selected || !user) return;
    try {
      if (mode === 'update') {
        await updateTradeLicense({
          licenseId: selected.licenseId,
          actorId: user.id,
          role: user.role === UserRole.LICENSEE ? UserRole.LICENSEE : user.role === UserRole.ADMIN ? UserRole.ADMIN : UserRole.CUSTOMER,
          tradeLicenseType: form.tradeLicenseType,
          commodity: form.commodity
        }).unwrap();
      }
      if (mode === 'cancel') {
        await cancelTradeLicense(selected.licenseId).unwrap();
      }
      if (mode === 'renew') {
        await renewTradeLicense(selected.licenseId).unwrap();
      }
      notify(`${modeText[mode].button} completed`, 'success');
    } catch {
      notify(`${modeText[mode].button} failed`, 'error');
    }
  }

  if (isLoading) {
    return <LoadingState />;
  }

  const busy = updating || cancelling || renewing;

  return (
    <div className="page-stack">
      <div className="page-title">
        <div>
          <span className="eyebrow">Customer license service</span>
          <h1>{modeText[mode].title}</h1>
          <p className="muted">{modeText[mode].intro}</p>
        </div>
      </div>

      {licenses.length === 0 ? (
        <EmptyState label="No issued trade license is available for this action." />
      ) : (
        <div className="split-layout">
          <Card title="Your trade licenses">
            <div className="record-list">
              {licenses.map((license) => (
                <button
                  className={selected?.licenseId === license.licenseId ? 'record active' : 'record'}
                  key={license.licenseId}
                  type="button"
                  onClick={() => chooseLicense(license.licenseId)}
                >
                  <strong>{license.licenseNumber}</strong>
                  <span>{license.tradeName}</span>
                  <span>{license.tradeLicenseType} - {license.commodity}</span>
                  <span>{license.status}</span>
                </button>
              ))}
            </div>
          </Card>

          {selected && (
            <Card title="License details">
              <div className="summary-list">
                <span>License ID</span><strong>{selected.licenseId}</strong>
                <span>License number</span><strong>{selected.licenseNumber}</strong>
                <span>Trade name</span><strong>{selected.tradeName}</strong>
                <span>Status</span><strong>{selected.status}</strong>
                <span>Valid until</span><strong>{formatDate(selected.validTo)}</strong>
              </div>

              {mode === 'update' && (
                <div className="form-grid two section-gap">
                  <label className="field" htmlFor="license-action-type">
                    <span>License type</span>
                    <select
                      id="license-action-type"
                      value={form.tradeLicenseType || selected.tradeLicenseType}
                      onChange={(event) => setForm({ ...form, tradeLicenseType: event.target.value })}
                    >
                      {LICENSE_TYPE_OPTIONS.map((licenseType) => (
                        <option key={licenseType.code} value={licenseType.code}>{licenseType.name}</option>
                      ))}
                    </select>
                  </label>
                  <label className="field" htmlFor="license-action-commodity">
                    <span>Commodity</span>
                    <input
                      id="license-action-commodity"
                      value={form.commodity || selected.commodity}
                      onChange={(event) => setForm({ ...form, commodity: event.target.value })}
                    />
                  </label>
                </div>
              )}

              {mode === 'cancel' && selected.status === LicenseStatus.CANCELLED && (
                <div className="state-box empty section-gap">This trade license is already cancelled.</div>
              )}

              {mode === 'renew' && (
                <div className="state-box section-gap">
                  Renewal will extend the validity period by one year from the current expiry date.
                </div>
              )}

              <button
                className={mode === 'cancel' ? 'button danger section-gap' : 'button primary section-gap'}
                type="button"
                onClick={submitAction}
                disabled={busy || (mode === 'cancel' && selected.status === LicenseStatus.CANCELLED)}
              >
                {modeText[mode].button}
              </button>
            </Card>
          )}
        </div>
      )}
    </div>
  );
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, { dateStyle: 'medium' }).format(new Date(value));
}
