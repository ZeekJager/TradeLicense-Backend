import { ApplicationStatus, UserRole } from '../types/domain';

export const STATUS_LABELS: Record<ApplicationStatus, string> = {
  [ApplicationStatus.DRAFT]: 'Draft',
  [ApplicationStatus.PENDING_REVIEW]: 'Pending review',
  [ApplicationStatus.RETURNED_FOR_ADJUSTMENT]: 'Returned for adjustment',
  [ApplicationStatus.PENDING_APPROVAL]: 'Pending approval',
  [ApplicationStatus.APPROVED]: 'Approved',
  [ApplicationStatus.LICENSE_ISSUED]: 'License issued',
  [ApplicationStatus.REJECTED]: 'Rejected',
  [ApplicationStatus.CANCELLED]: 'Cancelled'
};

export const ROLE_HOME: Record<UserRole, string> = {
  [UserRole.CUSTOMER]: '/customer',
  [UserRole.LICENSEE]: '/customer',
  [UserRole.REVIEWER]: '/reviewer',
  [UserRole.APPROVER]: '/approver',
  [UserRole.ADMIN]: '/admin'
};

export const WORKFLOW_STEPS = ['Application', 'Documents', 'Payment', 'Submit'];
