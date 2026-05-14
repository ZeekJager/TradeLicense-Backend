import { useMemo, useState } from 'react';
import { useAppSelector } from '../../app/hooks';
import { UploadedFilesPanel } from '../../components/applications/UploadedFilesPanel';
import { Card } from '../../components/ui/Card';
import { LICENSE_TYPE_OPTIONS } from '../../constants/licenseTypes';
import { EmptyState, LoadingState } from '../../components/ui/StateViews';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { Timeline } from '../../components/ui/Timeline';
import { useToast } from '../../components/ui/ToastProvider';
import {
  useApproveApplicationMutation,
  useListDocumentsQuery,
  usePaymentSlipQuery,
  usePendingApprovalQuery,
  useTimelineQuery
} from '../../services/applicationsApi';
import { ApprovalDecision, TradeLicenseApplication, UserRole } from '../../types/domain';

export function ApproverPage() {
  const user = useAppSelector((state) => state.auth.user);
  const token = useAppSelector((state) => state.auth.token);
  const { notify } = useToast();
  const { data: pending = [], isLoading } = usePendingApprovalQuery();
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [comment, setComment] = useState('Reviewed application approved.');
  const [licenseNumber, setLicenseNumber] = useState('TL-2026-0001');
  const [tinNumber, setTinNumber] = useState('TIN-987654');
  const [licenseTypeToIssue, setLicenseTypeToIssue] = useState('PLC');
  const [approveApplication, { isLoading: approving }] = useApproveApplicationMutation();
  const selected = useMemo(
    () => pending.find((application) => application.applicationId === selectedId) ?? pending[0],
    [pending, selectedId]
  );
  const { data: documents = [] } = useListDocumentsQuery(selected?.applicationId ?? '', { skip: !selected });
  const { data: paymentSlip } = usePaymentSlipQuery(selected?.applicationId ?? '', { skip: !selected });
  const { data: timeline = [] } = useTimelineQuery(selected?.applicationId ?? '', { skip: !selected });

  async function decide(decision: ApprovalDecision) {
    if (!selected || !user) return;
    try {
      await approveApplication({
        applicationId: selected.applicationId,
        approverId: user.id,
        role: UserRole.APPROVER,
        decision,
        comment,
        licenseNumber,
        tinNumber,
        licenseTypeToIssue
      }).unwrap();
      notify(`Approval decision ${decision} saved`, 'success');
      setSelectedId(null);
    } catch {
      notify('Could not save approval decision', 'error');
    }
  }

  if (isLoading) {
    return <LoadingState />;
  }

  return (
    <div className="page-stack">
      <div className="page-title">
        <div>
          <span className="eyebrow">Approver workflow</span>
          <h1>Approve Reviewed Applications</h1>
        </div>
      </div>

      <div className="split-layout">
        <Card title="Pending approval queue">
          {pending.length === 0 ? <EmptyState label="No applications are pending approval." /> : (
            <div className="record-list">
              {pending.map((application) => (
                <button
                  key={application.applicationId}
                  className={selected?.applicationId === application.applicationId ? 'record active' : 'record'}
                  type="button"
                  onClick={() => setSelectedId(application.applicationId)}
                >
                  <strong>{application.tradeName}</strong>
                  <span>{application.fullName}</span>
                  <span>{application.commodity}</span>
                  <StatusBadge status={application.status} />
                </button>
              ))}
            </div>
          )}
        </Card>

        <div className="page-stack">
          {selected ? (
            <>
              <ApplicationSummary application={selected} />
              <UploadedFilesPanel
                applicationId={selected.applicationId}
                documents={documents}
                paymentSlip={paymentSlip}
                token={token}
                onError={(message) => notify(message, 'error')}
              />
              <Card title="Decision">
                <div className="form-grid two">
                  <label className="field" htmlFor="license-number">
                    <span>License number</span>
                    <input id="license-number" value={licenseNumber} onChange={(event) => setLicenseNumber(event.target.value)} />
                  </label>
                  <label className="field" htmlFor="tin-number">
                    <span>TIN number</span>
                    <input id="tin-number" value={tinNumber} onChange={(event) => setTinNumber(event.target.value)} />
                  </label>
                  <label className="field" htmlFor="license-type-issue">
                    <span>License type to issue</span>
                    <select id="license-type-issue" value={licenseTypeToIssue} onChange={(event) => setLicenseTypeToIssue(event.target.value)}>
                      {LICENSE_TYPE_OPTIONS.map((licenseType) => (
                        <option key={licenseType.code} value={licenseType.code}>{licenseType.name}</option>
                      ))}
                    </select>
                  </label>
                  <label className="field" htmlFor="approval-comment">
                    <span>Comment</span>
                    <textarea id="approval-comment" value={comment} onChange={(event) => setComment(event.target.value)} />
                  </label>
                </div>
                <div className="button-row">
                  <button className="button primary" type="button" disabled={approving} onClick={() => decide(ApprovalDecision.APPROVE)}>Approve</button>
                  <button className="button danger" type="button" disabled={approving} onClick={() => decide(ApprovalDecision.REJECT)}>Reject</button>
                  <button className="button warning" type="button" disabled={approving} onClick={() => decide(ApprovalDecision.REREVIEW)}>Rereview</button>
                </div>
              </Card>
              <Card title="Audit trail">
                <Timeline events={timeline} />
              </Card>
            </>
          ) : (
            <EmptyState label="Select an application to approve." />
          )}
        </div>
      </div>
    </div>
  );
}

function ApplicationSummary({ application }: { application: TradeLicenseApplication }) {
  return (
    <Card title="Reviewed summary">
      <div className="summary-list">
        <span>Applicant</span><strong>{application.fullName}</strong>
        <span>Trade name</span><strong>{application.tradeName}</strong>
        <span>License type</span><strong>{application.tradeLicenseType}</strong>
        <span>Commodity</span><strong>{application.commodity}</strong>
        <span>Status</span><StatusBadge status={application.status} />
      </div>
    </Card>
  );
}
