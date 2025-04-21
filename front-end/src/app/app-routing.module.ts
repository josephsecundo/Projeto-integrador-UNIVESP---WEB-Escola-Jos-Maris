import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component'; // Novo componente para a página inicial
import { PageNotFoundComponent } from './page-not-found/page-not-found.component'; // Componente para rotas não encontradas

const routes: Routes = [
  { path: '', component: HomeComponent }, // Página inicial
  { path: '**', component: PageNotFoundComponent } // Página para rotas não encontradas
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }