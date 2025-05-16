import React, { useState } from "react";
import axios from "axios";
import "../Styles/CadastroUsuario.css";


const CadastroUsuarioModal = ({ aberto, onClose }) => {
  const [formData, setFormData] = useState({
    nome: "",
    email: "",
    login: "",
    senha: "",
  });
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const validateForm = () => {
    const newErrors = {};
    if (!formData.nome) newErrors.nome = "Nome é obrigatório.";
    else if (formData.nome.length > 50) newErrors.nome = "Nome deve ter no máximo 50 caracteres.";
    if (!formData.email) newErrors.email = "Email é obrigatório.";
    else if (!/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(formData.email)) newErrors.email = "Email inválido.";
    else if (formData.email.length > 50) newErrors.email = "Email deve ter no máximo 50 caracteres.";
    if (!formData.login) newErrors.login = "Login é obrigatório.";
    else if (formData.login.length > 15) newErrors.login = "Login deve ter no máximo 15 caracteres.";
    if (!formData.senha) newErrors.senha = "Senha é obrigatória.";
    else if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/.test(formData.senha))
      newErrors.senha = "A senha deve conter pelo menos 8 caracteres, incluindo letras, números e um caractere especial.";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMessage("");
    setErrorMessage("");
    if (!validateForm()) return;
    try {
      await axios.post(
        "http://localhost:8081/api/usuario/salvar",
        formData
      );
      setSuccessMessage("Usuário cadastrado com sucesso!");
      setFormData({ nome: "", email: "", login: "", senha: "" });
    } catch (error) {
      if (
        error.response?.data?.message &&
        error.response.data.message.includes("Duplicate entry")
      ) {
        setErrorMessage("E-mail já cadastrado. Por favor, utilize outro e-mail.");
      } else {
        setErrorMessage(
          error.response?.data?.message || "Erro ao cadastrar usuário. Tente novamente."
        );
      }
    }
  };

  if (!aberto) return null;

  return (
    <div className="modal-backdrop">
      <div className="modal-cadastro">
        <h2>Cadastro de Usuário</h2>
        <p style={{ color: "#294781", marginBottom: 10, fontSize: "0.98rem" }}>
          Após o cadastro, você receberá um e-mail para confirmação.
        </p>
        {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
        {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
        <form onSubmit={handleSubmit}>
          <div>
            <label>Nome:</label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              onChange={handleInputChange}
            />
            {errors.nome && <p style={{ color: "red" }}>{errors.nome}</p>}
          </div>
          <div>
            <label>Email:</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
            />
            {errors.email && <p style={{ color: "red" }}>{errors.email}</p>}
          </div>
          <div>
            <label>Login:</label>
            <input
              type="text"
              name="login"
              value={formData.login}
              onChange={handleInputChange}
            />
            {errors.login && <p style={{ color: "red" }}>{errors.login}</p>}
          </div>
          <div>
            <label>Senha:</label>
            <input
              type="password"
              name="senha"
              value={formData.senha}
              onChange={handleInputChange}
            />
            {errors.senha && <p style={{ color: "red" }}>{errors.senha}</p>}
          </div>
          <div className="botoes-modal">
            <button type="submit">Cadastrar</button>
            <button type="button" className="modal-close" onClick={onClose}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
};
export default CadastroUsuarioModal;