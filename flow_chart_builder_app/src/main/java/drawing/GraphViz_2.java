/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Наташа
 */
public class GraphViz_2 {

    /**
     * The dir. where temporary files will be created.
     */
    //private static String TEMP_DIR = "/tmp";	// Linux
    private static final String TEMP_DIR = "D:\\tmp\\";	// Windows
    /**
     * Program location.
     */
    //  private static String DOT = "/usr/bin/dot";	// Linux
    private static final String DOT = "C:\\graphviz-2.38\\release\\bin\\dot.exe";	// Windows

    public static File writeDotGraphToFile(String location, String name, String type, String graph) {
        File file = new File(location + "\\" + name + "." + type);

        try {
            if (file.createNewFile()) {
                System.out.println("File is created!");
                FileWriter fout = new FileWriter(file);
                fout.write(graph);
                fout.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println("Error: I/O error while writing the dot source to temp file!");
            e.printStackTrace();
            return null;
        }
        return file;
    }

//    public static void writeGraphImageToFile(String location, String name, String type) {
//        try {
//            File file = new File(location + "\\" + name + "." + type);
//
//            if (file.createNewFile()) {
//                System.out.println("File is created!");
//            } else {
//                System.out.println("File already exists.");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static byte[] getImageStream(File dot, String type) {
        File img;
        byte[] img_stream = null;

        try {
            img = new File("D:\\img" + "\\" + dot.getName().substring(0, dot.getName().length() - 3) + type);
            if (img.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
//            img = File.createTempFile("graph_", "." + type);
            Runtime rt = Runtime.getRuntime();

            String[] args = {DOT, "-T" + type, dot.getAbsolutePath(), "-o", img.getAbsolutePath()};
            Process p = rt.exec(args);

            p.waitFor();

            FileInputStream in = new FileInputStream(img.getAbsolutePath());
            img_stream = new byte[in.available()];

            in.read(img_stream);
            // Close it if we need to
            if (in != null) {
                in.close();
            }

//            if (img.delete() == false) {
//                System.err.println("Warning: " + img.getAbsolutePath() + " could not be deleted!");
//            }
        } catch (java.io.IOException ioe) {
            System.err.println("Error:    in I/O processing of tempfile in dir " + "D:\\tmp" + "\\" + "\n");
            System.err.println("       or in calling external command");
            ioe.printStackTrace();
        } catch (java.lang.InterruptedException ie) {
            System.err.println("Error: the execution of the external program was interrupted");
            ie.printStackTrace();
        }

        return img_stream;
    }

}
