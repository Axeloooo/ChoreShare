import { createSlice } from "@reduxjs/toolkit";

type User = {
  id: string;
  token: string;
};

const user: User | null = null;

const userSlice = createSlice({
  name: "user",
  initialState: user,
  reducers: {},
});

export default userSlice.reducer;
