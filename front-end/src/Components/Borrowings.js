import React, { useState } from 'react';
import '../Styles/Borrowings.css';

export default function Borrowings() {
  // Mock de dados iniciais
  const [list, setList] = useState([
    { id: 1, livro: "Dom Casmurro", usuario: "Maria", data: "2025-09-01", vencimento: "2025-09-15", status: "Emprestado" }
  ]);

  const [showForm, setShowForm] = useState(false);
  const [novoEmprestimo, setNovoEmprestimo] = useState({ livro: "", usuario: "", vencimento: "" });

  function handleDevolver(id) {
    setList(list.map(b => b.id === id ? { ...b, status: "Devolvido" } : b));
  }

  function handleSubmit(e) {
    e.preventDefault();
    const id = list.length + 1;
    setList([
      ...list,
      {
        id,
        data: new Date().toISOString(),
        status: "Emprestado",
        ...novoEmprestimo
      }
    ]);
    setNovoEmprestimo({ livro: "", usuario: "", vencimento: "" });
    setShowForm(false);
  }

  return (
    <div className="borrowings-page">
      <header>
        <h2>Empréstimos</h2>
        <button onClick={() => setShowForm(true)}>+ Novo Empréstimo</button>
      </header>

      {list.length === 0 ? <p>Nenhum empréstimo ativo.</p> : (
        <div className="table-responsive">
          <table className="borrowings-table">
            <thead>
              <tr>
                <th>Livro</th>
                <th>Usuário</th>
                <th>Data</th>
                <th>Vencimento</th>
                <th>Status</th>
                <th>Ação</th>
              </tr>
            </thead>
            <tbody>
              {list.map(b => (
                <tr key={b.id}>
                  <td>{b.livro}</td>
                  <td>{b.usuario}</td>
                  <td>{new Date(b.data).toLocaleDateString()}</td>
                  <td>{b.vencimento}</td>
                  <td className={`status ${b.status.toLowerCase()}`}>{b.status}</td>
                  <td>
                    {b.status === "Emprestado" && (
                      <button className="btn-return" onClick={() => handleDevolver(b.id)}>Devolver</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Formulário modal */}
      {showForm && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Novo Empréstimo</h3>
            <form onSubmit={handleSubmit}>
              <label>Livro
                <input value={novoEmprestimo.livro} onChange={e=>setNovoEmprestimo({...novoEmprestimo, livro: e.target.value})} required/>
              </label>
              <label>Usuário
                <input value={novoEmprestimo.usuario} onChange={e=>setNovoEmprestimo({...novoEmprestimo, usuario: e.target.value})} required/>
              </label>
              <label>Data de Vencimento
                <input type="date" value={novoEmprestimo.vencimento} onChange={e=>setNovoEmprestimo({...novoEmprestimo, vencimento: e.target.value})} required/>
              </label>
              <div className="actions">
                <button type="submit">Salvar</button>
                <button type="button" onClick={()=>setShowForm(false)}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
