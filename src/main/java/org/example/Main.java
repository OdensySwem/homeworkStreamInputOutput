package org.example;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Создание файлов;
        File gamesDir = new File("/Users/nikita-butorin/Desktop/Games");

        File src = new File(gamesDir, "src");
        File res = new File(gamesDir, "res");
        File saveGames = new File(gamesDir, "savegames");
        File temp = new File(gamesDir, "temp");

        File main = new File(src, "main");
        File test = new File(src, "test");

        File mainFile = new File(main, "Main.java");
        File utilsFile = new File(main, "Utils.java");

        File drawables = new File(res, "drawables");
        File vectors = new File(res, "vectors");
        File icons = new File(res, "icons");

        File tempFile = new File(temp, "Temp.txt");

        File saveFile = new File(saveGames, "saveFile.txt");

        //Создание game progress;
        GameProgress gm1 = new GameProgress(200, 2, 1, 0.5);
        GameProgress gm2 = new GameProgress(400, 3, 2, 1);
        GameProgress gm3 = new GameProgress(600, 4, 3, 1.5);
        GameProgress gm4 = new GameProgress(800, 5, 4, 3.5);
        GameProgress gm5 = new GameProgress(833, 5, 4, 3.5);

        ArrayList<String> gmList = new ArrayList<>();
        gmList.add("/Users/nikita-butorin/Desktop/Games/savegames/save1.dat");
        gmList.add("/Users/nikita-butorin/Desktop/Games/savegames/save2.dat");
        gmList.add("/Users/nikita-butorin/Desktop/Games/savegames/save3.dat");
        gmList.add("/Users/nikita-butorin/Desktop/Games/savegames/save4.dat");
        gmList.add("/Users/nikita-butorin/Desktop/Games/savegames/save5.dat");

        //Добавление объектов в savegames;
        saveGame("/Users/nikita-butorin/Desktop/Games/savegames/save1.dat", gm1);
        saveGame("/Users/nikita-butorin/Desktop/Games/savegames/save2.dat", gm2);
        saveGame("/Users/nikita-butorin/Desktop/Games/savegames/save3.dat", gm3);
        saveGame("/Users/nikita-butorin/Desktop/Games/savegames/save4.dat", gm4);
        saveGame("/Users/nikita-butorin/Desktop/Games/savegames/save5.dat", gm5);

        zipFiles("/Users/nikita-butorin/Desktop/Games/savegames/files.zip", gmList);

        for (File file: Objects.requireNonNull(saveGames.listFiles())){
            String fString = file.toString();
            int index = fString.lastIndexOf(".");
            if (index > 0){
                String extension = fString.substring(index + 1);
                if (!extension.equals("zip")){
                    file.delete();
                }
            }
        }

        openZip("/Users/nikita-butorin/Desktop/Games/savegames/files.zip", "/Users/nikita-butorin/Desktop/Games/savegames");
        System.out.println(openProgress("/Users/nikita-butorin/Desktop/Games/savegames/save1.dat"));
        System.out.println(openProgress("/Users/nikita-butorin/Desktop/Games/savegames/save2.dat"));
    }

    public static void zipFiles(String zipPath, ArrayList<String> files) throws FileNotFoundException {
        try (ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String file : files) {
                try (FileInputStream fileInput = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file);
                    zipOutput.putNextEntry(entry);
                    byte[] buffer = new byte[fileInput.available()];
                    fileInput.read(buffer);
                    zipOutput.write(buffer);
                    zipOutput.closeEntry();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(String path, GameProgress gameProgress) throws IOException {
        try (FileOutputStream fileOutput = new FileOutputStream(path);
             ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput)) {
            objectOutput.writeObject(gameProgress);
            objectOutput.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void existOrAddDir(File dir) {
        if (dir.exists()) {
            System.out.println("Директория существует");
        }
        dir.mkdir();
        System.out.println("Директория создалась");
    }

    public static void existOrAddFile(File file) throws IOException {
        if (file.exists()) {
            System.out.println("Файл существует");
        }
        file.createNewFile();
        System.out.println("Файл создался");
    }

    public static void openZip(String zipFile, String destination){
        try (ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFile))){
            ZipEntry entry;
            String name;
            while ((entry = zipInput.getNextEntry()) != null){
                name = entry.getName();
                FileOutputStream fileOutput = new FileOutputStream(name);
                for (int c = zipInput.read(); c != -1; c = zipInput.read()){
                    fileOutput.write(c);
                }
                fileOutput.flush();
                zipInput.closeEntry();
                fileOutput.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String pathToSaveFile){
        GameProgress gameProgress = null;
        try (ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(pathToSaveFile))){
            return gameProgress = (GameProgress) objInput.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }
}