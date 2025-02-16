import { Button } from 'primereact/button';
import { IconField } from 'primereact/iconfield';
import { InputIcon } from 'primereact/inputicon';
import { InputText } from "primereact/inputtext";
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { API } from '../services';

const Login = () => {
    const [mostrarSenha, setMostrarSenha] = useState(false);

    const {register, handleSubmit} = useForm();

    const navigate = useNavigate();

    async function registrar(dados) {
        const loginRequest = await API.post("auth/sign-up", dados);
        if (loginRequest.status === 200) {
            navigate("/");
        }
    }
    return ( 
        <>
            <div className='bg-primary-500 h-screen max-w-full flex flex-column align-items-center justify-content-center px-3'>
                <h2 className='uppercase'>Task Management</h2>
                <form onSubmit={handleSubmit(registrar)} className='col-12 lg:col-5 md:col-6 surface-0 p-3 border-round-md bg-white'>
                    <h3 style={{color: 'var(--blue-900)'}} className='text-center text-4xl'>Seja bem-vindo</h3>
                    
                    <label style={{color: 'var(--blue-900)'}} htmlFor="username" className='block uppercase font-bold text-sm mb-1'>Nome de usuário</label>
                    <InputText 
                        id="username" 
                        placeholder="username"
                        className='mb-3 w-full surface-700'
                        {...register('username', {required: true})}
                    />
                    
                    <label style={{color: 'var(--blue-900)'}} htmlFor="email" className='block uppercase font-bold text-sm mb-1'>Email</label>
                    <InputText 
                        id="email" 
                        placeholder="email"
                        className='mb-3 w-full surface-700'
                        {...register('email', {required: true})}
                    />

                    <label htmlFor="password" style={{color: 'var(--blue-900)'}} className='block uppercase font-bold text-sm mb-1'>Senha</label>
                    <div className='mb-3'>
                    <IconField>
                        <InputIcon 
                            className={`pi ${mostrarSenha ? 'pi-eye': 'pi-eye-slash'} cursor-pointer`}
                            onClick={() => setMostrarSenha(!mostrarSenha)}>
                        </InputIcon>
                        <InputText 
                            id="password" 
                            type={mostrarSenha ? 'text' : 'password'}
                            placeholder="********"
                            className='w-full surface-700'
                            {...register('password', {required: true})}
                        />
                    </IconField>
                    </div>
                    <div className='flex justify-content-between'>
                        <Button
                            label="Registrar"
                            type="submit"
                            style={{color: 'var(--white)'}}
                            className='bg-blue-800'/>

                        <Button 
                            label='Já tem uma conta? Faça seu login'
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