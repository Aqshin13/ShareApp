import React, { useEffect, useRef, useState } from "react";
import { useAuthState } from "../../context";
import http from "../../http";
import Modal from "../../components/Modal";
import "../../styles/main.css";
import CommentModal from "../../components/CommentModal";

function Home() {
  const authState = useAuthState();
  const [data, setData] = useState({
    content: [],
    number: 0,
  });
  const [show, setShow] = useState(false);
  const [showComment, showCommentModal] = useState(false);
  const formData = useRef(new FormData());
  const [newImage, setNewImage] = useState();
  const [fileLoadErrorMessage, setFileErrorMessage] = useState();
  const [selectedShareId, setSelectedShareId] = useState();
  const [apiProgress, setApiPorgress] = useState(false);
  const auth=useAuthState()
  async function getShares(page = 0, size = 6) {
    try {
      const result = await http.get(`/api/v1/shares?page=${page}&size=${size}`);
      console.log(result);

      setData(result.data);
      formData.current.delete("file");
    } catch (error) {
      console.log(error);
    }
  }

  const share = async (e) => {
    e.preventDefault();
    setApiPorgress(true);
    try {
      const result = await http.post("/api/v1/shares/create", formData.current);
      getShares();
      setNewImage();
      setShow(false);
    } catch (error) {
      setFileErrorMessage(error?.response?.data?.message);
    } finally {
      setApiPorgress(false);
    }
  };

  useEffect(() => {
    getShares();
  }, []);

  const checkLike = (share) => {
    return share.likeResponses?.some((x) => {
      return x.userResponse.id === authState.id;
    });
  };

  const like = async (share) => {
    if (checkLike(share)) {
      await http.delete("/api/v1/likes/share/" + share.id);
      getShares();
    } else {
      try {
        await http.post("/api/v1/likes/share/" + share.id);
        getShares();
      } catch (error) {
        console.log(error);
      }
    }
  };

  const next = () => {
    getShares(data.number + 1);
  };

  setTimeout(() => {
    setFileErrorMessage(null);
  }, 7000);

  const closeToast = (e) => {
    e.preventDefault();
    setFileErrorMessage(null);
  };

  const previous = () => {
    getShares(data.number - 1);
  };

  const showCommentForShareId = (shareId) => {
    setSelectedShareId(shareId);
    showCommentModal(true);
  };

  const deleteShare = async (id) => {
    try {
      const result = await http.delete("/api/v1/shares/" + id);
    } catch (error) {
      console.log(error);
    } finally {
      getShares(data.number);
    }
  };

  const { last, first } = data;

  return (
    <div className="p-3 pb-5">
      <div className="container-sm ">
        <div
          onClick={() => setShow(true)}
          className=" create__image    d-flex justify-content-center align-items-center"
          style={{
            height: "70px",
            width: "70px",
            borderRadius: "100%",
          }}
        >
          <a type="button" className="p-3">
            <i class="fa-solid fa-square-plus"></i>
          </a>
        </div>

        <div className="row g-5">
          {data.content.map((share) => {
            return (
              <div className="col-lg-4 col-md-6 col-12">
                <div
                  className="card border-warning"
                  style={{ height: "400px", position: "relative" }}
                >
                { auth.id==share.userResponse.id && <div class="dropdown">
                    <button
                      class="btn btn-warning"
                      type="button"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                      style={{
                        color: "#FFD43B",
                        position: "absolute",
                        top: "10px",
                        left: "10px",
                      }}
                    >
                      <i class="fa-solid fa-ellipsis-vertical"></i>
                    </button>
                    <ul class="dropdown-menu">
                      <li>
                        <a
                          class="dropdown-item"
                          type="buttin"
                          onClick={(e) => deleteShare(share.id)}
                        >
                          Delete
                        </a>
                      </li>
                    </ul>
                  </div>}
                  <div style={{ height: "300px" }}>
                    <img
                      className="card-img-top w-100 h-100 "
                      src={"shares/" + share.imageUrl}
                      alt=""
                    />
                  </div>
                  <div className="card-body d-flex justify-content-between align-items-center">
                    <h5 className="card-title text-primary">
                      {share.userResponse.username}
                    </h5>

                    <div>
                      <a type="button" onClick={() => like(share)}>
                        <i
                          className={
                            checkLike(share)
                              ? "fa-regular fa-heart me-2 text-danger"
                              : "fa-regular fa-heart me-2 text-secondary"
                          }
                        ></i>
                      </a>
                      <a
                        type="button"
                        onClick={() => showCommentForShareId(share.id)}
                      >
                        <i className="fa-regular fa-comments text-secondary ms-2"></i>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>

      <div className="d-flex justify-content-center mt-2">
        <ul class="pagination ">
          {!first && (
            <li class="page-item">
              <a
                class="page-link"
                type="button"
                onClick={() => previous()}
                aria-label="Previous"
              >
                <span aria-hidden="true">&laquo;</span>
              </a>
            </li>
          )}

          {!last && (
            <li class="page-item">
              <a
                class="page-link"
                type="button"
                onClick={() => next()}
                aria-label="Next"
              >
                <span aria-hidden="true">&raquo;</span>
              </a>
            </li>
          )}
        </ul>
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

      <Modal
        show={show}
        showFunction={setShow}
        share={share}
        formData={formData}
        newImage={newImage}
        setNewImage={setNewImage}
        apiProgress={apiProgress}
      />

      <CommentModal
        showComment={showComment}
        showCommentModal={showCommentModal}
        selectedShareId={selectedShareId}
      />
    </div>
  );
}

export default Home;
