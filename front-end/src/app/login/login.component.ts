import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    standalone: false
})
export class LoginComponent {
  login: string = '';
  senha: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const loginData = { login: this.login, senha: this.senha };

    this.http.post('http://localhost:8081/api/login', loginData).subscribe({
      next: (response: any) => {
        // Sucesso: redirecionar para a página inicial
        console.log('Login bem-sucedido:', response);
        this.router.navigate(['/']);
      },
      error: (error) => {
        // Erro: exibir mensagem de erro
        console.error('Erro no login:', error);
        alert('Usuário ou senha inválidos.');
      }
    });
  }
}
