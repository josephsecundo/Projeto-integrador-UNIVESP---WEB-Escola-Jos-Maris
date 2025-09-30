import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Login from './Components/LoginModal';
import PostarRecado from './Components/Administrativo';

// novos imports
import BookList from './Components/BookList';
import BookForm from './Components/BookForm';
import Borrowings from './Components/Borrowings';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/LoginModal" element={<Login />} />
        <Route path="/administrativo" element={<PostarRecado />} />

        {/* rotas da biblioteca */}
        <Route path="/books" element={<BookList />} />
        <Route path="/books/new" element={<BookForm />} />
        <Route path="/books/edit/:id" element={<BookForm />} />
        <Route path="/borrowings" element={<Borrowings />} />
      </Routes>
    </Router>
  );
}

export default App;
