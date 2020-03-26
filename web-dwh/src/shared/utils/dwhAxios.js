import axios from 'axios';
import queryString from 'query-string/index';

const dwhAxios = axios.create({
  paramsSerializer: queryString.stringify,
  withCredentials: true,
});

export default dwhAxios;
