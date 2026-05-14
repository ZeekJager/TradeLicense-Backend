type FileUploaderProps = {
  label: string;
  accept?: string;
  file?: File | null;
  onChange: (file: File | null) => void;
};

export function FileUploader({ label, accept, file, onChange }: FileUploaderProps) {
  return (
    <div className="file-uploader">
      <label>
        <span>{label}</span>
        <input
          type="file"
          accept={accept}
          onChange={(event) => onChange(event.target.files?.[0] ?? null)}
        />
      </label>
      <div className="file-preview">{file ? `${file.name} (${Math.ceil(file.size / 1024)} KB)` : 'No file selected'}</div>
    </div>
  );
}
