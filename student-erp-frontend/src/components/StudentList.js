import React, { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

const API_URL = `${process.env.REACT_APP_API_URL || "http://localhost:8080"}/students`;

function StudentList() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    loadStudents();
  }, []);

  const loadStudents = () => {
    setLoading(true);
    setError("");

    axios.get(API_URL)
      .then((response) => {
        setStudents(response.data);
      })
      .catch((err) => {
        console.error("Error loading students:", err);
        setError("Unable to load students from the backend.");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const deleteStudent = (id) => {
    Swal.fire({
      title: "Are you sure?",
      text: "This student will be deleted!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!"
    }).then((result) => {
      if (result.isConfirmed) {
        axios.delete(`${API_URL}/${id}`)
          .then(() => {
            Swal.fire("Deleted!", "Student has been deleted.", "success");
            loadStudents();
          })
          .catch((err) => {
            console.error("Error deleting student:", err);
            setError("Unable to delete the selected student.");
          });
      }
    });
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2 className="mb-0">Student List</h2>
        <button className="btn btn-success btn-sm" onClick={() => navigate("/add-student")}>Add Student</button>
      </div>

      {loading && <div className="alert alert-info">Loading students...</div>}
      {error && <div className="alert alert-danger">{error}</div>}

      {!loading && !error && (
        <table className="table table-bordered table-striped">
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Department</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {students.length > 0 ? (
              students.map((student) => (
                <tr key={student.id}>
                  <td>{student.id}</td>
                  <td>{student.name}</td>
                  <td>{student.email}</td>
                  <td>{student.department?.name || "-"}</td>
                  <td>
                    <button className="btn btn-primary btn-sm me-2" onClick={() => navigate(`/edit-student/${student.id}`)}>
                      Edit
                    </button>
                    <button className="btn btn-danger btn-sm" onClick={() => deleteStudent(student.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5" className="text-center">No students found.</td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default StudentList;