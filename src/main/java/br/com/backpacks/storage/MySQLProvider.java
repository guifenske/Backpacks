package br.com.backpacks.storage;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.*;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class MySQLProvider extends StorageProvider{
    private String url;
    private String user;
    private String password;
    private String databaseName;

    public MySQLProvider(String url, String user, String password, String databaseName) {
        super(StorageManager.StorageProviderType.MYSQL);
        this.url = url;
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(url, user, password);
        }   catch (SQLException | ClassNotFoundException e) {
            Main.getMain().getLogger().severe("Could not connect to MySQL... Using YAML as default storage provider.");
            StorageManager.setProvider(new YamlProvider(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml", Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml"));
            return;
        }

        createDatabase();
        Main.getMain().getLogger().info("Connected to MySQL.");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void createDatabase() {
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + databaseName + ";";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.prepareStatement(createDatabase).execute();
            connection.prepareStatement("USE " + databaseName + ";").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS backpacks (id INT, bpType VARCHAR(255), loc BLOB, outputId INT, inputId INT, shownameabove BOOLEAN, bpname VARCHAR(255), owner VARCHAR(255), firstPage BLOB, secondPage BLOB, upgradesIds BLOB);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS upgrades (id INT, upgradeType VARCHAR(255));").execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   @Override
   public void saveBackpacks() {
       try {
           Connection connection = DriverManager.getConnection(url, user, password);
           connection.prepareStatement("USE " + databaseName + ";").execute();
           connection.prepareStatement("DELETE FROM backpacks;").execute();

           if(Main.backPackManager.getBackpacks().isEmpty()){
               connection.close();
               return;
           }
           for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
               PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO backpacks (id, bpType, loc, outputId, inputId, shownameabove, bpname, owner, firstPage, secondPage, upgradesIds) VALUES (?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?);");
               preparedStatement.setInt(1, backPack.getId());
               preparedStatement.setString(2, backPack.getType().name());
               preparedStatement.setBlob(3, SerializationUtils.serializeLocationToStream(backPack.getLocation()));
               preparedStatement.setInt(4, backPack.getOutputUpgrade());
               preparedStatement.setInt(5, backPack.getInputUpgrade());
               preparedStatement.setBoolean(6, backPack.isShowingNameAbove());
               preparedStatement.setString(7, backPack.getName());
               if(backPack.getOwner() == null){
                   preparedStatement.setNull(8, Types.VARCHAR);
               }    else{
                   preparedStatement.setString(8, backPack.getOwner().toString());
               }
               preparedStatement.setBlob(9, SerializationUtils.serializeInventory(backPack.getFirstPage()));
               preparedStatement.setBlob(10, SerializationUtils.serializeInventory(backPack.getSecondPage()));
               preparedStatement.setBlob(11, SerializationUtils.serializeUpgradesIds(backPack));
               preparedStatement.execute();
           }
           connection.close();
       } catch (SQLException | IOException e) {
           e.printStackTrace();
       }
   }

   @Override
   public void saveUpgrades() {
       try {
           Connection connection = DriverManager.getConnection(url, user, password);
           connection.prepareStatement("USE " + databaseName + ";").execute();
           connection.prepareStatement("DELETE FROM upgrades;").execute();

           if(UpgradeManager.getUpgrades().isEmpty()){
               connection.close();
               return;
           }
           for(Upgrade upgrade : UpgradeManager.getUpgrades().values()){
               PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO upgrades (id, upgradeType) VALUES (?, ?);");
               preparedStatement.setInt(1, upgrade.getId());
               preparedStatement.setString(2, upgrade.getType().toString());
               preparedStatement.execute();
               //TODO: save upgrades items, fuel, etc..
           }
           connection.close();
       }    catch (SQLException e) {
           e.printStackTrace();
       }
   }

   @Override
   public void loadUpgrades() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.prepareStatement("USE " + databaseName + ";").execute();

            Statement statement = connection.createStatement();
            ResultSet upgradeSet = statement.executeQuery("SELECT * FROM upgrades;");
            while(upgradeSet.next()){
                switch (upgradeSet.getString("upgradeType")){
                    case "FURNACE" -> {
                        UpgradeManager.getUpgrades().put(upgradeSet.getInt("id"), new FurnaceUpgrade(UpgradeType.FURNACE, upgradeSet.getInt("id")));
                    }
                }
                UpgradeManager.getUpgrades().put(upgradeSet.getInt("id"), new Upgrade(UpgradeType.valueOf(upgradeSet.getString("upgradeType")), upgradeSet.getInt("id")));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

   }

   @Override
   public void loadBackpacks() {
       try{
           Connection connection = DriverManager.getConnection(url, user, password);
           connection.prepareStatement("USE " + databaseName + ";").execute();

           Statement statement = connection.createStatement();
           ResultSet backpackSet = statement.executeQuery("SELECT * FROM backpacks;");

           while(backpackSet.next()){
               BackPack backPack = new BackPack(BackpackType.valueOf(backpackSet.getString("bpType")), backpackSet.getInt("id"));
               if(SerializationUtils.deserializeLocationFromStream(backpackSet.getBlob("loc").getBinaryStream()) == null){
                   backPack.setLocation(null);
               }    else{
                   backPack.setLocation(SerializationUtils.deserializeLocationFromStream(backpackSet.getBlob("loc").getBinaryStream()));
                   Main.backPackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
               }
               backPack.setOutputUpgrade(backpackSet.getInt("outputId"));
               backPack.setInputUpgrade(backpackSet.getInt("inputId"));
               backPack.setShowNameAbove(backpackSet.getBoolean("shownameabove"));
               backPack.setName(backpackSet.getString("bpname"));
               if(backpackSet.getString("owner") != null){
                   backPack.setOwner(UUID.fromString(backpackSet.getString("owner")));
               }
               Bukkit.getConsoleSender().sendMessage("test");
               SerializationUtils.deserializeItemsToInventory(backpackSet.getBlob("firstPage").getBinaryStream(), backPack.getFirstPage());
               SerializationUtils.deserializeItemsToInventory(backpackSet.getBlob("secondPage").getBinaryStream(), backPack.getSecondPage());
               backPack.setUpgradesIds(SerializationUtils.deserializeUpgradesIds(backpackSet.getBlob("upgradesIds").getBinaryStream()));
               Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);
               new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
               new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();
               new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
           }

           statement.close();
           connection.close();
       } catch (SQLException | IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
   }

}
