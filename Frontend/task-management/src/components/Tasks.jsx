import { Button } from 'primereact/button';
import { API } from "../services";
import Cookies from 'js-cookie';
import { useEffect, useState } from "react";

const Tasks = (props) => {
    
    const [tasks, setTasks] = useState([]);
    
    async function searchTasks() {
        const status = props.name.replace(" ", "_").toUpperCase()
        const request = await API.get(`/tasks/my-tasks?status=${status}`, {
                headers: {
                    Authorization: `Bearer ${Cookies.get("token")}`
                }
            }
        );
        setTasks(request.data.data);
    }


    function formatDate(date) {
        const formattedDate = new Date(date);
        return formattedDate.toLocaleString();
    }

    async function deleteTask(id) {
        await API.delete(`/tasks/${id}`, {
            headers: {
                Authorization: `Bearer ${Cookies.get("token")}`
            }
        });
        searchTasks();
    }
    
    async function startTask(id) {
        await API.put(`/tasks/start/${id}`, {}, {
            headers: {
                Authorization: `Bearer ${Cookies.get("token")}`
            }
        });
        searchTasks();
        window.location.reload();
    }

    async function completeTask(id) {
        await API.put(`/tasks/complete/${id}`, {}, {
            headers: {
                Authorization: `Bearer ${Cookies.get("token")}`
            }
        });
        searchTasks();
        window.location.reload();
    }

    useEffect(() => {
        searchTasks();
    }, []);

    return (<>
        <div className="flex flex-column w-3">
            <h2 className="p-2">{props.name}</h2>
            <ul className="flex flex-column flex-wrap list-none justify-content-between p-0">
                {
                    tasks.map((task) => (
                        <li className="w-full mb-2" key={task.id}>
                            <div className="shadow-4 p-3 border-round-md bg-gray-600">
                                <div className='flex flex-row justify-content-between align-items-center mb-3'>
                                    <h3 className="mt-0 mb-0 white-space-nowrap overflow-hidden text-overflow-ellipsis">{task.title}</h3>
                                    <Button className='hover:bg-red-500 border-none border-round-sm' icon="pi pi-trash" onClick={() => {deleteTask(task.id)}}/>
                                </div>
                                <p className="mt-0 text-primary">{task.description}</p>
                                <h6 className="mt-0 text-primary">{task.status}</h6>
                                <div className='flex flex-row justify-content-between align-items-center'>
                                    <p className="mt-0 text-primary">{formatDate(task.dueDate)}</p>
                                    {(() => {
                                        switch (props.name) {
                                        case 'Pending':
                                            return <Button 
                                            label='Iniciar' className='hover:bg-green-500 border-none border-round-sm'
                                            onClick={() => {
                                                startTask(task.id)
                                            }}/>
                                        case 'In Progress':
                                            case 'Pending':
                                                return <Button 
                                                label='Completar' className='hover:bg-green-500 border-none border-round-sm'
                                                onClick={() => {
                                                    completeTask(task.id)
                                                }}/>
                                        default:
                                            return null
                                        }
                                    })()}
                                </div>
                            </div>
                        </li>    
                    ))
                }
            </ul>
        </div>
        
    </>);
}

export default Tasks;