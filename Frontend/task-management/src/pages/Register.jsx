import { Button } from 'primereact/button';
import { IconField } from 'primereact/iconfield';
import { InputIcon } from 'primereact/inputicon';
import { InputText } from "primereact/inputtext";
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { API } from '../services';

const Login = () => {
    const [showPassword, setShowPassword] = useState(false);

    const {register, handleSubmit} = useForm();

    const navigate = useNavigate();

    async function createUser(dados) {
        const loginRequest = await API.post("auth/sign-up", dados);
        if (loginRequest.status === 200) {
            navigate("/");
        }
    }

    return ( 
        <>
            <div className='bg-primary-500 h-screen max-w-full flex flex-column align-items-center justify-content-center px-3'>
                <h2 className='uppercase'>Task Management</h2>
                <form onSubmit={handleSubmit(createUser)} className='col-12 lg:col-5 md:col-6 surface-0 p-3 border-round-md bg-white'>
                    <h3 style={{color: 'var(--blue-900)'}} className='text-center text-4xl'>Welcome!</h3>
                    
                    <label style={{color: 'var(--blue-900)'}} htmlFor="username" className='block uppercase font-bold text-sm mb-1'>Username</label>
                    <InputText 
                        id="username" 
                        placeholder="username"
                        type='text'
                        className='mb-3 w-full bg-white'
                        style={{color: `var(--surface-0)`}}
                        {...register('username', {required: true})}
                    />
                    
                    <label style={{color: 'var(--blue-900)'}} htmlFor="email" className='block uppercase font-bold text-sm mb-1'>Email</label>
                    <InputText 
                        id="email" 
                        placeholder="email"
                        type='email'
                        className='mb-3 w-full bg-white'
                        style={{color: `var(--surface-0)`}}
                        {...register('email', {required: true})}
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
                            label="Register"
                            type="submit"
                            style={{color: 'var(--white)'}}
                            className='bg-blue-800'/>

                        <Button 
                            label='Already have an account? Login'
                            type="button"
                            onClick={
                                () => { navigate("/"); }
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