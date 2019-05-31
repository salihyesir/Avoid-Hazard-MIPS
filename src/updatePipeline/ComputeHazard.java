package updatePipeline;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import fileAndInstructions.Instructions;
import fileAndInstructions.InstructionsList;
import static fileAndInstructions.InstructionsList.list;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

public class ComputeHazard {
    
    private static PrintWriter printWriter;
    private static float cycleNumber = 0.0f;
    private static boolean branchDetect=false; 
    
    public static void compute() throws IOException{
    FileWriter fileWriter = new FileWriter("out.asm");
    printWriter = new PrintWriter(fileWriter);
    update();
    printWriter.close();
   }
    
public static void update() throws IOException {
    File imgFile = new File("graph.png");
    imgFile.createNewFile();
    String temp;
    String temp2;
    Graph<String, DefaultEdge> g = new DirectedPseudograph<>(DefaultEdge.class);
    Instructions[] instructions;
    instructions = new Instructions[InstructionsList.getInstructionListLength()];
    ArrayList<String[]> okunan = new ArrayList<String[]>();
    int nop_detect= 0;
    //computes the hazard graph
    for (int i = 0; i < InstructionsList.getInstructionListLength(); i++) { 
        instructions[i] = InstructionsList.getList().get(i);
        cycleNumber++; 
        okunan.add(instructions[i].getInputReg());
        temp = Integer.toString(i+1);
        g.addVertex(temp);
    }
    cycleNumber=cycleNumber+4;
    for (int i = 0; i < InstructionsList.getInstructionListLength(); i++){ 
        if (i==0){
            printWriter.printf(list.get(i).getInstruction()+"\n");
            System.out.println(list.get(i).getInstruction());
        }
        if( i!=0 && i!=InstructionsList.getInstructionListLength()){
            //1.ile 2. karşılaştır.
            nop_detect = dataHazard(instructions[i-1],instructions[i],12);
            //nop yoksa 2. ile 3. karşılaştır.
            if(i !=InstructionsList.getInstructionListLength()-1 && nop_detect == 0)
                nop_detect = dataHazard(instructions[i],instructions[i+1],23);
            //nop yoksa 1. ile 3. karşılaştır.  ayrıca aradaki branch değilse
           // System.out.println(controlHazard(instructions[i],0));
            if(i !=InstructionsList.getInstructionListLength()-1 && nop_detect == 0 && controlHazard(instructions[i],0) == 0){
                nop_detect=dataHazard(instructions[i-1],instructions[i+1],13);
            }
            //System.out.println(nop_detect);
        }
        if (i!=0) {
            printWriter.printf(list.get(i).getInstruction()+"\n");
            System.out.println(list.get(i).getInstruction());
        }
        // beq bne control hazard detect
        //if en son branch komutu gelirse nop koymaya gerek yok minimum nop ile çözüyoruz.
        if (i != InstructionsList.getInstructionListLength()-1) {
            controlHazard(instructions[i],1);
        }
        else
           controlHazard(instructions[i],0); 
    }

    System.out.println("*********************************************");
    System.out.println("Bonus 1: Hazard graph hesaplanır ve çizilir");
    System.out.println("Graph.png de görsel olarak görünebilir.");    
    boolean say =false;
    for (int i = 0; i < InstructionsList.getInstructionListLength(); i++){
        String savereg = instructions[i].getOutputReg();
        //System.out.println(i+1);
        //System.out.println(savereg);
        temp = Integer.toString(i+1);
        say=false;
        int control = 0;
        for (int j = i; j < InstructionsList.getInstructionListLength(); j++){
            temp2 = Integer.toString(j+1);
            control++;
            if(say==true)
                break;
            for (String reg : okunan.get(j)){
                //System.out.println(reg);
                if(say == true)
                    break;
                //Control hazard
                if(savereg.equals("CH")){
                    say=true;
                    temp2 = Integer.toString(j+2);
                    System.out.println(temp +"----CH"+control+"---->"+ temp2);
                    // Ya en son branch gelirse o zaman 
                    if(okunan.size() != j+1 )
                        g.addEdge( temp,temp2);
                    else{
                        //Gerek yok ileride edge yok
                        //temp2="next_?";
                        //g.addVertex(temp2);
                        //g.addEdge(temp,temp2 );
                    }
                    break;
                }
                //data hazard
                else if (reg.equals(savereg)){
                    //4 ortak stage olduğundan bu şekilde kodlandı.
                    if(control < 4){
                        int c = control-1;
                        if( i==j &&(instructions[i].getArg1().equals(instructions[i].getArg2()) || instructions[i].getArg1().equals(instructions[i].getArg3())))
                        {
                            System.out.println(  temp+"----------->"+temp2);
                            g.addEdge( temp,temp2);
                            break;
                        }
                        else if(i!=j){
                            System.out.println(  temp+"----DH"+c+"---->"+temp2);
                            g.addEdge( temp,temp2);
                        }
                        break;
                    }//Normalde 4 yeterli else if (control == 4) olmalıydı ama hoca bu şekilde çizmemiş graphı
                    //Örneğin 2 den 7 ye gitmiş
                    else if(control >= 4){
                        say = true;
                        int z = j-i;
                        System.out.println(  temp+"-----"+z+"----->"+temp2);
                        g.addEdge( temp,temp2);
                        break;
                    }
                }  
            }
        }
    }
    System.out.println(g.toString());
    System.out.println();
    JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String,DefaultEdge>(g);
    mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
    layout.execute(graphAdapter.getDefaultParent());
    BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
    ImageIO.write(image, "PNG", imgFile);
    System.out.println("*****************************************************");
}
    public static int dataHazard(Instructions iFirst, Instructions iSecond,int control) throws IOException {
      String outputReg = iFirst.getOutputReg();
      String[] inputReg = iSecond.getInputReg();
      for (String reg : inputReg) {
         if (reg.equals(outputReg)){
            //System.out.println(iSecond.getCmd());
            //System.out.println(reg);
             if (control == 12) {
                 printWriter.append("nop\nnop\n");
                System.out.println("nop\nnop");
                cycleNumber= cycleNumber +2;
                return 2;
             }
             else if(control == 23)
                 return 2;
             else if(control == 13)
             {
                printWriter.append("nop\n");
                System.out.println("nop");
                cycleNumber= cycleNumber +1;
                return 1;
             }
            
         }
      }
      return 0;
   }
    public static int controlHazard(Instructions i,int control) throws IOException {
       
        if (i == null || i.getCmd() == null){
            return 0;
        }
    switch (i.getCmd())
    { 
        case "j":
        case "jal":
        case "jalr":
        case "jr":
            branchDetect= true;
            //En son komut değilse 3 nop basar control branchden sonra gelen komut kontrolü için branch arada
            if (control !=0) {
                System.out.println("#Not verilen datapathde jr jal nerede pc ye mudahale ettiği belli değil");
                System.out.println("#Bende branch komutlarına verdiğim aynı nop sayısını verdim.");
                printWriter.append("nop\nnop\nnop\n");
                System.out.println("nop\nnop\nnop");
                cycleNumber= cycleNumber +3;
                
                return 3;
            }
            else 
                return 3;
        case "beq":
        case "bne":
             //En son komut değilse 3 nop basar
            branchDetect= true;
            if (control !=0) {
                printWriter.append("nop\nnop\nnop\n");
                System.out.println("nop\nnop\nnop");
                cycleNumber= cycleNumber +3;
                branchDetect= true;
                return 3;
            }
            else
                return 3;
        case "Salih_YESİR": break;
        default:
            return 0;
    }
      return 0;
   }
    public static void computeExecution(){
        if (!branchDetect) {
            System.out.println("Bonus 2: Exection Time ");
            System.out.println("Cycle Number : "+cycleNumber);
            BigDecimal Ghz = ((new BigDecimal(1).divide(new BigDecimal(4))));
            System.out.println("Ghz = "+Ghz.round(MathContext.DECIMAL32) + " ns");
            BigDecimal executionTime = ((new BigDecimal(cycleNumber).multiply(Ghz)));
            System.out.println("Execution Time = "+executionTime.round(MathContext.DECIMAL32) + " ns");
            System.out.println("***************************************");
        }
        else{
            System.out.println("Bonus 2: Exection Time ");
            System.out.println(" Programınızda branch vardır!");
            System.out.println(" Exection Time hesaplanmadı.");
        }
       
    }
}
