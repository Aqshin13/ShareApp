import axios from "axios";
import React, { useEffect, useState } from "react";
import Input from "../../components/Input";
import { signUp } from "./http";
import { Link } from "react-router";

function Signup() {
  const [username, setUsername] = useState();
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();
  const [successMessage, setSuccessMessage] = useState();
  const [errors, setErrors] = useState({});
  const [generalError, setGeneralError] = useState();
  const [apiProgress, setApiProgress] = useState(false);

  const signup = async (e) => {
    e.preventDefault();
    setApiProgress(true);
    try {
      const result = await signUp({ username, email, password });
      setSuccessMessage(result.data.message);
    } catch (error) {
      if (error.response.data?.validationError) {
        setErrors(error.response.data?.validationError);
      } else {
        setGeneralError("Unexpected Error.Try Again!");
      }
    } finally {
      setApiProgress(false);
    }
  };

  useEffect(() => {
    setErrors((prevError) => {
      return {
        ...prevError,
        username: undefined,
      };
    });
  }, [username]);

  useEffect(() => {
    setErrors((prevError) => {
      return {
        ...prevError,
        email: undefined,
      };
    });
  }, [email]);

  useEffect(() => {
    setErrors((prevError) => {
      return {
        ...prevError,
        password: undefined,
      };
    });
  }, [password]);

  const isExist = password && username && email;

  return (
    <div className="h-100 d-flex justify-content-center align-items-center">
      <form className="card p-4  shadow-lg" onSubmit={signup}>
        <Input
          id="username"
          name="username"
          type="text"
          labelName="username"
          onChange={(e) => setUsername(e.target.value)}
          error={errors.username}
        />
        <Input
          id="email"
          name="email"
          type="email"
          labelName="E-mail"
          onChange={(e) => setEmail(e.target.value)}
          error={errors.email}
        />
        <Input
          id="password"
          name="password"
          type="password"
          labelName="password"
          onChange={(e) => setPassword(e.target.value)}
          error={errors.password}
        />

        <div className="d-grid gap-2 mb-3">
          <button
            type="submit"
            name="signup"
            id="signup"
            className="btn btn-primary"
            disabled={apiProgress || !isExist}
          >
            Sign Up{" "}
            {apiProgress && (
              <div
                className="spinner-border spinner-border-sm text-light"
                role="status"
              ></div>
            )}
          </button>
        </div>


          <Link className="text-center" to={"/login"}>Do you have an account already?</Link>


        {successMessage && (
          <div className="alert alert-success mt-3 text-center" role="alert">
            <strong>{successMessage}</strong>
          </div>
        )}

        {generalError && (
          <div className="alert alert-danger mt-3 text-center" role="alert">
            <strong>{generalError}</strong>
          </div>
        )}
      </form>
    </div>
  );
}

export default Signup;
