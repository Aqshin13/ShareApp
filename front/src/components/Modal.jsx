import React from "react";
import { useRef, useState } from "react";
function Modal(props) {
  const {
    show,
    showFunction,
    share,
    formData,
    newImage,
    setNewImage,
    apiProgress,
  } = props;
  const inputRef = useRef();

  const clearInput = (e) => {
    e.preventDefault();
    if (inputRef.current) {
      inputRef.current.value = null;
    }
    setNewImage();
  };

  const loadImage = (event) => {
    if (event.target.files.length < 1) {
      return;
    }
    formData.current.delete("file");

    const file = event.target.files[0];
    const fileReader = new FileReader();
    fileReader.onloadend = () => {
      setNewImage(fileReader.result);
      formData.current.append("file", file);
    };
    fileReader.readAsDataURL(file);
  };

  let className = "modal fade";
  let classNameBG = "";
  if (show) {
    className += " show d-block";
    classNameBG += "modal-backdrop fade show";
  }

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
                  showFunction(false);
                  clearInput(e);
                }}
              ></button>
            </div>
            <div className="modal-body">
              <input
                type="file"
                class="form-control"
                id="shareImage"
                ref={inputRef}
                aria-label="Upload"
                onChange={(e) => {
                  loadImage(e);
                }}
              />

              {newImage && (
                <div className="mt-3 w-100">
                  <img className="w-100 img-fluid" src={newImage} />
                </div>
              )}
            </div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                data-bs-dismiss="modal"
                onClick={(e) => {
                  showFunction(false);
                  clearInput(e);
                }}
              >
                Close
              </button>
              <button
                class="btn btn-outline-secondary"
                type="button"
                onClick={share}
                disabled={apiProgress}
              >
                Share{" "}
                {apiProgress && (
                  <div
                    className="spinner-border spinner-border-sm text-light"
                    role="status"
                  ></div>
                )}
              </button>
            </div>
          </div>
        </div>
      </div>
      {classNameBG && <div className={classNameBG}></div>}
    </>
  );
}

export default Modal;
