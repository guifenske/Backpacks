package br.com.backpacks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadBackpacks {
    private ExecutorService executor;

    public ThreadBackpacks() {
        // Cria um ExecutorService com uma única thread
        executor = Executors.newSingleThreadExecutor();
    }

    //example for now
    public void saveAll() {
        Future<Void> future = executor.submit(() -> {
            // Lógica para salvar arquivos
            return null;
        });

        /* Tratamento de exceções, se necessário
        try {
            future.get(); // Aguarda a conclusão da tarefa
        } catch (InterruptedException | ExecutionException e) {
            // Tratar a exceção de acordo com sua lógica
        }
        */

    }


    public void encerrar() {
        // Encerra o executor quando não precisar mais
        executor.shutdown();
    }
}