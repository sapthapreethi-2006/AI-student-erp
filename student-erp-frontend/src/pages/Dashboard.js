import React from "react";

function Dashboard(){

  return(

    <div>

      <h2>Dashboard</h2>

      <div className="row mt-4">

        <div className="col-md-4">
          <div className="card text-white bg-primary mb-3">
            <div className="card-body">
              <h5>Total Students</h5>
              <h3>120</h3>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card text-white bg-success mb-3">
            <div className="card-body">
              <h5>Departments</h5>
              <h3>5</h3>
            </div>
          </div>
        </div>

      </div>

    </div>

  );

}

export default Dashboard;