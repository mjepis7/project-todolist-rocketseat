package br.com.mariaeduarda.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

// interface é um modelo/contrato dentro da aplicação, que pode conter os metodos mas não suas implementações, ou seja, tem apenas a representação dos seus metodos
// <> => a classe recebe um generator (atributos mais dinamicos)
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
  UserModel findByUsername(String username); // verificar se já existe um usuario cadastrado com esse username
}
