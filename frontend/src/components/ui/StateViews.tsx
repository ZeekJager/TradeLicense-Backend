export function LoadingState({ label = 'Loading data...' }: { label?: string }) {
  return <div className="state-box">{label}</div>;
}

export function EmptyState({ label = 'No records found.' }: { label?: string }) {
  return <div className="state-box empty">{label}</div>;
}

export function ErrorState({ message = 'Something went wrong.' }: { message?: string }) {
  return <div className="state-box error">{message}</div>;
}
