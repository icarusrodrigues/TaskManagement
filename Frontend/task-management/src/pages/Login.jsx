import { Button } from 'primereact/button';
import { IconField } from 'primereact/iconfield';
import { InputIcon } from 'primereact/inputicon';
import { InputText } from "primereact/inputtext";
import { Toast } from 'primereact/toast';
import { useContext, useState, useRef } from 'react';
import { useForm } from 'react-hook-form';
import { Context } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { API } from '../services';
import Cookies from 'js-cookie';


const Login = () => {
    const [showPassword, setShowPassword] = useState(false);
    const {register, handleSubmit} = useForm();
    const { setLogged } = useContext(Context);
    const navigate = useNavigate();
    const toast = useRef(null);

    async function login(dados) {
        try {
            const loginRequest = await API.post("auth/login", dados);
            setLogged(true);
            Cookies.set("token", loginRequest.data.token);
            navigate("/home");
        } catch(error) {
            toast.current.show({ severity: 'error', summary: 'Error', detail: error.response.data.message, life: 3000 });
        }
    }

    return ( 
        <>
            <Toast ref={toast} />
            <div className='bg-blue-600 h-screen max-w-full flex flex-column align-items-center justify-content-center px-3'>
                <h2 className='uppercase'>Task Management</h2>
                <form onSubmit={handleSubmit(login)} className='col-12 lg:col-5 md:col-6 surface-0 p-3 border-round-md bg-white'>
                    <h3 style={{color: 'var(--blue-900)'}} className='text-center text-4xl'>Welcome!</h3>
                    <label style={{color: 'var(--blue-900)'}} htmlFor="username" className='block uppercase font-bold text-sm mb-1'>Username or Email</label>
                    <InputText 
                        id="username" 
                        placeholder="username"
                        className='mb-3 w-full bg-white'
                        style={{color: `var(--surface-0)`}}
                        {...register('username', {required: true})}
                    />

                    <label htmlFor="password" style={{color: 'var(--blue-900)'}} className='block uppercase font-bold text-sm mb-1'>Password</label>
                    <div className='mb-3'>
                    <IconField>
                        <InputIcon 
                            className={`pi ${showPassword ? 'pi-eye': 'pi-eye-slash'} cursor-pointer`}
                            style={{color: `var(--surface-0)`}}
                            onClick={() => setShowPassword(!showPassword)}>
                        </InputIcon>
                        <InputText 
                            id="password" 
                            type={showPassword ? 'text' : 'password'}
                            placeholder="********"
                            className='w-full bg-white'
                            style={{color: `var(--surface-0)`}}
                            {...register('password', {required: true})}
                        />
                    </IconField>
                    </div>
                    <div className='flex justify-content-between'>
                        <Button
                            label="Login"
                            type="submit"
                            style={{color: 'var(--white)'}}
                            className='bg-blue-600'/>

                        <Button 
                            label='Create Account'
                            type="button"
                            onClick={
                                () => { navigate("/register"); }
                            }
                            style={{color: 'var(--white)'}}
                            className='bg-blue-600'/>
                    </div>
                </form>
            </div>
        </>
    );
}
 
export default Login;