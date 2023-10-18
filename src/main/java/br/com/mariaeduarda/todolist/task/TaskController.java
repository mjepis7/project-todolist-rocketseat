package br.com.mariaeduarda.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mariaeduarda.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = request.getAttribute("idUser"); // salvar o id do usuario
    taskModel.setIdUser((UUID) idUser);

    var currentDate = LocalDateTime.now();
    // se a data de inicio ou de termino da tarefa for antes do dia atual
    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de inicio/termino deve ser maior que a data atual");
    }

    // se a data de inicio for depois da data de termino
    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de inicio deve ser menor que a data de termino");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  // lista as tasks de determinado usuario(id)
  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID) idUser);
    return tasks;
  }

  // http://localhost:8080/tasks/92bbff85-b3bf-4cbf-959a-9d1e9738a60e
  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

    var task = this.taskRepository.findById(id).orElse(null);

    // se a task não existir
    if (task == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
    }

    var idUser = request.getAttribute("idUser");

    // se o id que esta tentando modificar a task for diferente do id que criou
    if (!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O usuario não tem permissão para alterar essa tarefa");
    }

    // exibir os outros dados da task
    Utils.copyNonNullProperties(taskModel, task);
    var taskUpdated = this.taskRepository.save(task);

    return ResponseEntity.ok().body(taskUpdated);
  }

}
