import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Styles/index.css';
import Infraestrutura from './Components/Infraestrutura';
import { useNavigate } from 'react-router-dom';

function Home() {
  document.title = 'Página Inicial - Professor José Maris';
  const [recados, setRecados] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate(); // Hook para navegação

  function formatarData(data) {
    const [dia, mes, ano] = data.split('/');
    return new Date(`${ano}-${mes}-${dia}`);
  }

  useEffect(() => {
    axios
      .get('http://localhost:8081/api/recados/ativos')
      .then((response) => {
        console.log('Dados recebidos:', response.data);
        setRecados(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Erro ao buscar recados:', error);
        setLoading(false);
      });
  }, []);

  const handleLoginClick = () => {
    navigate('/login');
  };

  return (
    <div className="home">
      <header className="home-header">
        <h1>BEM VINDO À PROFESSOR JOSE MARIS</h1>
        <p>Esta é a página oficial da nossa escola!</p>
        <button className="login-button" onClick={handleLoginClick}>
          SouAdmin
        </button>
      </header>
      <section className="home-location">
        <h2>Localização</h2>
        <p>Emeief Professor José Maris</p>
        <p>Rua Mario Menegatti, 415</p>
        <p>Vila Romana</p>
        <p>CEP: 17602-550</p>
        <p>Tupã, SP</p>
        <div className="map-container">
          <iframe
            title="Google Maps"
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3675.09123456789!2d-50.512345678!3d-21.123456789!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x949123456789abcd%3A0x123456789abcdef!2sRua%20Mario%20Menegatti%2C%20415%20-%20Vila%20Romana%2C%20Tup%C3%A3%20-%20SP%2C%2017602-550!5e0!3m2!1spt-BR!2sbr!4v1234567890123"
            width="100%"
            height="150"
            style={{ border: 0 }}
            allowFullScreen=""
            loading="lazy"
          ></iframe>
        </div>
      </section>
      <Infraestrutura /> {/* Adiciona o componente Infraestrutura */}
      <section className="home-mural">
        <h2>Mural de Recados</h2>
        {loading ? (
          <p>Carregando recados...</p>
        ) : recados.length > 0 ? (
          <ul>
            {recados.map((recado) => (
              <li key={recado.codigo}>
                <h3>{recado.titulo}</h3>
                <p>{recado.descricao}</p>
                <small>
                  Publicado em: {recado.dataPostagem ? formatarData(recado.dataPostagem).toLocaleDateString() : "Data inválida"}
                </small>
              </li>
            ))}
          </ul>
        ) : (
          <p>Nenhum recado disponível no momento.</p>
        )}
      </section>
    </div>
  );
}

export default Home;