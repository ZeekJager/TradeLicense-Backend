import { STATUS_LABELS } from '../../constants/workflow';
import { ApplicationStatus } from '../../types/domain';

export function StatusBadge({ status }: { status: ApplicationStatus }) {
  return <span className={`status-badge status-${status.toLowerCase()}`}>{STATUS_LABELS[status]}</span>;
}
