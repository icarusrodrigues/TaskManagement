import { useContext, useRef, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Button } from 'primereact/button';
import { Calendar } from 'primereact/calendar';
import { InputText } from "primereact/inputtext";
import { InputTextarea } from 'primereact/inputtextarea';
import { OverlayPanel } from 'primereact/overlaypanel';
import { useNavigate } from 'react-router-dom';
import { Context } from '../contexts/AuthContext';
import Cookies from 'js-cookie';
import Tasks from '../components/Tasks';
import { API } from '../services';

const Home = () => {

    const op = useRef(null);

    const navigate = useNavigate();

    const [datetime24h, setDateTime24h] = useState(new Date());

    const { setLogged } = useContext(Context);

    const {register, handleSubmit} = useForm();
    
    async function createTask(task) {
        await API.post(`/tasks`, task, {
            headers: {
                Authorization: `Bearer ${Cookies.get("token")}`
            }
        });
        window.location.reload();
    }

    return ( 
        <>
            <div className="bg-primary-500 h-full max-w-full min-h-screen flex flex-column p-4">
                <div className="flex flex-row justify-content-between align-items-center">
                    <h1 className="p-2">Task List</h1>
                    <div className='flex flex-row w-8 md:w-4 lg:w-3 p-5 justify-content-between'>
                        <Button 
                            label='Create new Task'
                            type="button"
                            style={{color: 'var(--white)'}}
                            onClick={(e) => {op.current.toggle(e)}}
                            className='bg-blue-800'/>
                        <Button 
                            label='Logout'
                            type="button"
                            style={{color: 'var(--white)'}}
                            onClick={() => {
                                Cookies.remove("token");
                                setLogged(false); 
                                navigate("/");}}
                            className='bg-blue-800 min-w-max'/>
                    </div>
                </div>

                <OverlayPanel ref={op} showCloseIcon id="overlaypanel" style={{width: '450px'}}>
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

                            <label style={{color: 'var(--blue-900)'}} htmlFor="description" className='block uppercase font-bold text-sm mb-1'>Description</label>
                            <InputTextarea
                                id="description" 
                                name="description" 
                                className='mb-3 w-full bg-white'
                                style={{color: `var(--surface-0)`}}
                                {...register('description', {required: true})}
                                rows={8}
                            />

                            <label style={{color: 'var(--blue-900)'}} htmlFor="dueDate" className='block uppercase font-bold text-sm mb-1'>Due Date</label>
                            <Calendar
                                id="dueDate" 
                                name="dueDate" 
                                className='mb-3 w-full'
                                dateFormat="dd/mm/yy"
                                {...register('dueDate')} 
                                value={datetime24h}
                                onChange={(e) => setDateTime24h(e.value)}
                                showIcon showTime hourFormat="24"/>

                            <div className="flex flex-column">
                                <Button 
                                    label='Create Task'
                                    type="submit"
                                    className='bg-blue-800'/>
                            </div>
                        </form>
                    </div>
                </OverlayPanel>
                
                <div className="flex flex-row justify-content-between">
                    <Tasks name={'Pending'}/>
                    <Tasks name={'In Progress'}/>
                    <Tasks name={'Completed'}/>
                </div>
            </div>
        </>
     );
}
 
export default Home;