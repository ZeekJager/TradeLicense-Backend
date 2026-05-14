import { api } from './api';
import {
  ApplicationStatus,
  ApprovalDecision,
  AuditEvent,
  CreateApplicationRequest,
  FileMetadata,
  LicenseTypeConfig,
  ReviewDecision,
  TradeLicenseApplication,
  UserRole
} from '../types/domain';

export const applicationsApi = api.injectEndpoints({
  endpoints: (builder) => ({
    licenseTypes: builder.query<LicenseTypeConfig[], void>({
      query: () => '/license-types',
      providesTags: ['LicenseType']
    }),
    listApplications: builder.query<TradeLicenseApplication[], ApplicationStatus | void>({
      query: (status) => status ? `/trade-license-applications?status=${status}` : '/trade-license-applications',
      providesTags: ['Application']
    }),
    getApplication: builder.query<TradeLicenseApplication, string>({
      query: (applicationId) => `/trade-license-applications/${applicationId}`,
      providesTags: (_result, _error, id) => [{ type: 'Application', id }]
    }),
    createApplication: builder.mutation<TradeLicenseApplication, CreateApplicationRequest>({
      query: (body) => ({ url: '/trade-license-applications', method: 'POST', body }),
      invalidatesTags: ['Application', 'StatusSummary']
    }),
    uploadDocument: builder.mutation<TradeLicenseApplication, { applicationId: string; documentType: string; file: File }>({
      query: ({ applicationId, documentType, file }) => {
        const body = new FormData();
        body.append('documentType', documentType);
        body.append('file', file);
        return { url: `/trade-license-applications/${applicationId}/documents/upload`, method: 'POST', body };
      },
      invalidatesTags: (_result, _error, arg) => [
        { type: 'Application', id: arg.applicationId },
        { type: 'Document', id: arg.applicationId },
        { type: 'Timeline', id: arg.applicationId }
      ]
    }),
    listDocuments: builder.query<FileMetadata[], string>({
      query: (applicationId) => `/trade-license-applications/${applicationId}/documents`,
      providesTags: (_result, _error, id) => [{ type: 'Document', id }]
    }),
    uploadPaymentSlip: builder.mutation<TradeLicenseApplication, { applicationId: string; file: File }>({
      query: ({ applicationId, file }) => {
        const body = new FormData();
        body.append('file', file);
        return { url: `/trade-license-applications/${applicationId}/payment/slip`, method: 'POST', body };
      },
      invalidatesTags: (_result, _error, arg) => [
        { type: 'Application', id: arg.applicationId },
        { type: 'Payment', id: arg.applicationId },
        { type: 'Timeline', id: arg.applicationId }
      ]
    }),
    paymentSlip: builder.query<FileMetadata, string>({
      query: (applicationId) => `/trade-license-applications/${applicationId}/payment/slip`,
      providesTags: (_result, _error, id) => [{ type: 'Payment', id }]
    }),
    submitApplication: builder.mutation<TradeLicenseApplication, { applicationId: string; actorId: string; role: UserRole }>({
      query: ({ applicationId, ...body }) => ({ url: `/trade-license-applications/${applicationId}/submit`, method: 'POST', body }),
      invalidatesTags: (_result, _error, arg) => [
        'Application',
        'StatusSummary',
        { type: 'Timeline', id: arg.applicationId }
      ]
    }),
    resubmitApplication: builder.mutation<TradeLicenseApplication, { applicationId: string; actorId: string; role: UserRole }>({
      query: ({ applicationId, ...body }) => ({ url: `/trade-license-applications/${applicationId}/resubmit`, method: 'POST', body }),
      invalidatesTags: (_result, _error, arg) => [
        'Application',
        'StatusSummary',
        { type: 'Timeline', id: arg.applicationId }
      ]
    }),
    cancelApplication: builder.mutation<TradeLicenseApplication, { applicationId: string; actorId: string; role: UserRole }>({
      query: ({ applicationId, ...body }) => ({ url: `/trade-license-applications/${applicationId}/cancel`, method: 'POST', body }),
      invalidatesTags: ['Application', 'StatusSummary']
    }),
    pendingReview: builder.query<TradeLicenseApplication[], void>({
      query: () => '/trade-license-reviews/pending',
      providesTags: ['Application']
    }),
    reviewApplication: builder.mutation<TradeLicenseApplication, { applicationId: string; reviewerId: string; role: UserRole; decision: ReviewDecision; comment: string }>({
      query: ({ applicationId, ...body }) => ({ url: `/trade-license-reviews/${applicationId}`, method: 'POST', body }),
      invalidatesTags: (_result, _error, arg) => [
        'Application',
        'StatusSummary',
        { type: 'Timeline', id: arg.applicationId }
      ]
    }),
    pendingApproval: builder.query<TradeLicenseApplication[], void>({
      query: () => '/trade-license-approvals/pending',
      providesTags: ['Application']
    }),
    approveApplication: builder.mutation<TradeLicenseApplication, {
      applicationId: string;
      approverId: string;
      role: UserRole;
      decision: ApprovalDecision;
      comment: string;
      licenseNumber: string;
      tinNumber: string;
      licenseTypeToIssue: string;
    }>({
      query: ({ applicationId, ...body }) => ({ url: `/trade-license-approvals/${applicationId}`, method: 'POST', body }),
      invalidatesTags: (_result, _error, arg) => [
        'Application',
        'StatusSummary',
        { type: 'Timeline', id: arg.applicationId }
      ]
    }),
    timeline: builder.query<AuditEvent[], string>({
      query: (applicationId) => `/trade-license-applications/${applicationId}/timeline`,
      providesTags: (_result, _error, id) => [{ type: 'Timeline', id }]
    })
  })
});

export const {
  useApproveApplicationMutation,
  useCancelApplicationMutation,
  useCreateApplicationMutation,
  useGetApplicationQuery,
  useLicenseTypesQuery,
  useListApplicationsQuery,
  useListDocumentsQuery,
  usePaymentSlipQuery,
  usePendingApprovalQuery,
  usePendingReviewQuery,
  useResubmitApplicationMutation,
  useReviewApplicationMutation,
  useSubmitApplicationMutation,
  useTimelineQuery,
  useUploadDocumentMutation,
  useUploadPaymentSlipMutation
} = applicationsApi;
