import { api, API_BASE_URL } from './api';
import { ApplicationStatus, CurrentUser, LicenseTypeConfig, StatusSummary, TradeLicenseApplication, UserRole } from '../types/domain';

export const adminApi = api.injectEndpoints({
  endpoints: (builder) => ({
    users: builder.query<CurrentUser[], void>({
      query: () => '/admin/users',
      providesTags: ['User']
    }),
    createUser: builder.mutation<CurrentUser, { fullName: string; email: string; password: string; role: UserRole }>({
      query: (body) => ({ url: '/admin/users', method: 'POST', body }),
      invalidatesTags: ['User']
    }),
    updateUserRole: builder.mutation<CurrentUser, { userId: string; role: UserRole }>({
      query: ({ userId, role }) => ({ url: `/admin/users/${userId}/role`, method: 'PUT', body: { role } }),
      invalidatesTags: ['User']
    }),
    adminApplications: builder.query<TradeLicenseApplication[], ApplicationStatus | void>({
      query: (status) => status ? `/admin/applications?status=${status}` : '/admin/applications',
      providesTags: ['Application']
    }),
    statusSummary: builder.query<StatusSummary[], void>({
      query: () => '/admin/applications/status-summary',
      providesTags: ['StatusSummary']
    }),
    adminLicenseTypes: builder.query<LicenseTypeConfig[], void>({
      query: () => '/admin/license-types',
      providesTags: ['LicenseType']
    }),
    createLicenseType: builder.mutation<LicenseTypeConfig, { code: string; name: string; requiredDocuments: string[] }>({
      query: (body) => ({ url: '/admin/license-types', method: 'POST', body }),
      invalidatesTags: ['LicenseType']
    }),
    updateLicenseType: builder.mutation<LicenseTypeConfig, { id: string; name: string; requiredDocuments: string[]; active: boolean }>({
      query: ({ id, ...body }) => ({ url: `/admin/license-types/${id}`, method: 'PUT', body }),
      invalidatesTags: ['LicenseType']
    })
  })
});

export function applicationReportUrl() {
  return `${API_BASE_URL}/admin/reports/applications.csv`;
}

export const {
  useAdminApplicationsQuery,
  useAdminLicenseTypesQuery,
  useCreateLicenseTypeMutation,
  useCreateUserMutation,
  useStatusSummaryQuery,
  useUpdateLicenseTypeMutation,
  useUpdateUserRoleMutation,
  useUsersQuery
} = adminApi;
