package br.com.mariaeduarda.todolist; // pacote onde a classe esta inserida

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @ => sempre tem uma função por tras dela
@SpringBootApplication // executa a aplicação
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

}
