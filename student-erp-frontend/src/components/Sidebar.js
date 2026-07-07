import React from "react";
import { Link } from "react-router-dom";

function Sidebar() {

  return (

    <div className="bg-light border" style={{width:"220px", height:"100vh"}}>

      <div className="p-3">

        <h5>Menu</h5>

        <ul className="nav flex-column">

          <li className="nav-item">
            <Link className="nav-link" to="/">Dashboard</Link>
          </li>

          <li className="nav-item">
            <Link className="nav-link" to="/students">Students</Link>
          </li>

          <li className="nav-item">
            <Link className="nav-link" to="/add-student">Add Student</Link>
          </li>

          <li className="nav-item">
            <Link className="nav-link" to="/ai-assistant">AI Assistant</Link>
          </li>

        </ul>

      </div>

    </div>

  );
}

export default Sidebar;