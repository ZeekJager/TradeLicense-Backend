type CardProps = {
  title?: string;
  actions?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
};

export function Card({ title, actions, children, className = '' }: CardProps) {
  return (
    <section className={`card ${className}`}>
      {(title || actions) && (
        <div className="card-header">
          {title && <h2>{title}</h2>}
          {actions}
        </div>
      )}
      {children}
    </section>
  );
}
