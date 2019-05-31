package hw3Test;

import fileAndInstructions.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import org.jgrapht.io.ExportException;
import updatePipeline.ComputeHazard;

/**
 *
 * @author Salih YESİR
 * 1850144004
 */

public class HW3 {

    public static void main(String[] args) throws IOException, URISyntaxException, ExportException {
        //Dosyayı açar okur yazar
        FileReader.openFile();
        //Hazard grap hesaplar çizer nop koyar.
        ComputeHazard.compute();
        //Executionı hesaplar
        ComputeHazard.computeExecution();
        
    }
    
}
