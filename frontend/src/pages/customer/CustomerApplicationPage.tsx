import { useMemo, useState } from 'react';
import { useAppSelector } from '../../app/hooks';
import { WORKFLOW_STEPS } from '../../constants/workflow';
import { LICENSE_TYPE_OPTIONS, REQUIRED_TRADE_LICENSE_DOCUMENTS } from '../../constants/licenseTypes';
import { Card } from '../../components/ui/Card';
import { FileUploader } from '../../components/ui/FileUploader';
import { FormField } from '../../components/ui/FormField';
import { EmptyState, LoadingState } from '../../components/ui/StateViews';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { Stepper } from '../../components/ui/Stepper';
import { Timeline } from '../../components/ui/Timeline';
import { useToast } from '../../components/ui/ToastProvider';
import {
  useCancelApplicationMutation,
  useCreateApplicationMutation,
  useLicenseTypesQuery,
  useListApplicationsQuery,
  useResubmitApplicationMutation,
  useSubmitApplicationMutation,
  useTimelineQuery,
  useUploadDocumentMutation,
  useUploadPaymentSlipMutation
} from '../../services/applicationsApi';
import { useMyTradeLicensesQuery, useUpdateTradeLicenseMutation } from '../../services/licensesApi';
import { ApplicationStatus, TradeLicense, TradeLicenseApplication, UserRole } from '../../types/domain';

export function CustomerApplicationPage() {
  const user = useAppSelector((state) => state.auth.user);
  const { notify } = useToast();
  const { data: licenseTypes = [], isLoading: loadingLicenseTypes } = useLicenseTypesQuery();
  const { data: returnedApplications = [], isLoading: loadingReturnedApplications } = useListApplicationsQuery(ApplicationStatus.RETURNED_FOR_ADJUSTMENT);
  const { data: issuedLicenses = [], isLoading: loadingLicenses } = useMyTradeLicensesQuery();
  const [createApplication, { isLoading: creating }] = useCreateApplicationMutation();
  const [uploadDocument, { isLoading: uploadingDocument }] = useUploadDocumentMutation();
  const [uploadPaymentSlip, { isLoading: uploadingPayment }] = useUploadPaymentSlipMutation();
  const [submitApplication, { isLoading: submitting }] = useSubmitApplicationMutation();
  const [resubmitApplication, { isLoading: resubmitting }] = useResubmitApplicationMutation();
  const [updateTradeLicense, { isLoading: updatingLicense }] = useUpdateTradeLicenseMutation();
  const [cancelApplication] = useCancelApplicationMutation();
  const [application, setApplication] = useState<TradeLicenseApplication | null>(null);
  const [step, setStep] = useState(0);
  const [form, setForm] = useState({
    applicantId: user?.id ?? crypto.randomUUID(),
    fullName: user?.fullName ?? '',
    tradeName: 'Zekarias Retail Trading',
    nationalIdNumber: '1234567890123456',
    email: user?.email ?? '',
    phoneNumber: '0912345678',
    tradeLicenseType: user?.legalCondition ?? 'PLC',
    commodity: 'Retail Shop',
    bankAccountNumber: user?.bankAccountNumber ?? '1000200030004000'
  });
  const [documents, setDocuments] = useState<Record<string, File | null>>({});
  const [paymentSlip, setPaymentSlip] = useState<File | null>(null);
  const [selectedLicenseId, setSelectedLicenseId] = useState<string | null>(null);
  const [licenseUpdateForm, setLicenseUpdateForm] = useState({
    tradeLicenseType: '',
    commodity: ''
  });
  const { data: timeline = [] } = useTimelineQuery(application?.applicationId ?? '', { skip: !application });

  const selectedLicenseType = useMemo(
    () => licenseTypes.find((licenseType) => licenseType.code === form.tradeLicenseType),
    [form.tradeLicenseType, licenseTypes]
  );
  const licenseTypeChoices = licenseTypes.length > 0 ? licenseTypes : LICENSE_TYPE_OPTIONS;
  const requiredDocuments = (selectedLicenseType?.requiredDocuments ?? REQUIRED_TRADE_LICENSE_DOCUMENTS)
    .map((documentType) => documentType === 'National ID' ? 'Bank Letter Supporting Capital of the Business' : documentType);
  const isAdjustment = application?.status === ApplicationStatus.RETURNED_FOR_ADJUSTMENT;
  const selectedLicense = useMemo(
    () => issuedLicenses.find((license) => license.licenseId === selectedLicenseId) ?? issuedLicenses[0],
    [issuedLicenses, selectedLicenseId]
  );

  function openReturnedApplication(returnedApplication: TradeLicenseApplication) {
    setApplication(returnedApplication);
    setForm((current) => ({
      ...current,
      applicantId: returnedApplication.applicantId,
      fullName: returnedApplication.fullName,
      tradeName: returnedApplication.tradeName,
      nationalIdNumber: returnedApplication.nationalIdNumber,
      email: returnedApplication.email,
      phoneNumber: returnedApplication.phoneNumber,
      tradeLicenseType: returnedApplication.tradeLicenseType,
      commodity: returnedApplication.commodity,
      bankAccountNumber: returnedApplication.bankAccountNumber
    }));
    setDocuments({});
    setPaymentSlip(null);
    setStep(1);
  }

  function openLicenseForUpdate(tradeLicense: TradeLicense) {
    setSelectedLicenseId(tradeLicense.licenseId);
    setLicenseUpdateForm({
      tradeLicenseType: tradeLicense.tradeLicenseType,
      commodity: tradeLicense.commodity
    });
  }

  async function handleCreate() {
    try {
      const created = await createApplication(form).unwrap();
      setApplication(created);
      setStep(1);
      notify('Application draft created', 'success');
    } catch {
      notify('Could not create application', 'error');
    }
  }

  async function handleUploadDocuments() {
    if (!application) return;
    const entries = Object.entries(documents).filter(([, file]) => file);
    const missingDocuments = requiredDocuments.filter((documentType) => !documents[documentType]);
    if (missingDocuments.length > 0) {
      notify(`Upload required document: ${missingDocuments[0]}`, 'error');
      return;
    }
    try {
      let latest = application;
      for (const [documentType, file] of entries) {
        latest = await uploadDocument({ applicationId: application.applicationId, documentType, file: file! }).unwrap();
      }
      setApplication(latest);
      setStep(latest.status === ApplicationStatus.RETURNED_FOR_ADJUSTMENT ? 3 : 2);
      notify(latest.status === ApplicationStatus.RETURNED_FOR_ADJUSTMENT ? 'Corrected documents uploaded' : 'Documents uploaded', 'success');
    } catch {
      notify('Document upload failed', 'error');
    }
  }

  async function handleUploadPayment() {
    if (!application || !paymentSlip) {
      notify('Upload a bank slip before continuing', 'error');
      return;
    }
    try {
      const updated = await uploadPaymentSlip({ applicationId: application.applicationId, file: paymentSlip }).unwrap();
      setApplication(updated);
      setStep(3);
      notify('Payment slip uploaded and payment settled', 'success');
    } catch {
      notify('Payment upload failed', 'error');
    }
  }

  async function handleSubmit() {
    if (!application || !user) return;
    try {
      const updated = await submitApplication({
        applicationId: application.applicationId,
        actorId: user.id,
        role: user.role === UserRole.LICENSEE ? UserRole.LICENSEE : UserRole.CUSTOMER
      }).unwrap();
      setApplication(updated);
      notify('Application submitted for review', 'success');
    } catch {
      notify('Submit failed. Confirm documents and payment are complete.', 'error');
    }
  }

  async function handleCancel() {
    if (!application || !user) return;
    const updated = await cancelApplication({ applicationId: application.applicationId, actorId: user.id, role: UserRole.CUSTOMER }).unwrap();
    setApplication(updated);
    notify('Application cancelled', 'info');
  }

  async function handleResubmit() {
    if (!application || !user) return;
    try {
      const updated = await resubmitApplication({
        applicationId: application.applicationId,
        actorId: user.id,
        role: user.role === UserRole.LICENSEE ? UserRole.LICENSEE : UserRole.CUSTOMER
      }).unwrap();
      setApplication(updated);
      notify('Application resubmitted for review', 'success');
    } catch {
      notify('Resubmit failed. Upload corrected documents first.', 'error');
    }
  }

  async function handleUpdateLicense() {
    if (!selectedLicense || !user) return;
    try {
      const role = user.role === UserRole.LICENSEE ? UserRole.LICENSEE : user.role === UserRole.ADMIN ? UserRole.ADMIN : UserRole.CUSTOMER;
      const updated = await updateTradeLicense({
        licenseId: selectedLicense.licenseId,
        actorId: user.id,
        role,
        tradeLicenseType: licenseUpdateForm.tradeLicenseType || selectedLicense.tradeLicenseType,
        commodity: licenseUpdateForm.commodity || selectedLicense.commodity
      }).unwrap();
      setSelectedLicenseId(updated.licenseId);
      setLicenseUpdateForm({
        tradeLicenseType: updated.tradeLicenseType,
        commodity: updated.commodity
      });
      notify('Trade license updated', 'success');
    } catch {
      notify('Could not update trade license', 'error');
    }
  }

  if (loadingLicenseTypes || loadingReturnedApplications || loadingLicenses) {
    return <LoadingState />;
  }

  return (
    <div className="page-stack">
      <div className="page-title">
        <div>
          <span className="eyebrow">Customer workflow</span>
          <h1>Submit New Trade License Application</h1>
        </div>
        {application && <StatusBadge status={application.status} />}
      </div>

      <Stepper steps={WORKFLOW_STEPS} activeIndex={step} />

      <Card title="My issued trade licenses">
        {issuedLicenses.length === 0 ? (
          <EmptyState label="No trade license has been issued for your account yet." />
        ) : (
          <div className="license-panel">
            <div className="record-list">
              {issuedLicenses.map((tradeLicense) => (
                <button
                  className={selectedLicense?.licenseId === tradeLicense.licenseId ? 'record active' : 'record'}
                  key={tradeLicense.licenseId}
                  type="button"
                  onClick={() => openLicenseForUpdate(tradeLicense)}
                >
                  <strong>{tradeLicense.licenseNumber}</strong>
                  <span>{tradeLicense.tradeLicenseType} - {tradeLicense.commodity}</span>
                  <span>Valid until {formatDate(tradeLicense.validTo)}</span>
                </button>
              ))}
            </div>

            {selectedLicense && (
              <div className="page-stack">
                <div className="summary-list">
                  <span>License number</span><strong>{selectedLicense.licenseNumber}</strong>
                  <span>TIN number</span><strong>{selectedLicense.tinNumber}</strong>
                  <span>Issued date</span><strong>{formatDate(selectedLicense.issuedDate)}</strong>
                  <span>Valid period</span><strong>{formatDate(selectedLicense.validFrom)} to {formatDate(selectedLicense.validTo)}</strong>
                  <span>Holder</span><strong>{selectedLicense.fullName}</strong>
                </div>
                <div className="form-grid two">
                  <FormField label="Trade license type" htmlFor="license-update-type">
                    <select
                      id="license-update-type"
                      value={licenseUpdateForm.tradeLicenseType || selectedLicense.tradeLicenseType}
                      onChange={(event) => setLicenseUpdateForm({ ...licenseUpdateForm, tradeLicenseType: event.target.value })}
                    >
                      {licenseTypeChoices.map((licenseType) => (
                        <option key={licenseType.code} value={licenseType.code}>{licenseType.name}</option>
                      ))}
                    </select>
                  </FormField>
                  <FormField label="Commodity" htmlFor="license-update-commodity">
                    <input
                      id="license-update-commodity"
                      value={licenseUpdateForm.commodity || selectedLicense.commodity}
                      onChange={(event) => setLicenseUpdateForm({ ...licenseUpdateForm, commodity: event.target.value })}
                    />
                  </FormField>
                  <button className="button primary" type="button" onClick={handleUpdateLicense} disabled={updatingLicense}>
                    Update Trade License
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </Card>

      <Card title="Returned for adjustment">
        {returnedApplications.length === 0 ? (
          <EmptyState label="No applications are currently returned for adjustment." />
        ) : (
          <div className="record-list compact">
            {returnedApplications.map((returnedApplication) => (
              <button
                className={application?.applicationId === returnedApplication.applicationId ? 'record active' : 'record'}
                key={returnedApplication.applicationId}
                type="button"
                onClick={() => openReturnedApplication(returnedApplication)}
              >
                <strong>{returnedApplication.fullName}</strong>
                <span>{returnedApplication.commodity}</span>
                <StatusBadge status={returnedApplication.status} />
              </button>
            ))}
          </div>
        )}
      </Card>

      <div className="content-grid">
        <Card title="Application wizard">
          {step === 0 && (
            <div className="form-grid two">
              {user?.tinNumber && (
                <FormField label="Registered TIN number" htmlFor="profile-tin">
                  <input id="profile-tin" value={user.tinNumber} disabled />
                </FormField>
              )}
              <FormField label="License type" htmlFor="tradeLicenseType">
                <select id="tradeLicenseType" value={form.tradeLicenseType} onChange={(event) => setForm({ ...form, tradeLicenseType: event.target.value })}>
                  {licenseTypeChoices.map((licenseType) => <option key={licenseType.code} value={licenseType.code}>{licenseType.name}</option>)}
                </select>
              </FormField>
              <FormField label="Commodity" htmlFor="commodity">
                <input id="commodity" value={form.commodity} onChange={(event) => setForm({ ...form, commodity: event.target.value })} />
              </FormField>
              <FormField label="Trade name" htmlFor="tradeName">
                <input id="tradeName" value={form.tradeName} onChange={(event) => setForm({ ...form, tradeName: event.target.value })} />
              </FormField>
              <FormField label="Full name" htmlFor="fullName">
                <input id="fullName" value={form.fullName} onChange={(event) => setForm({ ...form, fullName: event.target.value })} />
              </FormField>
              <FormField label="National ID number (16 digits)" htmlFor="nationalId">
                <input
                  id="nationalId"
                  inputMode="numeric"
                  maxLength={16}
                  pattern="[0-9]{16}"
                  value={form.nationalIdNumber}
                  onChange={(event) => setForm({ ...form, nationalIdNumber: event.target.value.replace(/\D/g, '').slice(0, 16) })}
                />
              </FormField>
              <FormField label="Email" htmlFor="email">
                <input id="email" type="email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} />
              </FormField>
              <FormField label="Phone number" htmlFor="phone">
                <input id="phone" value={form.phoneNumber} onChange={(event) => setForm({ ...form, phoneNumber: event.target.value })} />
              </FormField>
              <FormField label="Dedicated trade bank account number" htmlFor="trade-bank-account">
                <input
                  id="trade-bank-account"
                  value={form.bankAccountNumber}
                  onChange={(event) => setForm({ ...form, bankAccountNumber: event.target.value })}
                />
              </FormField>
              <button className="button primary" type="button" onClick={handleCreate} disabled={creating}>
                Create Draft
              </button>
            </div>
          )}

          {step === 1 && (
            <div className="form-grid">
              <p className="muted">
                {isAdjustment
                  ? 'Upload corrected documents for the reviewer adjustment request.'
                  : `Upload the TIN certificate, bank letter supporting capital, and formation letter from the government office for ${selectedLicenseType?.name ?? form.tradeLicenseType}.`}
              </p>
              {requiredDocuments.map((documentType) => (
                <FileUploader
                  key={documentType}
                  label={documentType}
                  file={documents[documentType]}
                  onChange={(file) => setDocuments({ ...documents, [documentType]: file })}
                />
              ))}
              <button className="button primary" type="button" onClick={handleUploadDocuments} disabled={uploadingDocument}>
                {isAdjustment ? 'Upload Corrected Documents' : 'Upload Documents'}
              </button>
            </div>
          )}

          {step === 2 && (
            <div className="form-grid">
              <FileUploader label="Bank slip" file={paymentSlip} onChange={setPaymentSlip} />
              <button className="button primary" type="button" onClick={handleUploadPayment} disabled={uploadingPayment}>
                Upload Bank Slip
              </button>
            </div>
          )}

          {step === 3 && (
            <div className="action-panel">
              <p>{isAdjustment ? 'Corrected documents are ready to send back to review.' : 'Application is ready to submit.'}</p>
              {isAdjustment ? (
                <button className="button primary" type="button" onClick={handleResubmit} disabled={resubmitting}>Resubmit</button>
              ) : (
                <>
                  <button className="button primary" type="button" onClick={handleSubmit} disabled={submitting}>Submit</button>
                  <button className="button danger" type="button" onClick={handleCancel}>Cancel</button>
                </>
              )}
            </div>
          )}
        </Card>

        <Card title="Status panel">
          {application ? (
            <div className="summary-list">
              <span>Application ID</span><strong>{application.applicationId}</strong>
              <span>Trade name</span><strong>{application.tradeName}</strong>
              <span>License type</span><strong>{application.tradeLicenseType}</strong>
              <span>Commodity</span><strong>{application.commodity}</strong>
              <span>Trade bank account</span><strong>{application.bankAccountNumber}</strong>
              <span>Status</span><StatusBadge status={application.status} />
            </div>
          ) : (
            <p className="muted">Create a draft application to start the workflow.</p>
          )}
        </Card>
      </div>

      {application && (
        <Card title="Audit timeline">
          <Timeline events={timeline} />
        </Card>
      )}
    </div>
  );
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, {
    dateStyle: 'medium'
  }).format(new Date(value));
}
