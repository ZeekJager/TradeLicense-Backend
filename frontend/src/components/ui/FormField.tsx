type FormFieldProps = {
  label: string;
  htmlFor: string;
  hint?: string;
  children: React.ReactNode;
};

export function FormField({ label, htmlFor, hint, children }: FormFieldProps) {
  return (
    <label className="field" htmlFor={htmlFor}>
      <span>{label}</span>
      {children}
      {hint && <small>{hint}</small>}
    </label>
  );
}
