type StepperProps = {
  steps: string[];
  activeIndex: number;
};

export function Stepper({ steps, activeIndex }: StepperProps) {
  return (
    <div className="stepper" aria-label="Workflow steps">
      {steps.map((step, index) => (
        <div key={step} className={`step ${index <= activeIndex ? 'done' : ''}`}>
          <span>{index + 1}</span>
          <p>{step}</p>
        </div>
      ))}
    </div>
  );
}
