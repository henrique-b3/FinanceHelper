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
        let errorMessage = "Ocorreu um erro inesperado. Tente novamente mais tarde.";

        // 1. Cenário: O servidor está offline ou a internet falhou (sem resposta)
        if (!error.response) {
            errorMessage = "Não foi possível conectar ao servidor. Verifique a sua ligação à internet.";
            return Promise.reject(errorMessage);
        }

        const { data, status } = error.response;

        if (status === 401 || status === 403) {
            errorMessage = "A sua sessão expirou ou não tem permissão. Por favor, inicie sessão novamente.";
            localStorage.removeItem('token'); window.location.href = '/login';
        } 
        
        else if (data && data.message) {
            errorMessage = data.message;
        } 
        
        else if (data && typeof data === 'object') {
            const keys = Object.keys(data);
            if (keys.length > 0) {
                errorMessage = data[keys[0]]; 
            }
        }

        return Promise.reject(errorMessage);
    }
);

export default api;