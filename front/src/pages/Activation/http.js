import axios from "axios";

export const activeUser = async (token) => {
  const result = await axios.patch("/api/v1/users/activation/" + token);

  return result;
};
