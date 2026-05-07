import React from "react";
import { useLocation } from "react-router-dom";

function Result() {

    const { state } = useLocation();

    if (!state) return <h2>No Data</h2>;

    const data = state;

    return (
        <div className="page">
            <div className="container">

                <h2>User Dashboard</h2>

                <div className="grid">

                    {/* STATUS */}
                    <div className="card status-card">
                        <h3>Status</h3>
                        <p className="success">{data.status}</p>
                        <p>{data.message}</p>
                    </div>

                    {/* USER */}
                    <div className="card">
                        <h3>User Info</h3>
                        <p><b>ID:</b> {data.data.user.id}</p>
                        <p><b>Name:</b> {data.data.user.name}</p>
                        <p><b>Email:</b> {data.data.user.email}</p>
                        <p><b>Phone:</b> {data.data.user.phone}</p>
                    </div>

                    {/* ORDERS */}
                    {data.data.orders && (
                        <div className="card orders-card">
                            <h3>Orders ({data.data.orders.totalOrders})</h3>

                            <div className="orders-grid">
                                {data.data.orders.orders.map((o, i) => (
                                    <div key={i} className="order-tile">
                                        ✔ {o.title}
                                        <br />
                                        ₹{o.price}
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* PAYMENTS */}
                    {data.data.payments && (
                        <div className="card payments-card">
                            <h3>Payments</h3>
                            <p><b>Status:</b> {data.data.payments.status}</p>
                            <p><b>Method:</b> {data.data.payments.method}</p>
                            <p><b>Currency:</b> {data.data.payments.currency}</p>
                        </div>
                    )}

                </div>
            </div>
        </div>
    );
}

export default Result;