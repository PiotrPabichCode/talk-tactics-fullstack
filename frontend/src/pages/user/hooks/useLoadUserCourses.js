import { useEffect, useState } from 'react';
import { request } from '../../../api/AxiosHelper';
import { toast } from 'react-toastify';
import { getUsername } from '../../../api/AxiosHelper';

export default function useLoadUserCourses() {
  const [userCourses, setUserCourses] = useState([]);
  const login = getUsername();

  useEffect(() => {
    const loadUserCourses = async () => {
      try {
        console.log(login);
        const response = await request(
          'GET',
          `/api/user-courses/users/login/${login}`
        );
        console.log(response.data);
        setUserCourses(response.data);
      } catch (error) {
        toast.error('Something went wrong');
        console.log(error);
      }
    };
    loadUserCourses();
  }, [login]);

  return [userCourses, setUserCourses];
}
