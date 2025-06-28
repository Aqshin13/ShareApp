import React, { useEffect, useState } from "react";
import axios from "axios";
import Input from "../../components/Input";
import { useAuthDispatch } from "../../context";
import { Link, useNavigate } from "react-router";
function Login() {
  const [username, setUsername] = useState();
  const [password, setPassword] = useState();
  const [apiProgress, setApiProgress] = useState();
  const [generalError, setGeneralError] = useState();

  const dispatch = useAuthDispatch();
  const navigate = useNavigate();

  const login = async (e) => {
    e.preventDefault();
    setGeneralError();
    setApiProgress(true);

    try {
      setApiProgress(true);
      const result = await axios.post("/api/v1/auth/login", {
        username,
        password,
      });

      dispatch({ type: "login-success", data: result.data });
      navigate("/home");
      console.log(result);
    } catch (error) {
      console.log(error);

      setGeneralError(error.response.data.message);
    } finally {
      setApiProgress(false);
    }
  };

  const isExist = username && password;

  useEffect(() => {
    setGeneralError();
  }, [username, password]);

  return (
    <div className="h-100 d-flex justify-content-center align-items-center">
      <form className="card p-4  shadow-lg" onSubmit={login}>
        <Input
          id="username"
          name="username"
          type="text"
          labelName="username"
          onChange={(e) => setUsername(e.target.value)}
        />

        <Input
          id="password"
          name="password"
          type="password"
          labelName="password"
          onChange={(e) => setPassword(e.target.value)}
        />

        <div className="d-grid gap-2">
          <button
            type="submit"
            name="signup"
            id="signup"
            className="btn btn-primary"
            disabled={apiProgress || !isExist}
          >
            Login{" "}
            {apiProgress && (
              <div
                className="spinner-border spinner-border-sm text-light"
                role="status"
              ></div>
            )}
          </button>
        </div>


       <div className="text-center mt-2">
         <Link to={"/"}>Didn't you have an account yet?</Link>
       </div>

        {generalError && (
          <div className="alert alert-danger mt-3 text-center" role="alert">
            <strong>{generalError}</strong>
          </div>
        )}
      </form>
    </div>
  );
}

export default Login;
