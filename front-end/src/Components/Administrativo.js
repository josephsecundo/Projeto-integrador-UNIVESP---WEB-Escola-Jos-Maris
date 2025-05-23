import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../Styles/Administrativo.css';
import CadastroUsuarioModal from './CadastroUsuario';


function AlterarSenhaModal({ aberto, onClose, onSubmit, senhaAtual, setSenhaAtual, novaSenha, setNovaSenha, confirmarSenha, setConfirmarSenha, mensagemSenha }) {
  if (!aberto) return null;
  return (
    <div className="modal-backdrop">
      <div className="modal-cadastro">
        <h2>Alterar Senha</h2>
        <form onSubmit={onSubmit}>
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
          <div className="botoes-modal">
            <button type="submit">Alterar Senha</button>
            <button type="button" className="modal-close" onClick={onClose}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
}

function PostarRecado() {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [dataValidade, setDataValidade] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [recados, setRecados] = useState([]);
  const [editando, setEditando] = useState(null);
  const [exibirAlterarSenha, setExibirAlterarSenha] = useState(false);
  const [senhaAtual, setSenhaAtual] = useState('');
  const [novaSenha, setNovaSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [mensagemSenha, setMensagemSenha] = useState('');
  const [mostrarPainel, setMostrarPainel] = useState(false);
  const [modalCadastroUsuario, setModalCadastroUsuario] = useState(false);

  useEffect(() => {
    document.title = 'Administrativo';
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editando) {
        await axios.put(`http://localhost:8081/api/recados/atualizar/${editando}`, {
          titulo,
          descricao,
          dataValidade,
        });
        setMensagem('Recado atualizado com sucesso!');
      } else {
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
        setRecados([...recados, response.data]);
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

  const inativarRecado = async (id) => {
    try {
      await axios.put(`http://localhost:8081/api/recados/inativar/${id}`);
      setRecados(recados.map((recado) =>
        recado.codigo === id ? { ...recado, ativo: false } : recado
      ));
      setMensagem('Recado inativado com sucesso!');
    } catch (err) {
      console.error('Erro ao inativar recado:', err);
      setMensagem('Erro ao inativar recado.');
    }
  };

  const reativarRecado = async (id) => {
    try {
      await axios.put(`http://localhost:8081/api/recados/reativar/${id}`);
      setRecados(recados.map((recado) =>
        recado.codigo === id ? { ...recado, ativo: true } : recado
      ));
      setMensagem('Recado reativado com sucesso!');
    } catch (err) {
      console.error('Erro ao reativar recado:', err);
      setMensagem('Erro ao reativar recado.');
    }
  };

  const iniciarEdicao = (recado) => {
    setEditando(recado.codigo);
    setTitulo(recado.titulo);
    setDescricao(recado.descricao);
    setDataValidade(recado.dataValidade);
  };

  const handleAlterarSenha = async (e) => {
    e.preventDefault();
    if (novaSenha !== confirmarSenha) {
      setMensagemSenha('As senhas não coincidem.');
      return;
    }
    try {
      const codigoUsuario = 1;
      await axios.put(
        `http://localhost:8081/api/usuario/alterar-senha?codigo=${codigoUsuario}&senhaAtual=${encodeURIComponent(senhaAtual)}&novaSenha=${encodeURIComponent(novaSenha)}`
      );
      setMensagemSenha('Senha alterada com sucesso!');
      setSenhaAtual('');
      setNovaSenha('');
      setConfirmarSenha('');
      setExibirAlterarSenha(false);
    } catch (err) {
      console.error('Erro ao alterar senha:', err);
      if (err.response && err.response.data) {
        setMensagemSenha(err.response.data);
      } else {
        setMensagemSenha('Erro ao alterar senha.');
      }
    }
  };
  
  return (
    <div>
      <div className="painel-topo">
        <button
          className="pagina-inicial-btn"
          onClick={() => window.location.href = '/'}
        >
          Página Inicial
        </button>
        <button
          className="painel-btn"
          onClick={() => setMostrarPainel((prev) => !prev)}
        >
          Painel
        </button>
        {mostrarPainel && (
          <>
            <button
              className="alterar-senha-btn"
              onClick={() => setExibirAlterarSenha(true)}
            >
              Alterar Senha
            </button>
            <button
              className="cadastrar-usuario-btn"
              onClick={() => setModalCadastroUsuario(true)}
            >
              Cadastrar Usuário
            </button>
            <CadastroUsuarioModal
              aberto={modalCadastroUsuario}
              onClose={() => setModalCadastroUsuario(false)}
            />
          </>
        )}
      </div>

      <AlterarSenhaModal
        aberto={exibirAlterarSenha}
        onClose={() => setExibirAlterarSenha(false)}
        onSubmit={handleAlterarSenha}
        senhaAtual={senhaAtual}
        setSenhaAtual={setSenhaAtual}
        novaSenha={novaSenha}
        setNovaSenha={setNovaSenha}
        confirmarSenha={confirmarSenha}
        setConfirmarSenha={setConfirmarSenha}
        mensagemSenha={mensagemSenha}
      />

      <div className="administrativo-container">
        <div className="recados-coluna">
          <h2>Recados Ativos</h2>
          <ul className="recados-lista">
            {recados.filter((recado) => recado.ativo).map((recado) => (
              <li key={recado.codigo} className="recado-item">
                <h3>{recado.titulo}</h3>
                <p>{recado.descricao}</p>
                <p>Validade: {
  recado.dataValidade
    ? (() => {
        const [ano, mes, dia] = recado.dataValidade.split('-');
        return `${dia}-${mes}-${ano}`;
      })()
    : ''
}</p>
                <div className="recado-acoes">
                  <button onClick={() => iniciarEdicao(recado)}>Editar</button>
                  <button onClick={() => inativarRecado(recado.codigo)}>Inativar</button>
                </div>
              </li>
            ))}
          </ul>
        </div>
        <div className="recados-coluna">
          <h2>Recados Inativos</h2>
          <ul className="recados-lista">
            {recados.filter((recado) => !recado.ativo).map((recado) => (
              <li key={recado.codigo} className="recado-item">
                <h3>{recado.titulo}</h3>
                <p>{recado.descricao}</p>
                <p>Validade: {
  recado.dataValidade
    ? (() => {
        const [ano, mes, dia] = recado.dataValidade.split('-');
        return `${dia}-${mes}-${ano}`;
      })()
    : ''
}</p>
                <div className="recado-acoes">
                  <button onClick={() => reativarRecado(recado.codigo)}>Reativar</button>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>

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
      </div>
    </div>
  );
}

export default PostarRecado;