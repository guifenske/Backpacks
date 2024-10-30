package br.com.backpacks.storage;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class MySQLProvider extends StorageProvider{
    private final String url;
    private final String user;
    private final String password;
    private final String databaseName;

    public MySQLProvider(String url, String user, String password) {
        super(StorageManager.StorageProviderType.MYSQL);
        this.url = url;
        this.user = user;
        this.password = password;
        this.databaseName = "advbackpacks";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.close();
        }   catch (SQLException e) {
            Main.getMain().getLogger().severe("Could not connect to MySQL... Using YAML as default storage provider.");
            return;
        }

        createDatabase();
    }

    public boolean canConnect() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void createDatabase() {
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + databaseName + ";";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.prepareStatement(createDatabase).execute();
            connection.prepareStatement("USE " + databaseName + ";").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS backpacks (id INT, bpType VARCHAR(255), loc BLOB, outputId INT, inputId INT, shownameabove BOOLEAN, bpname VARCHAR(255), owner VARCHAR(255), firstPage BLOB, secondPage BLOB, upgradesIds BLOB);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS upgrades (id INT, upgradeType VARCHAR(255), enabled BOOLEAN, furnace_smelting BLOB, furnace_fuel BLOB, furnace_result BLOB, furnace_operation INT, furnace_maxoperation INT, jukebox_discs BLOB, jukebox_playing VARCHAR(255), collector_mode INT, tank_1 BLOB, tank_2 BLOB, tank_input_1 BLOB, tank_input_2 BLOB);").execute();
            connection.close();
        } catch (SQLException e) {
            Main.getMain().getLogger().severe("Could not connect to MySQL... Using YAML as default storage provider.");
            return;
        }
        Main.getMain().getLogger().info("Connected to MySQL.");
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

               preparedStatement.setBlob(9, SerializationUtils.serializeBackpackInventory(backPack.getFirstPage()));
               preparedStatement.setBlob(10, SerializationUtils.serializeBackpackInventory(backPack.getSecondPage()));
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
               PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO upgrades (id, upgradeType, enabled, furnace_smelting, furnace_fuel, furnace_result, furnace_operation, furnace_maxoperation, jukebox_discs, jukebox_playing, collector_mode, tank_1, tank_2, tank_input_1, tank_input_2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

               preparedStatement.setInt(1, upgrade.getId());
               preparedStatement.setString(2, upgrade.getType().toString());
               preparedStatement.setBoolean(3, true);
               preparedStatement.setBlob(4, SerializationUtils.nullBlob());
               preparedStatement.setBlob(5, SerializationUtils.nullBlob());
               preparedStatement.setBlob(6, SerializationUtils.nullBlob());
               preparedStatement.setInt(7, 0);
               preparedStatement.setInt(8, 0);
               preparedStatement.setBlob(9, SerializationUtils.nullBlob());
               preparedStatement.setString(10, null);
               preparedStatement.setInt(11, 0);
               preparedStatement.setBlob(12, SerializationUtils.nullBlob());
               preparedStatement.setBlob(13, SerializationUtils.nullBlob());
               preparedStatement.setBlob(14, SerializationUtils.nullBlob());
               preparedStatement.setBlob(15, SerializationUtils.nullBlob());

               switch (upgrade.getType()){
                   case FURNACE -> {
                       FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;

                       /*
                       preparedStatement.setBlob(4, SerializationUtils.serializeItem(furnaceUpgrade.getSmelting()));
                       preparedStatement.setBlob(5, SerializationUtils.serializeItem(furnaceUpgrade.getFuel()));
                       preparedStatement.setBlob(6, SerializationUtils.serializeItem(furnaceUpgrade.getResult()));
                       preparedStatement.setInt(7, furnaceUpgrade.getOperation());
                       preparedStatement.setInt(8, furnaceUpgrade.getLastMaxOperation());

                        */
                   }

                   case JUKEBOX -> {
                       JukeboxUpgrade jukeboxUpgrade = (JukeboxUpgrade) upgrade;
                       preparedStatement.setBlob(9, jukeboxUpgrade.serializeDiscs());
                       if(jukeboxUpgrade.getInventory().getItem(13) != null)    preparedStatement.setString(10, jukeboxUpgrade.getInventory().getItem(13).getType().name());
                   }

                   case AUTOFEED -> {
                       AutoFeedUpgrade autoFeedUpgrade = (AutoFeedUpgrade) upgrade;
                       preparedStatement.setBoolean(3, autoFeedUpgrade.isEnabled());
                   }

                   case VILLAGER_BAIT -> {
                       VillagerBaitUpgrade followUpgrade = (VillagerBaitUpgrade) upgrade;
                       preparedStatement.setBoolean(3, followUpgrade.isEnabled());
                   }

                   case COLLECTOR -> {
                       CollectorUpgrade collectorUpgrade = (CollectorUpgrade) upgrade;
                       preparedStatement.setInt(11, collectorUpgrade.getMode());
                       preparedStatement.setBoolean(3, collectorUpgrade.isEnabled());
                   }

                   case LIQUID_TANK -> {
                       TanksUpgrade tanksUpgrade = (TanksUpgrade) upgrade;
                       preparedStatement.setBlob(12, tanksUpgrade.serializeTank(1));
                       preparedStatement.setBlob(13, tanksUpgrade.serializeTank(2));
                       preparedStatement.setBlob(14, SerializationUtils.serializeItem(tanksUpgrade.getInventory().getItem(12)));
                       preparedStatement.setBlob(15, SerializationUtils.serializeItem(tanksUpgrade.getInventory().getItem(14)));
                   }

               }

               Main.debugMessage("Saving " + upgrade.getType().getName() + " upgrade " + upgrade.getId());
               preparedStatement.execute();
           }
           connection.close();
       }    catch (SQLException | IOException e) {
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
                int id = upgradeSet.getInt("id");
                UpgradeType type = UpgradeType.valueOf(upgradeSet.getString("upgradeType"));

                switch (type){
                    case FURNACE -> {
                        FurnaceUpgrade upgrade = new FurnaceUpgrade(id);

                        /*
                        upgrade.setSmelting(SerializationUtils.deserializeItem(upgradeSet.getBlob("furnace_smelting").getBinaryStream()));
                        upgrade.setFuel(SerializationUtils.deserializeItem(upgradeSet.getBlob("furnace_fuel").getBinaryStream()));

                        upgrade.setResult(SerializationUtils.deserializeItem(upgradeSet.getBlob("furnace_result").getBinaryStream()));
                        upgrade.setOperation(upgradeSet.getInt("furnace_operation"));

                        upgrade.setLastMaxOperation(upgradeSet.getInt("furnace_maxoperation"));
                        upgrade.updateInventory();

                         */

                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case JUKEBOX -> {
                        JukeboxUpgrade upgrade = new JukeboxUpgrade(id);

                        upgrade.deserializeDiscs(upgradeSet.getBlob("jukebox_discs").getBinaryStream());
                        if(upgradeSet.getString("jukebox_playing") != null) upgrade.getInventory().setItem(13, new ItemStack(Material.valueOf(upgradeSet.getString("jukebox_playing"))));

                        upgrade.updateInventory();
                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case COLLECTOR -> {
                        CollectorUpgrade upgrade = new CollectorUpgrade(id);

                        upgrade.setMode(upgradeSet.getInt("collector_mode"));
                        upgrade.setEnabled(upgradeSet.getBoolean("enabled"));

                        upgrade.updateInventory();

                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case VILLAGER_BAIT -> {
                        VillagerBaitUpgrade upgrade = new VillagerBaitUpgrade(id);

                        upgrade.setEnabled(upgradeSet.getBoolean("enabled"));
                        upgrade.updateInventory();

                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case AUTOFEED -> {
                        AutoFeedUpgrade upgrade = new AutoFeedUpgrade(id);

                        upgrade.setEnabled(upgradeSet.getBoolean("enabled"));
                        upgrade.updateInventory();

                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case LIQUID_TANK -> {
                        TanksUpgrade upgrade = new TanksUpgrade(id);

                        upgrade.deserializeTank(upgradeSet.getBlob("tank_1").getBinaryStream());
                        upgrade.deserializeTank(upgradeSet.getBlob("tank_2").getBinaryStream());

                        upgrade.getInventory().setItem(12, SerializationUtils.deserializeItem(upgradeSet.getBlob("tank_input_1").getBinaryStream()));
                        upgrade.getInventory().setItem(14, SerializationUtils.deserializeItem(upgradeSet.getBlob("tank_input_2").getBinaryStream()));

                        upgrade.updateInventory();
                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    default -> {
                        Upgrade upgrade = new Upgrade(type, id);
                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }
                }

                if(UpgradeManager.lastUpgradeID == 0) UpgradeManager.lastUpgradeID = id;
                if(UpgradeManager.lastUpgradeID < id){
                    UpgradeManager.lastUpgradeID = id;
                }

                Main.debugMessage("Loading " + type.getName() + " Upgrade: " + id);
            }

            statement.close();
            connection.close();

        } catch (SQLException | IOException | ClassNotFoundException e) {
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
               }

               else{
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

               SerializationUtils.deserializeItemsToInventory(backpackSet.getBlob("firstPage").getBinaryStream(), backPack.getFirstPage());
               SerializationUtils.deserializeItemsToInventory(backpackSet.getBlob("secondPage").getBinaryStream(), backPack.getSecondPage());
               SerializationUtils.deserializeUpgradesIds(backpackSet.getBlob("upgradesIds").getBinaryStream(), backPack);

               Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);

               int id = backPack.getId();
               if(Main.backPackManager.getLastBackpackID() == 0) Main.backPackManager.setLastBackpackID(id);
               if(Main.backPackManager.getLastBackpackID() < id){
                   Main.backPackManager.setLastBackpackID(id);
               }

               Main.debugMessage("loaded backpack " + id);
           }

           statement.close();
           connection.close();
       } catch (SQLException | IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
   }

}
