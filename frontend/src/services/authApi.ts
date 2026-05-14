import { api } from './api';
import { AuthResponse, CurrentUser } from '../types/domain';
import { logout, setCredentials, setUser } from '../features/auth/authSlice';

export const authApi = api.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation<AuthResponse, { email: string; password: string }>({
      query: (body) => ({ url: '/auth/login', method: 'POST', body }),
      async onQueryStarted(_arg, { dispatch, queryFulfilled }) {
        const { data } = await queryFulfilled;
        dispatch(setCredentials(data));
      }
    }),
    register: builder.mutation<AuthResponse, {
      tinNumber: string;
      fullName: string;
      email: string;
      region: string;
      businessAddress: string;
      password: string;
      legalCondition: string;
      bankAccountNumber: string;
    }>({
      query: (body) => ({ url: '/auth/register', method: 'POST', body }),
      async onQueryStarted(_arg, { dispatch, queryFulfilled }) {
        const { data } = await queryFulfilled;
        dispatch(setCredentials(data));
      }
    }),
    me: builder.query<CurrentUser, void>({
      query: () => '/auth/me',
      async onQueryStarted(_arg, { dispatch, queryFulfilled }) {
        const { data } = await queryFulfilled;
        dispatch(setUser(data));
      }
    }),
    logout: builder.mutation<{ message: string }, void>({
      query: () => ({ url: '/auth/logout', method: 'POST' }),
      async onQueryStarted(_arg, { dispatch, queryFulfilled }) {
        try {
          await queryFulfilled;
        } finally {
          dispatch(logout());
        }
      }
    })
  })
});

export const { useLoginMutation, useLogoutMutation, useMeQuery, useRegisterMutation } = authApi;
