import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "../pages/Login";
import Home from "../pages/Home";
import Register from "../pages/Register";
import CreateTask from "../pages/CreateTask";

const Paths = () => {

    return ( 
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Login/>}/>

                    <Route path="/home" element={<Home/>}/>

                    <Route path="/register" element={<Register/>}/>

                    <Route path="/create" element={<CreateTask/>}/>
                </Routes>
            </BrowserRouter>
        </> 
    );
}
 
export default Paths;