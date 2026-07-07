import axios from "axios";

const API_URL = `${process.env.REACT_APP_API_URL || "http://localhost:8080"}/students`;

class StudentService {

    getStudents(){
        return axios.get(API_URL);
    }

    getStudentById(studentId){
        return axios.get(API_URL + "/" + studentId);
    }

    createStudent(student){
        return axios.post(API_URL, student);
    }

    updateStudent(studentId, student){
        return axios.put(API_URL + "/" + studentId, student);
    }

    deleteStudent(studentId){
        return axios.delete(API_URL + "/" + studentId);
    }
}

export default new StudentService();