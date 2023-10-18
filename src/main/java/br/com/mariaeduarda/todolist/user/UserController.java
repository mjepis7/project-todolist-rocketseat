package br.com.mariaeduarda.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/*
  * Metodos de acesso do HTTP para realizar requisições:
  * GET - Buscar uma informação
  * POST - Adicionar um dado/informação
  * PUT - Alterar um dado/informação
  * DELETE - Remover um dado
  * PATCH - Alterar somente uma parte do dado/informação

  * Modificadores:
  * public - qualquer um
  * private - restrito
  * protected - mesma estrutura do pacote que tem acesso
*/

@RestController // usado para contruir uma API
@RequestMapping("/users")
// metodo de uma classe do Java = identificador + tipo de retorno + nome +
// paramentros
public class UserController {

  @Autowired // spring gerencia todo o ciclo de vida
  private IUserRepository userRepository;
  /*
   * String (texto)
   * Integer (inteiro)
   * Double (numeros mais precisos)
   * Float (numeros mais precisos)
   * Char (caractere)
   * Date (data)
   * void (sem retorno)
   */

  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel userModel) {
    var user = this.userRepository.findByUsername(userModel.getUsername());

    // se já existir um usuário cadastrado
    if (user != null) {
      System.out.println("Usuário já cadastrado"); // mensagem de erro
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado"); // status code
    }

    // criptografa a senha
    var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

    userModel.setPassword(passwordHashed);

    /*
     * exibe no prompt
     * System.out.println(userModel.getUsername());
     */
    var userCreated = this.userRepository.save(userModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}
