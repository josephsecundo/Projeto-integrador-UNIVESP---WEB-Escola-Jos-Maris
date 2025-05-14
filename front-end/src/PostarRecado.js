import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './PostarRecado.css';

function PostarRecado() {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [recados, setRecados] = useState([]);
  const [editando, setEditando] = useState(null); // Armazena o ID do recado sendo editado

  // Carrega os recados existentes ao montar o componente
  useEffect(() => {
    const carregarRecados = async () => {
      try {
        const response = await axios.get('http://localhost:8081/api/recados');
        setRecados(response.data);
      } catch (err) {
        console.error('Erro ao carregar recados:', err);
      }
    };
    carregarRecados();
  }, []);

  // Função para postar ou editar um recado
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const dataPostagem = new Date().toLocaleDateString('pt-BR'); // Data atual no formato dd/MM/yyyy
      const dataValidade = new Date(new Date().setDate(new Date().getDate() + 7)).toLocaleDateString('pt-BR'); // Exemplo: validade de 7 dias

      if (editando) {
        // Editar recado existente
        console.log(`Editando recado com ID: ${editando}`);
        await axios.put(`http://localhost:8081/api/recados/atualizar/${editando}`, {
          titulo,
          descricao,
          dataPostagem,
          dataValidade,
        });
        setMensagem('Recado atualizado com sucesso!');
        setRecados(
          recados.map((recado) =>
            recado.codigo === editando
              ? { ...recado, titulo, descricao, dataPostagem, dataValidade }
              : recado
          )
        );
      } else {
        // Criar novo recado
        console.log('Criando novo recado');
        const response = await axios.post(
          'http://localhost:8081/api/recados/salvar',
          {
            titulo,
            descricao,
            dataPostagem,
            dataValidade,
          },
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        );
        setMensagem('Recado postado com sucesso!');
        setRecados([...recados, response.data]); // Adiciona o novo recado à lista
      }
      setTitulo('');
      setDescricao('');
      setEditando(null);
    } catch (err) {
      console.error('Erro ao postar ou editar recado:', err);
      setMensagem('Erro ao postar ou editar recado.');
    }
  };

  // Função para excluir um recado
  const excluirRecado = async (id) => {
    try {
      await axios.delete(`http://localhost:8081/api/recados/${id}`);
      setRecados(recados.filter((recado) => recado.id !== id)); // Remove o recado da lista
      setMensagem('Recado excluído com sucesso!');
    } catch (err) {
      console.error('Erro ao excluir recado:', err);
      setMensagem('Erro ao excluir recado.');
    }
  };

  // Função para iniciar a edição de um recado
  const iniciarEdicao = (recado) => {
    setEditando(recado.codigo); // Define o ID do recado sendo editado
    setTitulo(recado.titulo);
    setDescricao(recado.descricao);
  };

  return (
    <div className="postar-recado-container">
      <h2>{editando ? 'Editar Recado' : 'Postar Recado'}</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="titulo">Título:</label>
          <input
            type="text"
            id="titulo"
            value={titulo}
            onChange={(e) => setTitulo(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="descricao">Descrição:</label>
          <textarea
            id="descricao"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            required
          ></textarea>
        </div>
        {mensagem && <p className="mensagem">{mensagem}</p>}
        <button type="submit">{editando ? 'Atualizar' : 'Postar'}</button>
      </form>

      <h2>Recados Existentes</h2>
      <ul className="recados-lista">
        {recados.map((recado) => (
          <li key={recado.id} className="recado-item">
            <h3>{recado.titulo}</h3>
            <p>{recado.descricao}</p>
            <div className="recado-acoes">
              <button onClick={() => iniciarEdicao(recado)}>Editar</button>
              <button onClick={() => excluirRecado(recado.id)}>Excluir</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PostarRecado;