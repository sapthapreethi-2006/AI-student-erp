import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';

import Dashboard from './pages/Dashboard';
import StudentList from './components/StudentList';
import AddStudent from './components/AddStudent';
import EditStudent from './components/EditStudent';
import AiAssistant from './components/AiAssistant';

function App() {

  return (

    <Router>

      <Navbar/>

      <div style={{display:"flex"}}>

        <Sidebar/>

        <div className="container mt-4">

          <Routes>

            <Route path="/" element={<Dashboard />} />

            <Route path="/students" element={<StudentList />} />

            <Route path="/add-student" element={<AddStudent />} />

            <Route path="/edit-student/:id" element={<EditStudent />} />

            <Route path="/ai-assistant" element={<AiAssistant />} />

          </Routes>

        </div>

      </div>

    </Router>

  );
}

export default App;