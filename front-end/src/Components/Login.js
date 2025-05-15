import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // Importa o hook para navegação
import '../Styles/Login.css';

function Login() {
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate(); // Inicializa o hook

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Tentando login com:', { login: usuario, senha });
    try {
      const response = await axios.post(
        'http://localhost:8081/api/usuario/login',
        {
          login: usuario,
          senha,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );
      console.log('Resposta do servidor:', response.data);
      alert('Login bem-sucedido!');
      navigate('/administrativo'); // Certifique-se de que o caminho está correto
    } catch (err) {
      console.error('Erro ao tentar fazer login:', err);
      if (err.response && err.response.status === 401) {
        setError('Senha incorreta.');
      } else if (err.response && err.response.status === 404) {
        setError('Usuário não encontrado.');
      } else {
        setError('Erro ao tentar fazer login.');
      }
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="login">Login:</label>
          <input
            type="text"
            id="usuario"
            value={usuario}
            onChange={(e) => setUsuario(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="senha">Senha:</label>
          <input
            type="password"
            id="senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />
        </div>
        {error && <p className="error">{error}</p>}
        <button type="submit">Entrar</button>
      </form>
    </div>
  );
}

export default Login;