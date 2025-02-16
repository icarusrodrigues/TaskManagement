import axios from "axios";

export const API = axios.create({
    baseURL: "http://localhost:8080/task-management/api/"}
);