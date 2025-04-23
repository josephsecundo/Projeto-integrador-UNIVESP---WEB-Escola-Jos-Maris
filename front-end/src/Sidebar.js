import React from 'react';
import './Sidebar.css';

function Sidebar() {
  return (
    <div className="sidebar">
      <h2>Menu</h2>
      <ul>
        <li><a href="#infraestrutura">Infraestrutura</a></li>
        <li><a href="#localizacao">Localização</a></li>
        <li><a href="#mural">Mural de Recados</a></li>
      </ul>
    </div>
  );
}

export default Sidebar;