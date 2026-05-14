export enum UserRole {
  CUSTOMER = 'CUSTOMER',
  LICENSEE = 'LICENSEE',
  REVIEWER = 'REVIEWER',
  APPROVER = 'APPROVER',
  ADMIN = 'ADMIN'
}

export enum ApplicationStatus {
  DRAFT = 'DRAFT',
  PENDING_REVIEW = 'PENDING_REVIEW',
  RETURNED_FOR_ADJUSTMENT = 'RETURNED_FOR_ADJUSTMENT',
  PENDING_APPROVAL = 'PENDING_APPROVAL',
  APPROVED = 'APPROVED',
  LICENSE_ISSUED = 'LICENSE_ISSUED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}

export enum ReviewDecision {
  ACCEPT = 'ACCEPT',
  REJECT = 'REJECT',
  ADJUST = 'ADJUST'
}

export enum ApprovalDecision {
  APPROVE = 'APPROVE',
  REJECT = 'REJECT',
  REREVIEW = 'REREVIEW'
}

export enum LicenseStatus {
  ACTIVE = 'ACTIVE',
  CANCELLED = 'CANCELLED'
}

export type CurrentUser = {
  id: string;
  fullName: string;
  tinNumber?: string | null;
  email: string;
  region?: string | null;
  businessAddress?: string | null;
  legalCondition?: string | null;
  bankAccountNumber?: string | null;
  role: UserRole;
};

export type AuthResponse = {
  token: string;
  user: CurrentUser;
};

export type TradeLicenseApplication = {
  applicationId: string;
  applicantId: string;
  fullName: string;
  tradeName: string;
  nationalIdNumber: string;
  email: string;
  phoneNumber: string;
  tradeLicenseType: string;
  commodity: string;
  bankAccountNumber: string;
  status: ApplicationStatus;
};

export type TradeLicense = {
  licenseId: string;
  licenseNumber: string;
  sourceApplicationId: string;
  licenseHolderId: string;
  fullName: string;
  nationalIdNumber: string;
  email: string;
  phoneNumber: string;
  tinNumber: string;
  tradeName: string;
  tradeLicenseType: string;
  commodity: string;
  validFrom: string;
  validTo: string;
  issuedDate: string;
  status: LicenseStatus;
};

export type CreateApplicationRequest = {
  applicantId: string;
  fullName: string;
  tradeName: string;
  nationalIdNumber: string;
  email: string;
  phoneNumber: string;
  tradeLicenseType: string;
  commodity: string;
  bankAccountNumber: string;
};

export type FileMetadata = {
  id: string;
  applicationId: string;
  documentId?: string | null;
  kind: 'DOCUMENT' | 'PAYMENT_SLIP';
  documentType: string;
  originalFileName: string;
  contentType: string;
  size: number;
  uploadedAt: string;
};

export type AuditEvent = {
  id: string;
  applicationId: string;
  action: string;
  status: ApplicationStatus;
  actorId?: string | null;
  actorRole?: UserRole | null;
  comment?: string | null;
  occurredAt: string;
};

export type LicenseTypeConfig = {
  id: string;
  code: string;
  name: string;
  requiredDocuments: string[];
  active: boolean;
  updatedAt: string;
};

export type StatusSummary = {
  status: ApplicationStatus;
  count: number;
};
