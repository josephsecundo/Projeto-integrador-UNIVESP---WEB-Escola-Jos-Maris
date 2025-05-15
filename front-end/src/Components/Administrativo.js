import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../Styles/Administrativo.css';

function PostarRecado() {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [dataValidade, setDataValidade] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [recados, setRecados] = useState([]);
  const [editando, setEditando] = useState(null); // Armazena o ID do recado sendo editado
  const [exibirAlterarSenha, setExibirAlterarSenha] = useState(false); // Estado para exibir alterar senha
  const [senhaAtual, setSenhaAtual] = useState('');
  const [novaSenha, setNovaSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [mensagemSenha, setMensagemSenha] = useState('');

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
      if (editando) {
        // Editar recado existente
        await axios.put(`http://localhost:8081/api/recados/atualizar/${editando}`, {
          titulo,
          descricao,
          dataValidade,
        });
        setMensagem('Recado atualizado com sucesso!');
      } else {
        // Criar novo recado
        const response = await axios.post(
          'http://localhost:8081/api/recados/salvar',
          {
            titulo,
            descricao,
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
      setDataValidade('');
      setEditando(null);
    } catch (err) {
      console.error('Erro ao postar ou editar recado:', err);
      setMensagem('Erro ao postar ou editar recado.');
    }
  };

  // Função para inativar um recado
  const inativarRecado = async (id) => {
    try {
      await axios.put(`http://localhost:8081/api/recados/inativar/${id}`);
      setRecados(recados.map((recado) =>
        recado.codigo === id ? { ...recado, ativo: false } : recado
      )); // Atualiza o estado local para refletir a mudança
      setMensagem('Recado inativado com sucesso!');
    } catch (err) {
      console.error('Erro ao inativar recado:', err);
      setMensagem('Erro ao inativar recado.');
    }
  };

  // Função para reativar um recado
  const reativarRecado = async (id) => {
    try {
      await axios.put(`http://localhost:8081/api/recados/reativar/${id}`);
      setRecados(recados.map((recado) =>
        recado.codigo === id ? { ...recado, ativo: true } : recado
      )); // Atualiza o estado local para refletir a mudança
      setMensagem('Recado reativado com sucesso!');
    } catch (err) {
      console.error('Erro ao reativar recado:', err);
      setMensagem('Erro ao reativar recado.');
    }
  };

  // Função para iniciar a edição de um recado
  const iniciarEdicao = (recado) => {
    setEditando(recado.codigo);
    setTitulo(recado.titulo);
    setDescricao(recado.descricao);
    setDataValidade(recado.dataValidade);
  };

  // Função para alterar senha
  const handleAlterarSenha = async (e) => {
    e.preventDefault();
    if (novaSenha !== confirmarSenha) {
      setMensagemSenha('As senhas não coincidem.');
      return;
    }
    try {
      await axios.put('http://localhost:8081/api/usuarios/alterar-senha', {
        senhaAtual,
        novaSenha,
      });
      setMensagemSenha('Senha alterada com sucesso!');
      setSenhaAtual('');
      setNovaSenha('');
      setConfirmarSenha('');
      setExibirAlterarSenha(false);
    } catch (err) {
      console.error('Erro ao alterar senha:', err);
      setMensagemSenha('Erro ao alterar senha.');
    }
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
        <div className="form-group">
          <label htmlFor="dataValidade">Data de Validade:</label>
          <input
            type="date"
            id="dataValidade"
            value={dataValidade}
            onChange={(e) => setDataValidade(e.target.value)}
            required
          />
        </div>
        {mensagem && <p className="mensagem">{mensagem}</p>}
        <button type="submit">{editando ? 'Atualizar' : 'Postar'}</button>
      </form>

      <div className="alterar-senha-container">
        <button onClick={() => setExibirAlterarSenha(true)}>Alterar Senha</button>
      </div>

      {exibirAlterarSenha && (
        <div className="alterar-senha-form">
          <h3>Alterar Senha</h3>
          <form onSubmit={handleAlterarSenha}>
            <div className="form-group">
              <label htmlFor="senhaAtual">Senha Atual:</label>
              <input
                type="password"
                id="senhaAtual"
                value={senhaAtual}
                onChange={(e) => setSenhaAtual(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="novaSenha">Nova Senha:</label>
              <input
                type="password"
                id="novaSenha"
                value={novaSenha}
                onChange={(e) => setNovaSenha(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="confirmarSenha">Confirmar Nova Senha:</label>
              <input
                type="password"
                id="confirmarSenha"
                value={confirmarSenha}
                onChange={(e) => setConfirmarSenha(e.target.value)}
                required
              />
            </div>
            {mensagemSenha && <p className="mensagem">{mensagemSenha}</p>}
            <button type="submit">Alterar Senha</button>
            <button type="button" onClick={() => setExibirAlterarSenha(false)}>Cancelar</button>
          </form>
        </div>
      )}

      <h2>Recados Existentes</h2>
      <ul className="recados-lista">
        {recados.filter((recado) => recado.ativo).map((recado) => (
          <li key={recado.codigo} className="recado-item">
            <h3>{recado.titulo}</h3>
            <p>{recado.descricao}</p>
            <p>Validade: {recado.dataValidade}</p>
            <div className="recado-acoes">
              <button onClick={() => iniciarEdicao(recado)}>Editar</button>
              <button onClick={() => inativarRecado(recado.codigo)}>Inativar</button>
            </div>
          </li>
        ))}
      </ul>

      <h2>Recados Inativos</h2>
      <ul className="recados-lista">
        {recados.filter((recado) => !recado.ativo).map((recado) => (
          <li key={recado.codigo} className="recado-item">
            <h3>{recado.titulo}</h3>
            <p>{recado.descricao}</p>
            <p>Validade: {recado.dataValidade}</p>
            <div className="recado-acoes">
              <button onClick={() => reativarRecado(recado.codigo)}>Reativar</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PostarRecado;