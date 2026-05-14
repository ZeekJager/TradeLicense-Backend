import { Link } from 'react-router-dom';
import { Card } from '../components/ui/Card';

const steps = [
  ['Apply', 'Create a trade license request and upload the required files.'],
  ['Review', 'Reviewers inspect documents, payment evidence, and customer details.'],
  ['Approve', 'Approvers issue, reject, or send reviewed applications back.']
];

export function HomePage() {
  return (
    <main className="public-page">
      <section className="home-hero">
        <div className="home-copy">
          <span className="eyebrow">Trade License Workflow</span>
          <h1>Manage applications, reviews, approvals, and license verification in one workspace.</h1>
          <p>
            Customers can apply and track license actions while reviewers and approvers inspect every uploaded document before a license is issued.
          </p>
          <div className="button-row">
            <Link className="button primary" to="/register">Create customer account</Link>
            <Link className="button ghost" to="/login">Sign in</Link>
            <Link className="button ghost" to="/verify">Verify license</Link>
          </div>
        </div>
        <div className="home-preview" aria-label="Workflow preview">
          <div className="preview-header">
            <strong>License pipeline</strong>
            <span>Live workflow</span>
          </div>
          <div className="preview-row active"><span>01</span><strong>Application submitted</strong><small>Customer</small></div>
          <div className="preview-row"><span>02</span><strong>Documents inspected</strong><small>Reviewer</small></div>
          <div className="preview-row"><span>03</span><strong>License issued</strong><small>Approver</small></div>
        </div>
      </section>

      <section className="public-section">
        {steps.map(([title, text]) => (
          <Card key={title}>
            <span className="eyebrow">{title}</span>
            <p>{text}</p>
          </Card>
        ))}
      </section>
    </main>
  );
}
