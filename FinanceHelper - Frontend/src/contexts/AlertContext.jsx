import { createContext, useState, useContext } from 'react';
import AlertModel from '../components/Modal/AlertModal.component'; 

const AlertContext = createContext();

export function AlertProvider({ children }) {
  const [alertConfig, setAlertConfig] = useState({
    isOpen: false,
    message: '',
    type: 'error',
    title: ''
  });

  const showAlert = (message, type = 'error', title = null) => {
    const defaultTitle = title || (type === 'error' ? 'Erro' : 'Sucesso');
    setAlertConfig({
      isOpen: true,
      message,
      type,
      title: defaultTitle
    });
  };

  const hideAlert = () => {
    setAlertConfig((prev) => ({ ...prev, isOpen: false }));
  };

  return (
    <AlertContext.Provider value={{ showAlert, hideAlert }}>
      {children}
      <AlertModel
        isOpen={alertConfig.isOpen}
        title={alertConfig.title}
        message={alertConfig.message}
        type={alertConfig.type}
        onClose={hideAlert}
      />
    </AlertContext.Provider>
  );
}


export const useAlert = () => useContext(AlertContext);