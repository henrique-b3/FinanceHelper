import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080'
});


api.interceptors.request.use(async config => {
    
    const token = localStorage.getItem('token');

    if(token){
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
    
});

api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        let errorMessage = "Ocorreu um erro inesperado. Tente novamente.";

        if (error.response && error.response.data) {
            const { data } = error.response;
            
            if (data.message) {
                errorMessage = data.message;
            } 

            else if (typeof data === 'object') {
                const keys = Object.keys(data);
                if (keys.length > 0) {
                     errorMessage = data[keys[0]]; 
                }
            }
        }

        return Promise.reject(errorMessage);
    }
);

export default api;