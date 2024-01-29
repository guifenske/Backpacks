package br.com.backpacks.backupHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void zipAll(Path pathBackpacks, Path pathUpgrades, String path) throws IOException {
        List<String> srcFiles = Arrays.asList(pathBackpacks.toString(), pathUpgrades.toString());
        String fileOutputName = "backup-" + System.currentTimeMillis() + ".zip";

        FileOutputStream fos = new FileOutputStream(Paths.get(path) + "/" + fileOutputName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
            fileToZip.delete();

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
    }
}
