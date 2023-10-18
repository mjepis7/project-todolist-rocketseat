package br.com.mariaeduarda.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.mariaeduarda.todolist.user.IUserRepository;
// servlet é a base para qualquer framework do Java
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // coloca-se em toda classe que precisa ser gerenciada pelo spring
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var serveletPath = request.getServletPath();

    // verificar se o que esta sendo chamado é a rota de tasks
    // utiliza-se startsWith porque vai ter um parametro depois
    if (serveletPath.startsWith("/tasks/")) {

      // Pegar a autenticação (usuario e senha)
      var authorization = request.getHeader("Authorization");

      // subtring é um metodo utilizado para extrair uma parte de um texto/conteudo
      // .trim() - remove os espaços vazios
      var authEncoded = authorization.substring("Basic".length()).trim();

      byte[] authDecode = Base64.getDecoder().decode(authEncoded); // converte em array de bytes

      var authString = new String(authDecode); // decodifica

      // ["mjepi", "1234"]
      String[] credentials = authString.split(":"); // cria um array com o usuario e senha
      String username = credentials[0];
      String password = credentials[1];

      // Validar usuario
      var user = this.userRepository.findByUsername(username);

      if (user == null) {
        response.sendError(401); // usuario sem autorização (não cadastrado)
      } else {

        // Validar senha
        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()); // verifica a senha
        if (passwordVerify.verified) {
          // Continuar
          request.setAttribute("idUser", user.getId());
          // request é tudo que ta vindo da aplicação
          // responde é tudo que ta sendo enviando ao usuario
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401);
        }

      }
    } else {
      filterChain.doFilter(request, response);
    }

  }

}
