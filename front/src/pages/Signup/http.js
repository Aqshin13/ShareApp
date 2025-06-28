import axios from "axios";

export const signUp = async (body) => {
  const result = await axios.post("/api/v1/users/signup", body);
  return result;
};
