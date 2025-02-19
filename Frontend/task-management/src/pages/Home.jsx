import { useContext, useRef, useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Button } from 'primereact/button';
import { Calendar } from 'primereact/calendar';
import { Dropdown } from 'primereact/dropdown';
import { InputText } from "primereact/inputtext";
import { InputTextarea } from 'primereact/inputtextarea';
import { OverlayPanel } from 'primereact/overlaypanel';
import { useNavigate } from 'react-router-dom';
import { Context } from '../contexts/AuthContext';
import { ConfirmDialog } from 'primereact/confirmdialog';
import Cookies from 'js-cookie';
import FilteredTasks from '../components/FilteredTasks';
import Tasks from '../components/Tasks';
import { API } from '../services';

const Home = () => {

    const op = useRef(null);
    const navigate = useNavigate();
    const { setLogged } = useContext(Context);
    const {register, handleSubmit} = useForm();
    const [datetime24h, setDateTime24h] = useState(null);
    const [selectedStatus, setSelectedStatus] = useState('');
    const [tasks, setTasks] = useState([]);

    const status = [
        {name: 'Pending', code: 'PENDING'},
        {name: 'In Progress', code: 'IN_PROGRESS'},
        {name: 'Completed', code: 'COMPLETED'},
        {name: 'All', code: 'ALL'}
    ];

    function formatDateToServer(date) {
        const formattedDate = new Date(date);
        return `${formattedDate.getFullYear()}-${String(formattedDate.getMonth() + 1).padStart(2, '0')}-${formattedDate.getDate()}T${formattedDate.getHours()}:${String(formattedDate.getMinutes()).padStart(2, '0')}:00`;
    }
    
    async function createTask(task) {
        if (datetime24h) {
            task.dueDate = formatDateToServer(datetime24h);
        }

        await API.post(`/tasks`, task, {
            headers: {
                Authorization: `Bearer ${Cookies.get("token")}`
            }
        });

        window.location.reload();
    }

    async function searchTasks() {        
        const finalTasks = [
            {name: 'Pending', code: 'PENDING', tasks: []},
            {name: 'In Progress', code: 'IN_PROGRESS', tasks: []},
            {name: 'Completed', code: 'COMPLETED', tasks: []}
        ];
        
        await Promise.all(finalTasks.map(async (finalTask) => {
            const request = await API.get(`/tasks/my-tasks?status=${finalTask.code}`, {
                    headers: {
                        Authorization: `Bearer ${Cookies.get('token')}`
                    }
                }
            );

            finalTask.tasks = request.data.data;
        }));

        setTasks([...finalTasks]);
        return finalTasks;
    }

    async function filteredSearchTasks(actualStatus) {
        if (!actualStatus || actualStatus === 'ALL') {
            await searchTasks();
            return;
        }
        const request = await API.get(`/tasks/my-tasks?status=${actualStatus}`, {
            headers: {
                Authorization: `Bearer ${Cookies.get('token')}`
            }
        });
        setTasks(request.data.data);
    }

    useEffect(() => {
        searchTasks();
    }, []);

    return ( 
        <>
            <div className="h-full max-w-full min-h-screen flex flex-column">
                <header className="bg-blue-600 flex flex-row justify-content-between align-items-center p-4">
                    <div>
                        <h1 className="p-2">Task List</h1>
                        <Dropdown value={selectedStatus} 
                        onChange={async (e) => {
                            setSelectedStatus(e.value);
                            await filteredSearchTasks(e.value.code);
                        }} options={status} optionLabel="name" 
                        placeholder="Select a Status" className="w-full md:w-14rem" 
                        checkmark={true} 
                        highlightOnSelect={false} />
                    </div>

                    <div className='flex flex-row w-8 md:w-4 lg:w-3 p-5 justify-content-between'>
                        <Button 
                            label='Create new Task'
                            type="button"
                            style={{color: 'var(--white)'}}
                            onClick={(e) => {op.current.toggle(e)}}
                            className='bg-blue-900 hover:bg-blue-300'/>
                        <Button 
                            label='Logout'
                            type="button"
                            style={{color: 'var(--white)'}}
                            onClick={() => {
                                Cookies.remove("token");
                                setLogged(false); 
                                navigate("/");}}
                            className='bg-blue-900 hover:bg-red-600 min-w-max'/>
                    </div>
                </header>

                <OverlayPanel ref={op} id="overlaypanel" style={{width: '450px'}}>
                    <div className="flex flex-column">
                        <h2 className="p-2">Create a new Task</h2>
                        <form onSubmit={handleSubmit(createTask)}>
                            <label style={{color: 'var(--blue-900)'}} htmlFor="title" className='block uppercase font-bold text-sm mb-1'>Title</label>
                            <InputText 
                                id="title" 
                                name="title" 
                                className='mb-3 w-full bg-white'
                                style={{color: `var(--surface-0)`}}
                                {...register('title', {required: true})}
                            />

                            <div className='mb-3'>
                                <label style={{color: 'var(--blue-900)'}} htmlFor="description" className='block uppercase font-bold text-sm mb-1'>Description</label>
                                <InputTextarea
                                    id="description" 
                                    name="description" 
                                    className='w-full bg-white'
                                    aria-describedby="description-help" 
                                    style={{color: `var(--surface-0)`}}
                                    {...register('description', {required: true})}
                                    rows={8} 
                                />
                                <small id="description-help">
                                    Please, describe your task.
                                </small>
                            </div>

                            <div className='mb-3'>
                                <label style={{color: 'var(--blue-900)'}} htmlFor="dueDate" className='block uppercase font-bold text-sm mb-1'>Due Date</label>
                                <Calendar
                                    id="dueDate" 
                                    name="dueDate" 
                                    className='w-full'
                                    dateFormat='dd/mm/yy'
                                    aria-describedby="due-date-help" 
                                    value={datetime24h}
                                    onChange={(e) => setDateTime24h(e.value)}
                                    showIcon showTime hourFormat='24'/>
                                    <small id="due-date-help">
                                        If you do not specify an expiration date, an expiration of 1 week will automatically be added.
                                    </small>
                            </div>

                            <div className="flex flex-column">
                                <Button 
                                    label='Create Task'
                                    type="submit"
                                    className='bg-blue-600'/>
                            </div>
                        </form>
                    </div>
                </OverlayPanel>
                
                <main className='p-4 bg-white'>
                    <ConfirmDialog/>
                    <section className="flex flex-row justify-content-between">
                        {(() => {
                            switch (selectedStatus.code) {
                                case 'PENDING':
                                case 'IN_PROGRESS':
                                case 'COMPLETED':
                                    return <FilteredTasks name={selectedStatus.name} code={selectedStatus.code} tasks={tasks} searchTasks={filteredSearchTasks}/>;
                                default:
                                    return (<>
                                        {
                                            tasks.map((task) => (
                                                <Tasks name={task.name} tasks={task.tasks} searchTasks={searchTasks}/>
                                            ))
                                        }
                                    </>)
                            }
                        })()}   
                        
                    </section>
                </main>
            </div>
        </>
     );
}
 
export default Home;