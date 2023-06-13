import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { request } from 'api/AxiosHelper';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

export default function AddUser() {
  const { t } = useTranslation();
  let navigate = useNavigate();

  const [user, setUser] = useState({
    name: '',
    username: '',
    email: '',
  });

  const { name, username, email } = user;

  const onInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      await request('POST', '/api/users', user);
      CustomToast(
        TOAST_SUCCESS,
        t('toast.success.add.user'),
        TOAST_AUTOCLOSE_SHORT
      );
      navigate('/');
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className='container'>
      <div className='row'>
        <div className='col-md-6 offset-md-3 border rounded p-4 mt-2 shadow'>
          <h2 className='text-center m-4'>Register User</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className='mb-3'>
              <label htmlFor='Name' className='form-label'>
                Name
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter your name'
                name='name'
                value={name}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Username' className='form-label'>
                Username
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter your username'
                name='username'
                value={username}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Email' className='form-label'>
                E-mail
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter your e-mail address'
                name='email'
                value={email}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <button type='submit' className='btn btn-outline-primary'>
              Submit
            </button>
            <Link className='btn btn-outline-danger mx-2' to='/'>
              Cancel
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
}
