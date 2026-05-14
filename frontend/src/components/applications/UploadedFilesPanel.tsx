import { Card } from '../ui/Card';
import { EmptyState } from '../ui/StateViews';
import { API_BASE_URL } from '../../services/api';
import { FileMetadata } from '../../types/domain';

type UploadedFilesPanelProps = {
  applicationId: string;
  documents: FileMetadata[];
  paymentSlip?: FileMetadata;
  token?: string | null;
  onError: (message: string) => void;
};

export function UploadedFilesPanel({
  applicationId,
  documents,
  paymentSlip,
  token,
  onError
}: UploadedFilesPanelProps) {
  return (
    <>
      <Card title="Uploaded customer documents">
        {documents.length === 0 ? (
          <EmptyState label="No documents uploaded by the customer." />
        ) : (
          <div className="inspection-list">
            {documents.map((file) => (
              <UploadedFileRow
                key={file.id}
                file={file}
                label={file.documentType}
                token={token}
                url={`${API_BASE_URL}/trade-license-applications/${applicationId}/documents/${file.id}/download`}
                onError={onError}
              />
            ))}
          </div>
        )}
      </Card>

      <Card title="Uploaded payment slip">
        {paymentSlip ? (
          <UploadedFileRow
            file={paymentSlip}
            label="Bank slip"
            token={token}
            url={`${API_BASE_URL}/trade-license-applications/${applicationId}/payment/slip/download`}
            onError={onError}
          />
        ) : (
          <EmptyState label="No payment slip uploaded by the customer." />
        )}
      </Card>
    </>
  );
}

type UploadedFileRowProps = {
  file: FileMetadata;
  label: string;
  token?: string | null;
  url: string;
  onError: (message: string) => void;
};

function UploadedFileRow({ file, label, token, url, onError }: UploadedFileRowProps) {
  async function preview() {
    try {
      const blobUrl = await createAuthorizedBlobUrl(url, token);
      const opened = window.open(blobUrl, '_blank', 'noopener,noreferrer');
      if (!opened) {
        throw new Error('The browser blocked the preview window');
      }
      window.setTimeout(() => URL.revokeObjectURL(blobUrl), 60_000);
    } catch (error) {
      onError(error instanceof Error ? error.message : 'Could not preview file');
    }
  }

  async function download() {
    try {
      const blobUrl = await createAuthorizedBlobUrl(url, token);
      const link = document.createElement('a');
      link.href = blobUrl;
      link.download = file.originalFileName;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.setTimeout(() => URL.revokeObjectURL(blobUrl), 1_000);
    } catch (error) {
      onError(error instanceof Error ? error.message : 'Could not download file');
    }
  }

  return (
    <div className="file-row inspectable">
      <div className="file-main">
        <span>{label}</span>
        <strong>{file.originalFileName}</strong>
        <small>
          {formatFileSize(file.size)} - {file.contentType || 'unknown type'} - {formatDate(file.uploadedAt)}
        </small>
      </div>
      <div className="button-row">
        <button className="button ghost" type="button" onClick={preview}>Preview</button>
        <button className="button primary" type="button" onClick={download}>Download</button>
      </div>
    </div>
  );
}

async function createAuthorizedBlobUrl(url: string, token?: string | null) {
  if (!token) {
    throw new Error('You must be logged in to inspect uploaded files');
  }

  const response = await fetch(url, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw new Error(`File request failed with status ${response.status}`);
  }

  return URL.createObjectURL(await response.blob());
}

function formatFileSize(size: number) {
  if (size < 1024) {
    return `${size} B`;
  }
  if (size < 1024 * 1024) {
    return `${(size / 1024).toFixed(1)} KB`;
  }
  return `${(size / (1024 * 1024)).toFixed(1)} MB`;
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, {
    dateStyle: 'medium',
    timeStyle: 'short'
  }).format(new Date(value));
}
