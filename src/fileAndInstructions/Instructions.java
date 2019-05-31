//Instruction 
package fileAndInstructions;
public class Instructions
{
//arg1 ilk arg2 ikinci arg3 içünçü  
    private String label, cmd, arg1, arg2, arg3, comment;

    public Instructions() {
      this.label = "";
      this.cmd = "";
      this.arg1 = "";
      this.arg2 = "";
      this.arg3 = "";
      this.comment = "";
    }
    public Instructions(String label, String cmd, String arg1, String arg2, String arg3, 
        String comment) {
        this.label = label;
        this.cmd = cmd;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.comment = comment;
    }
    public String getArg1() {
        return arg1;
    }
    public String getArg2() {
        return arg2;
    }
    public String getArg3() {
        return arg3;
    }
    public String getCmd() {
        return cmd;
    }
    public String getLabel() {
      return label;
    }
    public String getComment() {
        return comment;
    }
   
    
   public String getInstruction() {
      return (label != null ? label + ": ": "")
            + (cmd != null ? cmd + " " : "")
            + (arg1 != null ? arg1 + " " : "")
            + (arg2 != null ? arg2 + " " : "")
            + (arg3 != null ? arg3 + " " : "")
            + (comment != null ? "#" + comment : "");
   }
    public String[] getInputReg(){
        if (cmd == null)
            return new String[0];
        String[] input;
        switch (cmd) {
        case "j":
        case "jal":
        case "jalr":
        case "jr":
        case "syscall":
        input = new String[0];
        break;
        case "beq":
        case "bne":
        case "la":
            input = new String[2];
          //System.out.println(arg1);
            input[0] = arg1;
            input[1] = arg2;
            break;
        case "lw":
            input = new String[1];
            String tmp_lw;
            input[0]= arg1;
            break;
        case "sw"://2 tane input register var
            input = new String[2];
            input[0] = arg1;
            String tmp;
            tmp= arg2.substring(2, 5);
            input[1] = tmp;
            break;
        case "li":
            input = new String[1];
            input[0] = arg1;
            break;
        default://2 tane input register var
            input = new String[2];
            input[0] = arg2;
            input[1] = arg3;
        }
      return input;
    }

   
    public String getOutputReg() {
        if (cmd == null)
            return "";
        String output;
        switch (cmd) {
        case "j":
        case "jal":
        case "jalr":
        case "jr":
        case "sw":
        case "syscall":
            output = "";
            break;
        case "beq":
        case "bne":
            output= "CH";
            break;
        default:
            output = arg1;
        }
        return output;
    }
}
