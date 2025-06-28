import React, { useEffect, useState } from "react";
import profileImage from "../../assets/default.jpg";

import http from "../../http";
import { useAuthState } from "../../context";

function Friends() {
  const [users, setUsers] = useState({
    content: [],
    number: 0,
  });

  const [followStatus, setFollowStatus] = useState([]);

  const loggedInUserId = useAuthState();

  const getFriends = async (page = 0, size = 6) => {
    const result = await http.get("/api/v1/users/all", {
      params: { id: loggedInUserId.id, page: page, size: size },
    });

    console.log(result);

    setUsers(result.data);
  };

  const getFollowStatus = async (userId) => {
    const status = await http.get("api/v1/friends/follow-status/" + userId);
    setFollowStatus(status.data);
  };

  const checkStatus = (id) => {
    const found = followStatus.find((f) => f.following.id === id);
    return found ? found.status : null;
  };

  const next = () => {
    getFriends(users.number + 1);
  };

  const previous = () => {
    getFriends(users.number - 1);
  };

  const followUser = async (user_id) => {
    try {
      const follow = await http.post("/api/v1/friends/follow/" + user_id);
    } catch (e) {
      console.log(e);
    } finally {
      await getFollowStatus(loggedInUserId.id);
    }
  };

  const unFollow = async (user_id) => {
    try {
      const follow = await http.delete("/api/v1/friends/unfollow/" + user_id);
    } catch (e) {
      console.log(e);
    } finally {
      await getFollowStatus(loggedInUserId.id);
    }
  };

  const [followRequest, setFollowRequest] = useState([]);

  const loggedIdUser = useAuthState();

  const getRequest = async () => {
    const resultFollow = await http.get(
      "/api/v1/friends/follow-request/" + loggedIdUser.id
    );
    setFollowRequest(resultFollow.data);
  };

  const acceptFollow = async (id) => {
    await http.post("/api/v1/friends/accept/" + id);
    getRequest();
  };

  const rejectFollow = async (id) => {
    await http.delete("/api/v1/friends/reject/" + id);
    getRequest();
  };
  const { last, first } = users;

  useEffect(() => {
    getFriends();
    getRequest();
    getFollowStatus(loggedInUserId.id);
  }, []);

  return (
    <div className="container p-3 pb-5">
      <div className="row">
        <div className="col-12  col-md-8 col-lg-9 order-2 order-md-1">
          <div className="row g-5">
            {users.content.map((user) => {
              const st = checkStatus(user.id);
              let button;
              if (st === "ACCEPTED") {
                button = (
                  <button
                    className="btn btn-danger"
                    onClick={() => unFollow(user.id)}
                  >
                    Remove
                  </button>
                );
              } else if (st === "PENDING") {
                button = (
                  <button
                    className="btn btn-warning"
                    onClick={() => unFollow(user.id)}
                  >
                    PENDING
                  </button>
                );
              } else {
                button = (
                  <button
                    className="btn btn-primary"
                    onClick={() => followUser(user.id)}
                  >
                    Follow
                  </button>
                );
              }
              return (
                <div className="col-sm-12 col-md-6 col-lg-4">
                  <div className="card" style={{ height: "400px" }}>
                    <div style={{ height: "300px" }}>
                      <img
                        src={
                          user.profileImageUrl
                            ? "/profile/" + user.profileImageUrl
                            : profileImage
                        }
                        class="card-img-top w-100 h-100"
                        alt="..."
                      ></img>
                    </div>
                    <div class="card-body">
                      <h5 class="card-title">{user.username}</h5>
                      {button}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {followRequest.length>0 && (
          <div
            className="col-12  col-md-4 col-lg-3 card p-1 order-1 order-md-2 mb-3 mb-md-0 overflow-auto"
            style={{ maxHeight: "300px" }}
          >
            <h3 className="card-header text-center mb-2">Request</h3>
            <ul class="list-group ">
              {followRequest.map((follows) => {
                return (
                  <div className="list-group-item d-flex  justify-content-between align-items-center">
                    <span>{follows.follower.username}</span>

                    <div className="d-flex">
                      <button
                        onClick={() => acceptFollow(follows.id)}
                        className="btn btn-success"
                      >
                        Accept
                      </button>
                      <button
                        onClick={() => rejectFollow(follows.id)}
                        className="btn btn-danger"
                      >
                        Reject
                      </button>
                    </div>
                  </div>
                );
              })}
            </ul>
          </div>
        )}
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
    </div>
  );
}

export default Friends;
