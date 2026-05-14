import { describe, expect, it, beforeEach } from 'vitest';
import authReducer, { logout, setCredentials } from './authSlice';
import { UserRole } from '../../types/domain';

describe('authSlice', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it('stores credentials and clears them on logout', () => {
    const state = authReducer(undefined, setCredentials({
      token: 'token-123',
      user: {
        id: '11111111-1111-1111-1111-111111111111',
        fullName: 'Customer User',
        email: 'customer@tradelicense.test',
        role: UserRole.CUSTOMER
      }
    }));

    expect(state.token).toBe('token-123');
    expect(state.user?.role).toBe(UserRole.CUSTOMER);
    expect(localStorage.getItem('tlw.token')).toBe('token-123');

    const cleared = authReducer(state, logout());
    expect(cleared.token).toBeNull();
    expect(cleared.user).toBeNull();
  });
});
