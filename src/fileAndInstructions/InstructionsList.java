//Sadece arraylist ve işlemleri yazdır, boyutu al ve referansı al.
package fileAndInstructions;
import java.util.ArrayList;

public class InstructionsList
{
    public static ArrayList<Instructions> list;
    static {
        list = new ArrayList<Instructions>();
    }
    public static void addInstruction(Instructions instruction) {
        list.add(instruction);
    }
    public static void printList() {
        System.out.println("********MIPS assembly program********");
        for(Instructions i: list) {
            System.out.println(i.getInstruction());
        }
        System.out.println("***********************************");
    }
    public static int getInstructionListLength() {
        return list.size();
    }
    public static ArrayList<Instructions> getList() {
    return list;
   }
}
