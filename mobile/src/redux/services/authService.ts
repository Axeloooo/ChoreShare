import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

type AuthRegister = {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  username: string;
  password: string;
};

type AuthLogin = {
  username: string;
  password: string;
};

type AuthToken = {
  token: string;
};

const authApi = createApi({
  reducerPath: "authApi",
  baseQuery: fetchBaseQuery({ baseUrl: "http://localhost:8888" }),
  endpoints: (builder) => ({
    postRegister: builder.mutation<AuthToken, AuthRegister>({
      query: (user) => ({
        url: "/api/v1/auth/register",
        method: "POST",
        body: user,
      }),
    }),
    postLogin: builder.mutation<AuthToken, AuthLogin>({
      query: (user) => ({
        url: "/api/v1/auth/login",
        method: "POST",
        body: user,
      }),
    }),
  }),
});

export default authApi;

export const { usePostRegisterMutation, usePostLoginMutation } = authApi;
