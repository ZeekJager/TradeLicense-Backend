import { api } from './api';
import { TradeLicense, UserRole } from '../types/domain';

export const licensesApi = api.injectEndpoints({
  endpoints: (builder) => ({
    myTradeLicenses: builder.query<TradeLicense[], void>({
      query: () => '/trade-licenses/mine',
      providesTags: ['License']
    }),
    verifyTradeLicense: builder.query<TradeLicense, string>({
      query: (licenseId) => `/trade-licenses/verify/${licenseId}`,
      providesTags: (_result, _error, id) => [{ type: 'License', id }]
    }),
    tradeLicenseByApplication: builder.query<TradeLicense, string>({
      query: (applicationId) => `/trade-licenses/application/${applicationId}`,
      providesTags: (_result, _error, applicationId) => [{ type: 'License', id: applicationId }]
    }),
    updateTradeLicense: builder.mutation<TradeLicense, {
      licenseId: string;
      actorId: string;
      role: UserRole;
      tradeLicenseType: string;
      commodity: string;
    }>({
      query: ({ licenseId, ...body }) => ({
        url: `/trade-licenses/${licenseId}`,
        method: 'PATCH',
        body
      }),
      invalidatesTags: ['License']
    }),
    cancelTradeLicense: builder.mutation<TradeLicense, string>({
      query: (licenseId) => ({
        url: `/trade-licenses/${licenseId}/cancel`,
        method: 'PATCH'
      }),
      invalidatesTags: ['License']
    }),
    renewTradeLicense: builder.mutation<TradeLicense, string>({
      query: (licenseId) => ({
        url: `/trade-licenses/${licenseId}/renew`,
        method: 'PATCH'
      }),
      invalidatesTags: ['License']
    })
  })
});

export const {
  useCancelTradeLicenseMutation,
  useMyTradeLicensesQuery,
  useLazyVerifyTradeLicenseQuery,
  useRenewTradeLicenseMutation,
  useTradeLicenseByApplicationQuery,
  useUpdateTradeLicenseMutation
} = licensesApi;
