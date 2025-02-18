import { useState, useRef } from 'react';
import { useForm } from 'react-hook-form';
import { Button } from 'primereact/button';
import { OverlayPanel } from 'primereact/overlaypanel';
import { Calendar } from 'primereact/calendar';
import { InputText } from "primereact/inputtext";
import { InputTextarea } from 'primereact/inputtextarea';
import { ScrollPanel } from 'primereact/scrollpanel';
import { confirmDialog } from 'primereact/confirmdialog';
import { API } from '../services';
import Cookies from 'js-cookie';

const Task = (props) => {
    const op = useRef(null);
    const {register, handleSubmit} = useForm();
    
    const [idToUpdate, setIdToUpdate] = useState(0);    
    const [titleToUpdate, setTitleToUpdate] = useState('');
    const [descriptionToUpdate, setDescriptionToUpdate] = useState('');
    const [dueDateToUpdate, setDueDateToUpdate] = useState('');

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
        const actualStatus = props.task.status;

        if (dueDateToUpdate) {
            task.dueDate = formatDateToServer(dueDateToUpdate);
        }
        await API.put(`/tasks/${idToUpdate}`, task, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        
        await props.searchTasks(actualStatus);
    }

    async function deleteTask(id) {
        const actualStatus = props.task.status;

        await API.delete(`/tasks/${id}`, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });

        await props.searchTasks(actualStatus);
    }
    
    async function startTask(id) {
        const actualStatus = props.task.status;
        
        await API.put(`/tasks/start/${id}`, {}, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        
        await props.searchTasks(actualStatus);
    }

    async function completeTask(id) {
        const actualStatus = props.task.status;

        await API.put(`/tasks/complete/${id}`, {}, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        
        await props.searchTasks(actualStatus);
    }
    
    return (
        <>
            <article className='shadow-4 p-3 border-round-md bg-gray-600'>
                <div className='flex flex-row justify-content-between align-items-center mb-3'>
                    <h3 className='mt-0 mb-0 white-space-nowrap overflow-hidden text-overflow-ellipsis'>{props.task.title}</h3>
                    <div className='flex flex-column md:flex-row justify-content-between align-items-center gap-1'>
                        {(() => {
                            switch (props.task.status) {
                                case 'PENDING': case 'IN_PROGRESS':
                                    return <Button className='border-none border-round-sm' icon='pi pi-pencil' onClick={(e) => {
                                    setIdToUpdate(props.task.id);
                                    setTitleToUpdate(props.task.title);
                                    setDescriptionToUpdate(props.task.description);
                                    setDueDateToUpdate(props.task.dueDate);

                                    op.current.toggle(e);}}/>
                                default:
                                    return null;
                            }
                        })()}
                        

                        <Button className='hover:bg-red-500 border-none border-round-sm' icon='pi pi-trash' onClick={() => {
                            confirmDelete(props.task.id)
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
                                switch (props.task.status) {
                                    case 'PENDING':
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
                    <p className='mt-0 max-h-8rem text-primary'>{props.task.description}</p>
                </ScrollPanel>
                <h6 className='mt-0 text-primary'>{props.task.status}</h6>
                <div className='flex flex-row justify-content-between align-items-center'>
                    <p className='my-0 text-primary'>{formatDate(props.task.dueDate)}</p>
                    {(() => {
                        switch (props.task.status) {
                            case 'PENDING':
                                return <Button 
                                label='Start' className='hover:bg-green-500 border-none border-round-sm'
                                onClick={() => {
                                    startTask(props.task.id)
                                }}/>
                            case 'IN_PROGRESS':
                                return <Button 
                                label='Complete' className='hover:bg-green-500 border-none border-round-sm'
                                onClick={() => {
                                    completeTask(props.task.id)
                                }}/>
                            default:
                                return null
                        }
                    })()}
                </div>
            </article>
        </>
    );
}

export default Task;