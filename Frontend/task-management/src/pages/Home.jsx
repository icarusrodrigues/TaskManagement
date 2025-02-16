import { useContext } from 'react';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import { Context } from '../contexts/AuthContext';
import Cookies from 'js-cookie';
import Tasks from '../components/Tasks';

const Home = () => {

    const navigate = useNavigate();

    const { setLogged } = useContext(Context);

    return ( 
        <>
            <div className="bg-primary-500 h-full max-w-full min-h-screen flex flex-column p-4">
                <div className="flex flex-row justify-content-between align-items-center">
                    <h1 className="p-2">Lista de tarefas</h1>
                    <div className='flex flex-row w-8 md:w-4 lg:w-3 p-5 justify-content-between'>
                        <Button 
                            label='Cadastrar nova tarefa'
                            type="button"
                            style={{color: 'var(--white)'}}
                            onClick={() => {
                                navigate("/create");}}
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