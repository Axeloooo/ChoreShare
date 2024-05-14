import { PayloadAction, createSlice } from "@reduxjs/toolkit";

export type Auth = {
  userId: string | null;
  token: string | null;
};

const auth: Auth = {
  userId: null,
  token: null,
};

const authSlice = createSlice({
  name: "auth",
  initialState: auth,
  reducers: {
    setAuth(state, action: PayloadAction<Auth>) {
      state.userId = action.payload.userId;
      state.token = action.payload.token;
    },
    clearAuth(state) {
      state.userId = null;
      state.token = null;
    },
  },
});

export const { setAuth, clearAuth } = authSlice.actions;

export default authSlice.reducer;
