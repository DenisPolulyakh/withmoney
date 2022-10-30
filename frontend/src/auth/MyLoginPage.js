import * as React from 'react';
import {useState} from 'react';
import {useLogin, useNotify} from 'react-admin';
import './login.css'

const MyLoginPage = ({theme}) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const login = useLogin();
    const notify = useNotify();

    const handleSubmit = e => {
        e.preventDefault();
        login({email, password}).catch(() =>
            notify('Invalid email or password')
        );
    };

    return (
        <div className="wrapper fadeInDown">
            <div id="formContent">

                <h2 className="active"> Sign In </h2>
                <h2 className="inactive">Sign Up </h2>

{/*
                <div className="fadeIn first">
                    <img src="http://danielzawadzki.com/codepen/01/icon.svg" id="icon" alt="User Icon"/>
                </div>*/}


                <form>
                    <input type="text" id="login" className="fadeIn second" name="login" placeholder="login"/>
                        <input type="text" id="password" className="fadeIn third" name="login" placeholder="password"/>
                            <input type="submit" className="fadeIn fourth" value="Log In"/>
                </form>


                <div id="formFooter">
                    <a className="underlineHover" href="#">Forgot Password?</a>
                </div>

            </div>
        </div>
    );
};

export default MyLoginPage;