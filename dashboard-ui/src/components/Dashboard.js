import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Dashboard() {

    const [userId, setUserId] = useState("");
    const [include, setInclude] = useState("");
    const navigate = useNavigate();

    const handleFetch = async () => {

        if (!userId || userId.trim() === "") {
            alert("Please enter User ID");
            return;
        }

        try {
            const response = await axios.get(
                `http://localhost:8080/api/dashboard/${userId}`,
                {
                    params: { include }
                }
            );

            navigate("/result", { state: response.data });

        } catch (error) {
            alert("API Error");
        }
    };

    return (
        <div className="dashboard-page">
            <div className="dashboard-card">

                <h2>User Dashboard</h2>

                <div className="form-group">
                    <label>User ID</label>
                    <input
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        placeholder="Enter user id"
                    />
                </div>

                <div className="form-group">
                    <label>Include</label>
                    <input
                        value={include}
                        onChange={(e) => setInclude(e.target.value)}
                        placeholder="orders,payments"
                    />
                </div>

                <button onClick={handleFetch}>
                    Fetch Details
                </button>

            </div>
        </div>
    );
}

export default Dashboard;