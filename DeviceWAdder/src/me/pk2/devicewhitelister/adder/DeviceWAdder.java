package me.pk2.devicewhitelister.adder;

import com.alexkasko.krakatau.KrakatauLibrary;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DeviceWAdder {
    private static String READ_ERROR = "UNABLE TO OBTAIN LINE";
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String read() {
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException exception) {
            exception.printStackTrace();
            return READ_ERROR;
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to DeviceWhitelister uuid adder! By: PK2_Stimpy#7089");

        System.out.print("File: ");
        String filename = read();
        if(filename.contentEquals(READ_ERROR))
            return;

        System.out.print("New UUIDs(Split by ','): ");
        String UUIDS = read();
        if(filename.contentEquals(READ_ERROR))
            return;

        System.out.println("\n");
        System.out.print("Opening file... ");

        JarFile jarFile;
        try {
            jarFile = new JarFile(filename);
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }

        System.out.println("OK!");
        System.out.print("Finding file... ");

        Enumeration<JarEntry> enumeration = jarFile.entries();
        while(enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();
            if(entry.isDirectory() || !entry.getName().endsWith("DeviceWhitelister.class"))
                continue;

            InputStream input;
            try {
                input = jarFile.getInputStream(entry);
            } catch (IOException exception) {
                exception.printStackTrace();
                break;
            }

            FileOutputStream out;
            try {
                out = new FileOutputStream(new File("./DeviceWhitelister.class"));
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                return;
            }

            try {
                while (input.available() > 0)
                    out.write(input.read());
                out.close();
                input.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }

            System.out.println("OK!");
            System.out.println("Disassembling... ");

            Collection<File> inFile = new HashSet<>();
            inFile.add(new File("./DeviceWhitelister.class"));

            KrakatauLibrary lib = new KrakatauLibrary();
            lib.disassemble(inFile, new File("./"));

            System.out.println("OK!");

            System.out.print("Modifying... ");

            StringBuilder disassembledContentB = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader("./me/pk2/devicewhitelister/DeviceWhitelister.j"))){
                String line;
                while((line = br.readLine()) != null)
                    disassembledContentB.append(line).append("\n");
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }

            String[] content = disassembledContentB.toString().split("\n");
            for(int i = 0; i < content.length; i++) {
                if(!content[i].endsWith("putstatic me/pk2/devicewhitelister/DeviceWhitelister UUIDS Ljava/lang/String;"))
                    continue;
                content[i-1] = "\tldc '" + UUIDS + "'";
            }

            PrintWriter writer;
            try {
                writer = new PrintWriter("./me/pk2/devicewhitelister/DeviceWhitelister.j");
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                return;
            }

            writer.println(String.join("\n", content));
            writer.close();

            System.out.println("OK!");
            System.out.println("Recompiling into a class... ");

            Collection<File> outFile = new HashSet<>();
            outFile.add(new File("./me/pk2/devicewhitelister/DeviceWhitelister.j"));

            lib.assemble(outFile, new File("./"));

            File outFileClass = new File("./me/pk2/devicewhitelister/DeviceWhitelister.class");

            System.out.println("OK!");
            System.out.println("Modifying jar... ");

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("jar", "-uvf0", filename, "me/pk2/devicewhitelister/DeviceWhitelister.class");

            try {
                jarFile.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }

            try {
                Process process = processBuilder.start();
                StringBuilder output = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while((line = reader.readLine()) != null)
                    output.append(line).append("\n");
                if(process.waitFor() == 0)
                    System.out.println("OK!\n\n" + output.toString());
                else System.out.println("FAIL?\n\n" + output.toString());
            } catch (IOException | InterruptedException exception) {
                exception.printStackTrace();
                return;
            }

            /*
            File devicew = new File(filename);
            File classesFile = new File("./classes/DeviceWhitelister.jar");
            classesFile.getParentFile().mkdirs();
            try {
                Files.copy(devicew.toPath(), classesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }

            runCommand("jar -xvf DeviceWhitelister.jar");

            File oldClass = new File("./classes/me/pk2/devicewhitelister/DeviceWhitelister.class");
            try {
                Files.copy(outFileClass.toPath(), oldClass.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }

            runCommand("jar -cvf DeviceWhitelisterN.jar classes/");*/

            break;
        }

        System.out.print("Cleaning up... ");

        File[] tempFiles = {
                new File("lextab.py"),
                new File("parsetab.py"),
                new File("DeviceWhitelister.class"),
                new File("me/")
        };

        for(File file : tempFiles) {
            if(!file.exists())
                continue;

            try {
                if (file.isFile())
                    file.delete();
                else FileUtils.deleteDirectory(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        System.out.println("OK!");
    }
}
