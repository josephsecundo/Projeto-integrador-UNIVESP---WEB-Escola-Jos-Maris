import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Login from './Components/LoginModal';
import PostarRecado from './Components/Administrativo';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/LoginModal" element={<Login />} />
        <Route path="/administrativo" element={<PostarRecado />} />
      </Routes>
    </Router>
  );
}

export default App;
