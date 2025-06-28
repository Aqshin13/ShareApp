import React, { useEffect, useRef } from "react";
import { useState } from "react";
import http from "../../http/index";
import { useAuthDispatch, useAuthState } from "../../context";
import { useParams } from "react-router";
import profileImage from "../../assets/default.jpg";
import { useNavigate } from "react-router";

function Profile() {
  const param = useParams();
  const id = param["id"];
  const [user, setUser] = useState();
  const [fileLoadErrorMessage, setFileErrorMessage] = useState();
  const auth = useAuthState();
  const navigate = useNavigate();
  const [newImage, setNewImage] = useState();
  const dispatch = useAuthDispatch();
  const formData = useRef(new FormData());
  const [apiProgressForShare, setApiPorgressForShare] = useState(false);
  const [apiProgressForDelete, setApiPorgressForDelete] = useState(false);





  const getUser = async () => {
    const result = await http.get("/api/v1/users/id/" + id);
    setUser(result.data);
  };
  const closeToast = (e) => {
    e.preventDefault();
    setFileErrorMessage(null);
  };

  const loadImage = (event) => {
    if (event.target.files.length < 1) {
      return;
    }
    const file = event.target.files[0];
    const fileReader = new FileReader();
    fileReader.onloadend = () => {
      setNewImage(fileReader.result);
      formData.current = new FormData();
      formData.current.append("file", file);
    };
    fileReader.readAsDataURL(file);
  };
  setTimeout(() => {
    setFileErrorMessage(null);
  }, 7000);

  const share = async () => {
    setApiPorgressForShare(true);
    try {
      const result = await http.put(
        "/api/v1/users/" + auth.id,
        formData.current
      );
    } catch (error) {
      setFileErrorMessage(error?.response?.data?.message);
    } finally {
      setApiPorgressForShare(false);
      getUser();
    }
  };

  const logout = (e) => {
    e.preventDefault();
    dispatch({ type: "logout-success" });
    navigate("/login");
  };

  const clearInput = () => {
    setNewImage(null);
    formData.current.delete("file");
  };

  useEffect(() => {
    getUser();
  }, [id]);

  const deleteAccount = () => {
    setApiPorgressForDelete(true);
    try {
      const result = http.delete("/api/v1/users/" + auth.id);
      dispatch({ type: "logout-success" });
      navigate("/login");
    } catch (error) {
      console.log(error);
    } finally {
      setApiPorgressForDelete(false);
    }
  };

  const image =
    newImage ||
    (user?.profileImageUrl
      ? `/profile/${user.profileImageUrl}`
      : profileImage) ||
    profileImage;

  return (
    <>
      {user ? (
        <div className="container pb-5">
          <div className="row">
            <div className="col-sm-12 offset-sm-0 col-md-8 offset-md-2 col-lg-6 offset-lg-3  text-center">
              <div className="card">
                <img
                  className="card-img-top w-100"
                  style={{ height: "400px" }}
                  src={image}
                  alt=""
                />

                <h3 className="card-header">
                  {user ? user.username : "Loading"}
                </h3>

                {id == auth.id && (
                  <>
                    <div className="card-body">
                      <div className="input-group">
                        <input
                          className="form-control"
                          type="file"
                          name="file"
                          id="file"
                          onChange={(e) => {
                            loadImage(e);
                          }}
                        />
                        <button
                          class="btn btn-outline-primary"
                          type="button"
                          onClick={share}
                          disabled={apiProgressForShare}
                        >
                          Upload{" "}
                          {apiProgressForShare && (
                            <div
                              className="spinner-border spinner-border-sm text-light"
                              role="status"
                            ></div>
                          )}
                        </button>
                        <button
                          class="btn btn-outline-danger"
                          type="button"
                          onClick={clearInput}
                        >
                          Cancel
                        </button>
                      </div>
                    </div>

                    <div className="card-footer d-flex justify-content-between">
                      <button
                        className="btn btn-warning"
                        onClick={(e) => logout(e)}
                      >
                        Logout{" "}
                      </button>

                      <button
                        onClick={() => deleteAccount()}
                        className="btn btn-danger"
                        disabled={apiProgressForDelete}
                      >
                        Delete Account{" "}
                        {apiProgressForDelete && (
                          <div
                            className="spinner-border spinner-border-sm text-light"
                            role="status"
                          ></div>
                        )}
                      </button>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>

          <div class="toast-container position-fixed bottom-0 end-0 p-3">
            <div
              id="liveToast"
              className={fileLoadErrorMessage ? "toast fade show" : "toast"}
              role="alert"
              aria-live="assertive"
              aria-atomic="true"
            >
              <div class="toast-header">
                <strong class="me-auto">Error</strong>
                <button
                  type="button"
                  class="btn-close"
                  onClick={(e) => closeToast(e)}
                ></button>
              </div>
              <div class="toast-body bg-danger">{fileLoadErrorMessage}</div>
            </div>
          </div>
        </div>
      ) : (
        <div className="alert alert-danger text-center">User is not found</div>
      )}
    </>
  );
}

export default Profile;
