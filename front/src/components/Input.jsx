import React from "react";

function Input(props) {
  const { labelName, error, onChange, type, name, id } = props;

  return (
    <div className="mb-3">
      <label htmlFor="username" className="form-label">
        {labelName}
      </label>
      <input
        type={type}
        className={error ? "form-control is-invalid" : "form-control"}
        name={name}
        id={id}
        onChange={onChange}
      />
      {error && <div class="invalid-feedback">{error}</div>}
    </div>
  );
}

export default Input;
