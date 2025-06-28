import { Link } from "react-router";
import "../styles/main.css";
import { useAuthState } from "../context";

function Navbar() {

  const loggedInUser=useAuthState()


  return (
    <>
      <nav class="my-navbar fixed-bottom container-sm p-0 navbar navbar-expand bg-body-tertiary">
        <div class="container-sm">
          <ul class="navbar-nav  mb-2 mb-lg-0 w-100 d-flex justify-content-around align-items-center">
            <li class="nav-item">
              <Link to={"/myprofile/"+loggedInUser.id} class="nav-link" href="#">
                <i class="fa-solid fa-user"></i>
              </Link>
            </li>
            <li class="nav-item">
              <Link to={"/home"} class="nav-link active " href="#">
                <i class="fa-regular fa-image"></i>
              </Link>
            </li>

            <li class="nav-item">
              <Link to={"/friends"} class="nav-link" href="#">
                <i class="fa-solid fa-user-group"></i>
              </Link>
            </li>
          </ul>
        </div>
      </nav>
    </>
  );
}

export default Navbar;
