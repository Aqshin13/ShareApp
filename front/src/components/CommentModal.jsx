import React, { useEffect } from "react";
import { useRef, useState } from "react";
import http from "../http";
function CommentModal(props) {
  const { showComment, showCommentModal, selectedShareId: shareId } = props;
  const [text, setText] = useState("");
  const [comment, setComment] = useState([]);
  const clearInput = (e) => {
    e.preventDefault();
    setText("");
  };

  let className = "modal fade";
  let classNameBG = "";
  if (showComment) {
    className += " show d-block";
    classNameBG += "modal-backdrop fade show";
  }

  const addComment = async () => {
    const result = await http.post("/api/v1/comments/shares", { text, shareId });
    console.log(result);
    getCommentForShare(shareId);
    setText("")
  };

  const getCommentForShare = async (shareId) => {
    const comments = await http.get("/api/v1/comments/shares/" + shareId);
    setComment(comments.data);
    console.log(comments);
  };

  useEffect(() => {
    getCommentForShare(shareId);
  }, [shareId]);




  return (
    <>
      <div className={className} aria-hidden="true">
        <div className="modal-dialog d-flex h-100 justify-content-center align-items-center">
          <div className="modal-content">
            <div className="modal-header">
              <h1 className="modal-title fs-5" id="exampleModalLabel">
                Modal title
              </h1>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
                onClick={(e) => {
                  showCommentModal(false);
                  clearInput(e);
                }}
              ></button>
            </div>
            <div className="modal-body overflow-auto"  style={{ maxHeight: "300px" }}>
              

              <ul className="list-group">
                {comment.map((c) => (
                   <li className="list-group-item">
                    {c.text}
                   </li>
                ))}
              </ul>
            </div>
            <div className="modal-footer d-flex justify-content-start align-items-start">
              <input
                type="text"
                class="form-control mb-3"
                id="comment"
                onChange={(e) => {
                  setText(e.target.value);
                }}
                value={text}
              />
              <button
                type="button"
                className="btn btn-secondary"
                data-bs-dismiss="modal"
                onClick={(e) => {
                  showCommentModal(false);
                  clearInput(e);
                }}
              >
                Close
              </button>
              <button onClick={addComment} class="btn btn-outline-secondary" type="button">
                Comment
              </button>
            </div>
          </div>
        </div>
      </div>
      {classNameBG && <div className={classNameBG}></div>}
    </>
  );
}

export default CommentModal;
