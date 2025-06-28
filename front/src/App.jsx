import { useState } from "react";
import { AuthenticationContext } from "./context";
import { Outlet } from "react-router";

function App() {
  const[show,setShow]=useState(false)

  return (
    <>
      <AuthenticationContext>
        <Outlet />
      </AuthenticationContext>
    </>
  );
}

export default App;
