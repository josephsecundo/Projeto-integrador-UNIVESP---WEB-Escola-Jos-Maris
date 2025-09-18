import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../Styles/Login.css';

const LoginModal = ({ aberto, onClose, onEsqueceuSenha }) => {
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [mensagem, setMensagem] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Tentando login com:', { login, senha });
    try {
      const response = await axios.post(
        'http://localhost:8081/api/usuario/login',
        {
          login,
          senha,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );
      console.log('Resposta do servidor:', response.data);
      // alert('Login bem-sucedido!');
      navigate('/administrativo');
    } catch (err) {
      console.error('Erro ao tentar fazer login:', err);
      if (err.response && err.response.status === 401) {
        setMensagem('Senha incorreta.');
      } else if (err.response && err.response.status === 404) {
        setMensagem('Usuário não encontrado.');
      } else {
        setMensagem('Erro ao tentar fazer login.');
      }
    }
  };

  if (!aberto) return null;

  return (
    <div className="modal-backdrop">
      <div className="modal-cadastro">
        <h2>Login</h2>
        {mensagem && <p style={{ color: 'red' }}>{mensagem}</p>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Login:</label>
            <input
              type="text"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Senha:</label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
          </div>
          <div className="botoes-modal">
            <button type="submit">Entrar</button>
            <button
              type="button"
              className="modal-close"
              onClick={onClose}
            >
              Cancelar
            </button>
          </div>
        </form>
        <button
          type="button"
          className="esqueceu-senha-btn"
          onClick={onEsqueceuSenha}
          style={{
            background: 'none',
            border: 'none',
            color: '#294781',
            textDecoration: 'underline',
            cursor: 'pointer',
            marginTop: '10px',
          }}
        >
          Esqueceu a senha?
        </button>
      </div>
    </div>
  );
};

export default LoginModal;