import { Button } from 'primereact/button';
import { IconField } from 'primereact/iconfield';
import { InputIcon } from 'primereact/inputicon';
import { InputText } from "primereact/inputtext";
import { useContext, useState } from 'react';
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

    async function login(dados) {
        const loginRequest = await API.post("auth/login", dados);
        if (loginRequest.status === 200) {
            setLogged(true);
            Cookies.set("token", loginRequest.data.token);
            navigate("/home")
        }
    }

    return ( 
        <>
            <div className='bg-primary-500 h-screen max-w-full flex flex-column align-items-center justify-content-center px-3'>
                <h2 className='uppercase'>Task Management</h2>
                <form onSubmit={handleSubmit(login)} className='col-12 lg:col-5 md:col-6 surface-0 p-3 border-round-md bg-white'>
                    <h3 style={{color: 'var(--blue-900)'}} className='text-center text-4xl'>Welcome!</h3>
                    <label style={{color: 'var(--blue-900)'}} htmlFor="username" className='block uppercase font-bold text-sm mb-1'>Username</label>
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
                            className='bg-blue-800'/>

                        <Button 
                            label='Create Account'
                            type="button"
                            onClick={
                                () => { navigate("/register"); }
                            }
                            style={{color: 'var(--white)'}}
                            className='bg-blue-800'/>
                    </div>
                </form>
            </div>
        </>
    );
}
 
export default Login;