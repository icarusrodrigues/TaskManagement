import { useEffect, useState, useRef } from 'react';
import { useForm } from 'react-hook-form';
import { Button } from 'primereact/button';
import { API } from '../services';
import { confirmDialog } from 'primereact/confirmdialog';
import { OverlayPanel } from 'primereact/overlaypanel';
import { Calendar } from 'primereact/calendar';
import { InputText } from "primereact/inputtext";
import { InputTextarea } from 'primereact/inputtextarea';
import { ScrollPanel } from 'primereact/scrollpanel';

import Cookies from 'js-cookie';

const Tasks = (props) => {
    const op = useRef(null);
    const [tasks, setTasks] = useState([]);
    const {register, handleSubmit} = useForm();
    
    const [idToUpdate, setIdToUpdate] = useState(0);    
    const [titleToUpdate, setTitleToUpdate] = useState('');
    const [descriptionToUpdate, setDescriptionToUpdate] = useState('');
    const [dueDateToUpdate, setDueDateToUpdate] = useState('');
    
    async function searchTasks() {
        const status = props.name.replace(' ', '_').toUpperCase()
        const request = await API.get(`/tasks/my-tasks?status=${status}`, {
                headers: {
                    Authorization: `Bearer ${Cookies.get('token')}`
                }
            }
        );
        setTasks(request.data.data);
    }

    function formatDate(date) {
        const formattedDate = new Date(date);
        return `${formattedDate.getDate()}/${String(formattedDate.getMonth() + 1).padStart(2, '0')}/${formattedDate.getFullYear()} ${formattedDate.getHours()}:${String(formattedDate.getMinutes()).padStart(2, '0')}`;
    }

    function formatDateToServer(date) {
        const formattedDate = new Date(date);
        return `${formattedDate.getFullYear()}-${String(formattedDate.getMonth() + 1).padStart(2, '0')}-${formattedDate.getDate()}T${formattedDate.getHours()}:${String(formattedDate.getMinutes()).padStart(2, '0')}:00`;
    }

    const confirmDelete = (id) => {
        confirmDialog({
            message: 'Do you want to delete this task?',
            header: 'Delete Confirmation',
            icon: 'pi pi-info-circle',
            accept: () => deleteTask(id),
            reject: () => null
        });
    }

    async function updateTask(task) {
        if (dueDateToUpdate) {
            task.dueDate = formatDateToServer(dueDateToUpdate);
        }
        await API.put(`/tasks/${idToUpdate}`, task, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        searchTasks();
    }

    async function deleteTask(id) {
        await API.delete(`/tasks/${id}`, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        searchTasks();
    }
    
    async function startTask(id) {
        await API.put(`/tasks/start/${id}`, {}, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        searchTasks();
        window.location.reload();
    }

    async function completeTask(id) {
        await API.put(`/tasks/complete/${id}`, {}, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        searchTasks();
        window.location.reload();
    }

    useEffect(() => {
        searchTasks();
    }, []);

    return (<>
        <div className='flex flex-column w-3'>
            <h2 className='p-2'>{props.name}</h2>
            <ul className='flex flex-column flex-wrap list-none justify-content-between p-0'>
                {
                    tasks.map((task) => (
                        <li className='w-full mb-2' key={task.id}>
                            <article className='shadow-4 p-3 border-round-md bg-gray-600'>
                                <div className='flex flex-row justify-content-between align-items-center mb-3'>
                                    <h3 className='mt-0 mb-0 white-space-nowrap overflow-hidden text-overflow-ellipsis'>{task.title}</h3>
                                    <div className='flex flex-column md:flex-row justify-content-between align-items-center gap-1'>
                                        <Button className='border-none border-round-sm' icon='pi pi-pencil' onClick={(e) => {
                                            setIdToUpdate(task.id);
                                            setTitleToUpdate(task.title);
                                            setDescriptionToUpdate(task.description);
                                            setDueDateToUpdate(task.dueDate);

                                            op.current.toggle(e);
                                        }}/>

                                        <Button className='hover:bg-red-500 border-none border-round-sm' icon='pi pi-trash' onClick={() => {
                                            confirmDelete(task.id)
                                        }}/>
                                    </div>
                                </div>

                                <OverlayPanel ref={op} id="overlaypanel" style={{width: '450px'}}>
                                    <div className="flex flex-column">
                                        <h2 className="p-2">Edit Task</h2>
                                        <form onSubmit={handleSubmit(updateTask)}>
                                            <label style={{color: 'var(--blue-900)'}} htmlFor="title" className='block uppercase font-bold text-sm mb-1'>Title</label>
                                            <InputText 
                                                id="title" 
                                                name="title" 
                                                className='mb-3 w-full bg-white'
                                                style={{color: `var(--surface-0)`}}
                                                {...register('title')}
                                                value={titleToUpdate}
                                                onChange={(e) => {setTitleToUpdate(e.value)}}/>

                                            <label style={{color: 'var(--blue-900)'}} htmlFor="description" className='block uppercase font-bold text-sm mb-1'>Description</label>
                                            <InputTextarea
                                                id="description" 
                                                name="description" 
                                                className='mb-3 w-full bg-white'
                                                style={{color: `var(--surface-0)`}}
                                                {...register('description')}
                                                value={descriptionToUpdate}
                                                onChange={(e) => {setDescriptionToUpdate(e.value)}}
                                                rows={8}/>

                                            {(() => {
                                                switch (props.name) {
                                                    case 'Pending':
                                                        return <div>
                                                            <label style={{color: 'var(--blue-900)'}} htmlFor="dueDate" className='block uppercase font-bold text-sm mb-1'>Due Date</label>
                                                            <Calendar
                                                                id="dueDate" name="dueDate" 
                                                                className='mb-3 w-full'
                                                                dateFormat="dd/mm/yy"
                                                                value={dueDateToUpdate}
                                                                onChange={(e) => setDueDateToUpdate(e.value)}
                                                                showIcon showTime hourFormat="24"/>
                                                        </div>
                                                    default:
                                                        return null;
                                                }
                                            })()}
                                            
                                            <Button
                                                label='Update Task'
                                                type='submit'
                                                className='bg-blue-800 text-white border-none border-round-sm'/>
                                        </form>
                                    </div>
                                </OverlayPanel>

                                <ScrollPanel style={{ width: '100%', height: '150px', marginBottom: '1rem' }} >
                                    <p className='mt-0 max-h-8rem text-primary'>{task.description}</p>
                                </ScrollPanel>
                                <h6 className='mt-0 text-primary'>{task.status}</h6>
                                <div className='flex flex-row justify-content-between align-items-center'>
                                    <p className='my-0 text-primary'>{formatDate(task.dueDate)}</p>
                                    {(() => {
                                        switch (props.name) {
                                            case 'Pending':
                                                return <Button 
                                                label='Start' className='hover:bg-green-500 border-none border-round-sm'
                                                onClick={() => {
                                                    startTask(task.id)
                                                }}/>
                                            case 'In Progress':
                                                return <Button 
                                                label='Complete' className='hover:bg-green-500 border-none border-round-sm'
                                                onClick={() => {
                                                    completeTask(task.id)
                                                }}/>
                                            default:
                                                return null
                                        }
                                    })()}
                                </div>
                            </article>
                        </li>    
                    ))
                }
            </ul>
        </div>
    </>);
}

export default Tasks;