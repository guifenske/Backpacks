package br.com.backpacks.storage;

import br.com.backpacks.utils.BackPack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SerializationUtils {

    public static List<String> serializeLocationToList(Location location) {
        List<String> data = new ArrayList<>();
        data.add(location.getWorld().getName());
        data.add(String.valueOf(location.getX()));
        data.add(String.valueOf(location.getY()));
        data.add(String.valueOf(location.getZ()));
        return data;
    }

    public static ByteArrayInputStream serializeLocationToStream(Location location) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        if(location == null){
            dataOutput.writeUTF("null");
            dataOutput.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        dataOutput.writeUTF(location.getWorld().getName());
        dataOutput.writeDouble(location.getX());
        dataOutput.writeDouble(location.getY());
        dataOutput.writeDouble(location.getZ());

        dataOutput.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static Location deserializeLocationFromStream(InputStream inputStream) throws IOException {
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        String world = dataInput.readUTF();
        if(world.equals("null")){
            dataInput.close();
            return null;
        }
        double x = dataInput.readDouble();
        double y = dataInput.readDouble();
        double z = dataInput.readDouble();

        dataInput.close();
        return new Location(Bukkit.getServer().getWorld(world), x, y, z);
    }

    public static Location deserializeLocationAsList(List<String> oldData) {
        String world = oldData.get(0);
        double x = Double.parseDouble(oldData.get(1));
        double y = Double.parseDouble(oldData.get(2));
        double z = Double.parseDouble(oldData.get(3));

        return new Location(Bukkit.getServer().getWorld(world), x, y, z);
    }

    public static ByteArrayInputStream serializeInventory(Inventory inventory) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        //write the inventory
        if(inventory == null){
            dataOutput.writeInt(0);
            dataOutput.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        }

        dataOutput.writeInt(inventory.getSize());
        for(int i = 0; i < inventory.getSize(); i++){
            dataOutput.writeObject(inventory.getItem(i));
        }

        dataOutput.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static void deserializeItemsToInventory(InputStream inputStream, Inventory inventory) throws IOException, ClassNotFoundException {
        if (inputStream == null || inputStream.available() == 0 || inventory == null) return;
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        int size = dataInput.readInt();
        // Read the serialized inventory
        if(size == 0){
            dataInput.close();
            return;
        }
        for (int i = 0; i < size; i++) {
            inventory.setItem(i, (ItemStack) dataInput.readObject());
        }

        dataInput.close();
    }

    public static ByteArrayInputStream serializeUpgradesIds(BackPack backPack) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        if(backPack.getUpgradesIds().isEmpty()){
            dataOutput.writeInt(0);
            dataOutput.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        }

        dataOutput.writeInt(backPack.getUpgradesIds().size());
        for(Integer upgrade : backPack.getUpgradesIds()){
            dataOutput.writeInt(upgrade);
        }

        dataOutput.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static List<Integer> deserializeUpgradesIds(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null || inputStream.available() == 0) return new ArrayList<>();
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        List<Integer> list = new ArrayList<>();
        int size = dataInput.readInt();
        if(size == 0){
            dataInput.close();
            return list;
        }

        for(int i = 0; i < size; i++){
            list.add(dataInput.readInt());
        }

        dataInput.close();
        return list;
    }

}
