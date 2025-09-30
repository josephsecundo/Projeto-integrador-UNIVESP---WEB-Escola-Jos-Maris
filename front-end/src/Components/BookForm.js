import React, { useEffect, useState } from 'react';
import api from './services/api';
import { useNavigate, useParams } from 'react-router-dom';

export default function BookForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState({
    title:'', author:'', isbn:'',
    total_copies:1, available_copies:1,
    year:'', category:'', description:''
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) {
      api.get(`/books/${id}`).then(res => setBook(res.data)).catch(console.error);
    }
  }, [id]);

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    try {
      if (id) await api.put(`/books/${id}`, book);
      else await api.post('/books', book);
      navigate('/books');
    } catch (err) {
      console.error('Erro salvando livro', err);
      alert('Erro ao salvar livro');
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="book-form">
      <h2>{id ? 'Editar Livro' : 'Novo Livro'}</h2>

      <label>Título
        <input value={book.title} onChange={e=>setBook({...book, title:e.target.value})} required/>
      </label>

      <label>Autor
        <input value={book.author} onChange={e=>setBook({...book, author:e.target.value})}/>
      </label>

      <label>ISBN
        <input value={book.isbn} onChange={e=>setBook({...book, isbn:e.target.value})}/>
      </label>

      <label>Ano
        <input type="number" value={book.year} onChange={e=>setBook({...book, year: e.target.value})}/>
      </label>

      <label>Quantidade total
        <input type="number" min="1" value={book.total_copies} onChange={e=>setBook({...book, total_copies: parseInt(e.target.value)})}/>
      </label>

      <label>Disponíveis
        <input type="number" min="0" value={book.available_copies} onChange={e=>setBook({...book, available_copies: parseInt(e.target.value)})}/>
      </label>

      <label>Categoria
        <input value={book.category} onChange={e=>setBook({...book, category:e.target.value})}/>
      </label>

      <label>Descrição
        <textarea value={book.description} onChange={e=>setBook({...book, description:e.target.value})}/>
      </label>

      <button type="submit" disabled={loading}>{loading ? 'Salvando...' : 'Salvar'}</button>
      <button type="button" onClick={()=>navigate('/books')}>Cancelar</button>
    </form>
  );
}
