import { createContext, useState } from "react";

export const Context = createContext();

export const AuthContext = ({children}) => {

    const [logged, setLogged] = useState(false);

    return (
        <>
            <Context.Provider value={{ logged, setLogged }}>
                {children}
            </Context.Provider>
        </>
    );
};