import { createRoot } from "react-dom/client";
import "./style.scss";
import "./style.js"
import Signup from "./pages/Signup/index.jsx";
import { RouterProvider } from "react-router";
import router from "./router/index.jsx";
import { AuthenticationContext } from "./context/index.jsx";

createRoot(document.getElementById("root")).render(
    <RouterProvider router={router} />
);
