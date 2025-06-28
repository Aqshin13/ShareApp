import { Navigate, Outlet } from "react-router";
import Navbar from "../components/Navbar";

const ProtectedLayout = () => {
  const isAuthenticated = localStorage.getItem("token");

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  return (
    <div className="container-sm">
      <Outlet />
      <Navbar />
    </div>
  );
};

export const PublicLayout = () => {
  const isAuthenticated = localStorage.getItem("token");

  return isAuthenticated ? <Navigate to="/home" replace /> : <Outlet />;
};

export default ProtectedLayout;
