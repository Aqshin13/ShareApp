import { createBrowserRouter } from "react-router";
import Signup from "../pages/Signup";
import Activation from "../pages/Activation";
import Login from "../pages/Login";
import Home from "../pages/Home";
import App from "../App";
import ProtectedLayout from "../security";
import { PublicLayout } from "../security";
import Friends from "../pages/Friends";
import Profile from "../pages/Profile";
export default createBrowserRouter([
  {
    path: "/",
    Component: App,
    children: [
      {
        element: <PublicLayout />,
        children: [
          {
            path: "/",
            index: true,
            Component: Signup,
          },
          {
            path: "/activation/:token",
            Component: Activation,
          },
          {
            path: "/login",
            Component: Login,
          },
          {
            path: "*",
            Component: Login,
          },
        ],
      },
      {
        element: <ProtectedLayout />,
        children: [
          { path: "home", Component: Home },
          { path: "*", Component: Home },
          { path: "friends", Component: Friends },
          { path: "myprofile/:id", Component: Profile },
        ],
      },
      {
        path: "*",
        Component: Login,
      },
    ],
  },
]);
