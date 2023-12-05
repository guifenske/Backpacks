package br.com.backpacks;

import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.entity.Player;

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

    public void updateDiscoveredRecipes(Player player){
        Future<Void> future = executor.submit(() -> {
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK());
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK());
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK());
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK());
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK());
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK());
            player.discoverRecipe(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK());
            return null;
        });
    }


    public void encerrar() {
        // Encerra o executor quando não precisar mais
        executor.shutdown();
    }
}