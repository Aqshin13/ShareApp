import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router";
import { activeUser } from "./http";
function Activation() {
  const param = useParams();
  const token = param["token"];
  const [successMessage, setSuccessMessage] = useState();
  const [error, setError] = useState();

  useEffect(() => {
    async function doActive() {
      try {
        const result = await activeUser(token);
        setSuccessMessage(result.data.message);
      } catch (error) {
        console.log(error);
        setError(error.response.data.message);
      }
    }

    doActive();
  }, [token]);

  return (
    <div className="h-100 d-flex justify-content-center align-items-center">
      {successMessage && (
        <div class="alert alert-success" role="alert">
          <strong>{successMessage}</strong>
          <Link to={"/login"}>Got to Login page</Link>
        </div>
      )}

      {error && (
        <div class="alert alert-danger" role="alert">
          <strong>{error}</strong>
        </div>
      )}
    </div>
  );
}

export default Activation;
