import { Routes, Route } from "react-router-dom";
import Dashboard from "./components/Dashboard";
import Result from "./components/Result";
import "./App.css";

function App() {
    return (
        <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/result" element={<Result />} />
        </Routes>
    );
}

export default App;