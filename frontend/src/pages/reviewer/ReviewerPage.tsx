import { useMemo, useState } from 'react';
import { useAppSelector } from '../../app/hooks';
import { UploadedFilesPanel } from '../../components/applications/UploadedFilesPanel';
import { Card } from '../../components/ui/Card';
import { EmptyState, LoadingState } from '../../components/ui/StateViews';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { Timeline } from '../../components/ui/Timeline';
import { useToast } from '../../components/ui/ToastProvider';
import {
  useListDocumentsQuery,
  usePaymentSlipQuery,
  usePendingReviewQuery,
  useReviewApplicationMutation,
  useTimelineQuery
} from '../../services/applicationsApi';
import { ReviewDecision, TradeLicenseApplication, UserRole } from '../../types/domain';

export function ReviewerPage() {
  const user = useAppSelector((state) => state.auth.user);
  const token = useAppSelector((state) => state.auth.token);
  const { notify } = useToast();
  const { data: pending = [], isLoading } = usePendingReviewQuery();
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [comment, setComment] = useState('Application reviewed successfully.');
  const [reviewApplication, { isLoading: reviewing }] = useReviewApplicationMutation();
  const selected = useMemo(
    () => pending.find((application) => application.applicationId === selectedId) ?? pending[0],
    [pending, selectedId]
  );
  const { data: documents = [] } = useListDocumentsQuery(selected?.applicationId ?? '', { skip: !selected });
  const { data: paymentSlip } = usePaymentSlipQuery(selected?.applicationId ?? '', { skip: !selected });
  const { data: timeline = [] } = useTimelineQuery(selected?.applicationId ?? '', { skip: !selected });

  async function decide(decision: ReviewDecision) {
    if (!selected || !user) return;
    try {
      await reviewApplication({
        applicationId: selected.applicationId,
        reviewerId: user.id,
        role: UserRole.REVIEWER,
        decision,
        comment
      }).unwrap();
      notify(`Review decision ${decision} saved`, 'success');
      setSelectedId(null);
    } catch {
      notify('Could not save review decision', 'error');
    }
  }

  if (isLoading) {
    return <LoadingState />;
  }

  return (
    <div className="page-stack">
      <div className="page-title">
        <div>
          <span className="eyebrow">Reviewer workflow</span>
          <h1>Review Submitted Applications</h1>
        </div>
      </div>

      <div className="split-layout">
        <Card title="Pending review queue">
          {pending.length === 0 ? (
            <EmptyState label="No applications are pending review." />
          ) : (
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
              <ApplicationDetails application={selected} />
              <UploadedFilesPanel
                applicationId={selected.applicationId}
                documents={documents}
                paymentSlip={paymentSlip}
                token={token}
                onError={(message) => notify(message, 'error')}
              />
              <Card title="Decision panel">
                <label className="field" htmlFor="review-comment">
                  <span>Comment</span>
                  <textarea id="review-comment" value={comment} onChange={(event) => setComment(event.target.value)} />
                </label>
                <div className="button-row">
                  <button className="button primary" type="button" disabled={reviewing} onClick={() => decide(ReviewDecision.ACCEPT)}>Accept</button>
                  <button className="button danger" type="button" disabled={reviewing} onClick={() => decide(ReviewDecision.REJECT)}>Reject</button>
                  <button className="button warning" type="button" disabled={reviewing} onClick={() => decide(ReviewDecision.ADJUST)}>Adjust</button>
                </div>
              </Card>
              <Card title="Audit trail">
                <Timeline events={timeline} />
              </Card>
            </>
          ) : (
            <EmptyState label="Select an application to review." />
          )}
        </div>
      </div>
    </div>
  );
}

function ApplicationDetails({ application }: { application: TradeLicenseApplication }) {
  return (
    <Card title="Application details">
      <div className="summary-list">
        <span>Applicant</span><strong>{application.fullName}</strong>
        <span>Trade name</span><strong>{application.tradeName}</strong>
        <span>Commodity</span><strong>{application.commodity}</strong>
        <span>License type</span><strong>{application.tradeLicenseType}</strong>
        <span>Email</span><strong>{application.email}</strong>
        <span>Status</span><StatusBadge status={application.status} />
      </div>
    </Card>
  );
}
