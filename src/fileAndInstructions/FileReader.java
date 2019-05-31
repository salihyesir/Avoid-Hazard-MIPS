//takes a MIPS assembly program as input
//Dosyadan okuma için regex buradadır.
package fileAndInstructions;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReader
{
   
   private static File asmFile;
   
   private static Path asmFilePath;

   public static void openFile() {
      asmFile = new File("mipsInput.asm");
      asmFilePath = Paths.get(asmFile.getPath());
      read();
   }
   private static void read() {
      Path file = asmFilePath;
      try (InputStream in = Files.newInputStream(file);
         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
         String line = new String();                                                                                                 //group5                    //group6
         String pattern = "(\\.\\w+)?(?:\\s*([\\w.]+)\\s*:)?[\\s,]*([\\w.]+)?[\\s,]*(\\$[\\w]+)?[\\s,]*([\\w\\s+\\-(]*[$]?[\\w)]+)?[\\s,]*(\\$*[\\w(-?[0-9]+)]+)?[\\s,]*(?:#(.*))?";
         //githubdan bu regex ifade alternatifleri de vardır.
         Pattern checkRegex = Pattern.compile(pattern);
         //.data yazısına kadar oku
         while ((line = reader.readLine()) != null && !line.equals(".data")) {   
            if(!line.trim().isEmpty()){
               Matcher match = checkRegex.matcher(line);
               if (match.find()) {
                  String label = match.group(2);
                  String cmd = match.group(3);
                  String arg1 = match.group(4);
                  String arg2 = match.group(5);
                  String arg3 = match.group(6);
                  String comment = match.group(7) != null ? match.group(7).trim() : null;
                  // Instruction eklenir. Liste Compute edilirken aktarılcak
                  addInstruction(label, cmd, arg1, arg2, arg3, comment);
               }
            }
         }
         InstructionsList.printList();

      } catch (IOException e) {
         System.err.println(e);
      }
   }
   private static void addInstruction(String label, String cmd,String arg1, String arg2, String arg3, String comment) {
      InstructionsList.addInstruction(new Instructions(label, cmd, arg1, arg2, arg3, comment));
   }
}
