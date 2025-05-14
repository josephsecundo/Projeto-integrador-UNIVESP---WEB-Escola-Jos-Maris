import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Login from './Login';
import PostarRecado from './PostarRecado';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/postar-recado" element={<PostarRecado />} />
      </Routes>
    </Router>
  );
}

export default App;
