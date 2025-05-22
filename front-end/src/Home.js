import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Styles/index.css';
import Infraestrutura from './Components/Infraestrutura';
import { useNavigate } from 'react-router-dom';
import LoginModal from './Components/LoginModal';

function Home() {
  document.title = 'Página Inicial - Professor José Maris';
  const [recados, setRecados] = useState([]);
  const [loading, setLoading] = useState(true);
  const [weather, setWeather] = useState(null);
  const [loginModalAberto, setLoginModalAberto] = useState(false);
  const navigate = useNavigate();

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

  useEffect(() => {
    fetch('https://api.openweathermap.org/data/2.5/weather?q=Tupã,BR&appid=8c22d333854ba1dc99caa54b1357163d&units=metric&lang=pt_br')
      .then(res => res.json())
      .then(data => setWeather(data))
      .catch(() => setWeather(null));
  }, []);

  const handleLoginClick = () => {
    navigate('/LoginModal');
  };

  return (
    <div className="home">
      <div className="home-images-coluna">
        <img src="/images/escola.jpg" alt="Imagem 1" className="home-img" />
        <img src="/images/escolaFachada.jpg" alt="Imagem 2" className="home-img" />
        <img src="/images/escolaPanoramica.jpg" alt="Imagem 3" className="home-img" />
      </div>
      <div className='home-content'>
      <header className="home-header">
        <h1>BEM VINDO À ESCOLA PROFESSOR JOSÉ MARIS</h1>
        <p>Esta é a página oficial da nossa escola!</p>
        <button
          className="login-button"
          onClick={() => setLoginModalAberto(true)}
        >
          SouAdmin
        </button>
        <LoginModal
          aberto={loginModalAberto}
          onClose={() => setLoginModalAberto(false)}
          onEsqueceuSenha={() => alert('Função de recuperação de senha!')}
        />
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
      <section className="home-weather">
        <h2>Previsão do Tempo</h2>
        {weather && weather.main ? (
          <div>
            <p>
              {weather.name} - {weather.weather[0].description}
            </p>
            <p>
              Temperatura: {Math.round(weather.main.temp)}°C
            </p>
            <p>
              Umidade: {weather.main.humidity}%
            </p>
          </div>
        ) : (
          <p>Carregando previsão...</p>
        )}
      </section>
      <Infraestrutura /> {}
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
    </div>
  );
}

export default Home;