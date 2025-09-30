import React, { useState } from 'react';
import '../Styles/ListadeLivros.css';

export default function BookList() {
  // Mock de dados iniciais
  const [livros, setLivros] = useState([
    { id: 1, titulo: "Dom Casmurro", autor: "Machado de Assis", ano: 1899, exemplares: 3, disponiveis: 2 }
  ]);

  const [showForm, setShowForm] = useState(false);
  const [editando, setEditando] = useState(null);
  const [novoLivro, setNovoLivro] = useState({ titulo: "", autor: "", ano: "", exemplares: 1, disponiveis: 1 });

  function handleSalvar(e) {
    e.preventDefault();
    if (editando) {
      // Atualizar
      setLivros(livros.map(l => l.id === editando ? { ...novoLivro, id: editando } : l));
    } else {
      // Criar
      const id = livros.length + 1;
      setLivros([...livros, { ...novoLivro, id }]);
    }
    setNovoLivro({ titulo: "", autor: "", ano: "", exemplares: 1, disponiveis: 1 });
    setEditando(null);
    setShowForm(false);
  }

  function handleEditar(livro) {
    setNovoLivro(livro);
    setEditando(livro.id);
    setShowForm(true);
  }

  function handleExcluir(id) {
    if (window.confirm("Deseja realmente excluir este livro?")) {
      setLivros(livros.filter(l => l.id !== id));
    }
  }

  return (
    <div className="book-page">
      <header>
        <h2>Biblioteca</h2>
        <button onClick={() => setShowForm(true)}>+ Novo Livro</button>
      </header>

      {livros.length === 0 ? <p>Nenhum livro cadastrado.</p> : (
        <div className="table-responsive">
          <table className="book-table">
            <thead>
              <tr>
                <th>Título</th>
                <th>Autor</th>
                <th>Ano</th>
                <th>Exemplares</th>
                <th>Disponíveis</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {livros.map(l => (
                <tr key={l.id}>
                  <td>{l.titulo}</td>
                  <td>{l.autor}</td>
                  <td>{l.ano}</td>
                  <td>{l.exemplares}</td>
                  <td>{l.disponiveis}</td>
                  <td>
                    <button className="btn-edit" onClick={() => handleEditar(l)}>Editar</button>
                    <button className="btn-delete" onClick={() => handleExcluir(l.id)}>Excluir</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Formulário Modal */}
      {showForm && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>{editando ? "Editar Livro" : "Novo Livro"}</h3>
            <form onSubmit={handleSalvar}>
              <label>Título
                <input value={novoLivro.titulo} onChange={e=>setNovoLivro({...novoLivro, titulo: e.target.value})} required />
              </label>
              <label>Autor
                <input value={novoLivro.autor} onChange={e=>setNovoLivro({...novoLivro, autor: e.target.value})} required />
              </label>
              <label>Ano
                <input type="number" value={novoLivro.ano} onChange={e=>setNovoLivro({...novoLivro, ano: e.target.value})} />
              </label>
              <label>Exemplares
                <input type="number" min="1" value={novoLivro.exemplares} onChange={e=>setNovoLivro({...novoLivro, exemplares: parseInt(e.target.value)})} />
              </label>
              <label>Disponíveis
                <input type="number" min="0" value={novoLivro.disponiveis} onChange={e=>setNovoLivro({...novoLivro, disponiveis: parseInt(e.target.value)})} />
              </label>
              <div className="actions">
                <button type="submit">Salvar</button>
                <button type="button" onClick={()=>{ setShowForm(false); setEditando(null); }}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
