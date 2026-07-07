import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const API_URL = `${process.env.REACT_APP_API_URL || "http://localhost:8080"}/students`;

function AddStudent() {

  const navigate = useNavigate();

  const [student, setStudent] = useState({
    name: "",
    email: "",
    department: {
      id: ""
    }
  });

  const handleChange = (e) => {

    const { name, value } = e.target;

    if (name === "department") {
      setStudent({
        ...student,
        department: { id: value }
      });
    } else {
      setStudent({
        ...student,
        [name]: value
      });
    }

  };

  const saveStudent = (e) => {

    e.preventDefault();

    axios.post(API_URL, student)
      .then(() => {
        alert("Student Added Successfully");
        navigate("/students");
      })
      .catch((error) => {
        console.error("Error adding student:", error);
        alert("Unable to add student. Please try again.");
      });

  };

  return (

    <div className="container mt-4">

      <div className="card p-4 shadow">

        <h3>Add Student</h3>

        <form onSubmit={saveStudent}>

          <div className="mb-3">
            <label>Name</label>

            <input
              type="text"
              name="name"
              className="form-control"
              value={student.name}
              onChange={handleChange}
            />
          </div>

          <div className="mb-3">
            <label>Email</label>

            <input
              type="email"
              name="email"
              className="form-control"
              value={student.email}
              onChange={handleChange}
            />
          </div>

          <div className="mb-3">
            <label>Department</label>

            <select
              name="department"
              className="form-control"
              value={student.department.id}
              onChange={handleChange}
            >

              <option value="">Select Department</option>
              <option value="1">CSE</option>
              <option value="2">IT</option>
              <option value="3">ECE</option>
              <option value="4">MECH</option>

            </select>

          </div>

          <button className="btn btn-success">
            Add Student
          </button>

        </form>

      </div>

    </div>

  );

}

export default AddStudent;