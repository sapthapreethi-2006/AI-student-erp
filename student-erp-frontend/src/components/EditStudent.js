import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const API_URL = `${process.env.REACT_APP_API_URL || "http://localhost:8080"}/students`;

function EditStudent() {

  const { id } = useParams();
  const navigate = useNavigate();

  const [student, setStudent] = useState({
    name: "",
    email: "",
    department: {
      id: ""
    }
  });

  useEffect(() => {

    axios.get(`${API_URL}/${id}`)
      .then(res => {
        setStudent(res.data);
      })
      .catch((error) => {
        console.error("Error loading student:", error);
        alert("Unable to load the selected student.");
      });

  }, [id]);

  const handleChange = (e) => {

  const { name, value } = e.target;

  if (name === "department") {

      setStudent({
          ...student,
          department: { id: parseInt(value) }   // ✅ convert to number
      });

  } else {

      setStudent({
          ...student,
          [name]: value
      });

  }
};

  const updateStudent = (e) => {

    e.preventDefault();

    axios.put(`${API_URL}/${id}`, student)
      .then(() => {
        alert("Student Updated Successfully");
        navigate("/students");
      })
      .catch((error) => {
        console.error("Error updating student:", error);
        alert("Unable to update student. Please try again.");
      });

  };

  return (

    <div className="container mt-4">

      <div className="card p-4 shadow">

        <h3>Edit Student</h3>

        <form onSubmit={updateStudent}>

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
value={student.department?.id || ""}
onChange={handleChange}
>

<option value="">Select Department</option>
<option value="1">CSE</option>
<option value="2">IT</option>
<option value="3">ECE</option>
<option value="4">MECH</option>

</select>

          </div>

          <button className="btn btn-primary">
            Update Student
          </button>

        </form>

      </div>

    </div>

  );

}

export default EditStudent;