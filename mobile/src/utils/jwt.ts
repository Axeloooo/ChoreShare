import base64 from "react-native-base64";

export type DecodedToken = {
  sub: string;
  iat: number;
  exp: number;
};

const base64UrlToBase64 = (input: string) => {
  let base64 = input.replace(/-/g, "+").replace(/_/g, "/");
  const paddingNeeded = (4 - (base64.length % 4)) % 4;
  for (let i = 0; i < paddingNeeded; i++) {
    base64 += "=";
  }
  return base64;
};

export const jwtDecode = (token: string): DecodedToken => {
  const parts = token.split(".");
  if (parts.length !== 3) {
    throw new Error("Invalid token");
  }

  const payload = base64.decode(base64UrlToBase64(parts[1]));
  return JSON.parse(payload);
};
