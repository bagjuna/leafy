import axios from 'axios';

const api = axios.create({
});

api.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
export default api;