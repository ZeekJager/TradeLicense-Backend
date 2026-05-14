import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { AuthResponse, CurrentUser } from '../../types/domain';

type AuthState = {
  token: string | null;
  user: CurrentUser | null;
};

function storage() {
  if (
    typeof globalThis !== 'undefined'
    && 'localStorage' in globalThis
    && typeof globalThis.localStorage?.getItem === 'function'
  ) {
    return globalThis.localStorage;
  }
  return null;
}

const storedToken = storage()?.getItem('tlw.token') ?? null;
const storedUser = storage()?.getItem('tlw.user') ?? null;

const initialState: AuthState = {
  token: storedToken,
  user: storedUser ? JSON.parse(storedUser) as CurrentUser : null
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials(state, action: PayloadAction<AuthResponse>) {
      state.token = action.payload.token;
      state.user = action.payload.user;
      storage()?.setItem('tlw.token', action.payload.token);
      storage()?.setItem('tlw.user', JSON.stringify(action.payload.user));
    },
    setUser(state, action: PayloadAction<CurrentUser>) {
      state.user = action.payload;
      storage()?.setItem('tlw.user', JSON.stringify(action.payload));
    },
    logout(state) {
      state.token = null;
      state.user = null;
      storage()?.removeItem('tlw.token');
      storage()?.removeItem('tlw.user');
    }
  }
});

export const { logout, setCredentials, setUser } = authSlice.actions;
export default authSlice.reducer;
