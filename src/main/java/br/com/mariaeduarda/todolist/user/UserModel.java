package br.com.mariaeduarda.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // coloca os getters e setters automaticamente
@Entity(name = "tb_users") // nome da tabela do banco de dados
public class UserModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id; // id da tabela no formato UUID (sequencia aleatoria de letras e numeros)

  // O identificador por default é public
  @Column(unique = true) // coluna com restrição para definir um atributo unico
  private String username;
  private String name;
  private String password;

  @CreationTimestamp
  private LocalDateTime createAt;

  /*
   * getters - buscar informação do que tem dentro do atributo
   * setters - atualizar/inserir/definir um valor para o atributo privado de uma
   * classe
   */
}
