import { AuditEvent } from '../../types/domain';
import { StatusBadge } from './StatusBadge';

export function Timeline({ events }: { events: AuditEvent[] }) {
  if (events.length === 0) {
    return <div className="state-box empty">No audit events yet.</div>;
  }

  return (
    <ol className="timeline">
      {events.map((event) => (
        <li key={event.id}>
          <div>
            <strong>{event.action.replaceAll('_', ' ')}</strong>
            <span>{new Date(event.occurredAt).toLocaleString()}</span>
          </div>
          <StatusBadge status={event.status} />
          {event.comment && <p>{event.comment}</p>}
        </li>
      ))}
    </ol>
  );
}
