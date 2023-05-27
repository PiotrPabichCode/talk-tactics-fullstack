import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { request } from '../../../api/AxiosHelper';

export default function EditUser() {
  const navigate = useNavigate();
  const url = '/admin?isUserDisplayed=true';
  const roles = [{ value: 'ADMIN' }, { value: 'USER' }];

  const { id } = useParams();

  const [updateData, setUpdateData] = useState({
    id: 0,
    login: '',
    firstName: '',
    lastName: '',
    email: '',
    role: '',
  });

  const { login, firstName, lastName, email, role } = updateData;

  const onInputChange = (e) => {
    setUpdateData({ ...updateData, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    const loadUser = async () => {
      try {
        const response = await request('GET', `/api/user/${id}`);
        console.log(response.data);
        setUpdateData((updateData) => ({
          ...updateData,
          id: response.data.id,
          login: response.data.login,
          firstName: response.data.firstName,
          lastName: response.data.lastName,
          email: response.data.email,
          role: response.data.role,
        }));
      } catch (error) {
        console.log(error);
      }
    };
    loadUser();
  }, [id]);

  useEffect(() => {
    console.log(updateData);
  }, [updateData]);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      console.log(updateData);
      await request('PUT', `/api/update_user`, updateData);
      toast.success('User edited successfully');
      navigate(url);
    } catch (error) {
      toast.error('Something went wrong');
      console.log(error);
    }
  };

  return (
    <div className='container-fluid bg-secondary px-4 pb-4'>
      <div className='row text-light'>
        <div className='col-md-6 offset-md-3 bg-dark opacity-100 border rounded p-4 mt-2 shadow position-relative'>
          <Link
            className='btn btn-primary position-absolute end-0 me-4'
            to={url}>
            Back
          </Link>
          <h2 className='text-center m-4'>Edit User</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className='mb-3'>
              <label htmlFor='Login' className='form-label'>
                Login
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter your login'
                name='login'
                value={login}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='FirstName' className='form-label'>
                First name
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter your first name'
                name='firstName'
                value={firstName}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='LastName' className='form-label'>
                Last name
              </label>
              <input
                type={'text'}
                className='form-control'
                placeholder='Enter your last name'
                name='lastName'
                value={lastName}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Email' className='form-label'>
                Email
              </label>
              <input
                type={'email'}
                className='form-control'
                placeholder='Enter your email'
                name='email'
                value={email}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='Admin' className='form-label'>
                Admin
              </label>
              <select
                className='form-control'
                name='role'
                value={role}
                onChange={(e) => onInputChange(e)}>
                {roles.map((item) => (
                  <option key={item.value} value={item.value}>
                    {item.value}
                  </option>
                ))}
              </select>
            </div>
            <button type='submit' className='btn btn-outline-primary'>
              Submit
            </button>
            <Link className='btn btn-outline-danger mx-2' to={url}>
              Cancel
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
}