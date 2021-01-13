package edu.arizona.cs;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class QueryEngineQ5 {
    boolean indexExists=false;
    String inputFilePath ="";

    public QueryEngineQ5(){
        /*add your code*/
        buildModel();
    }

    private void buildModel() {
        /*add your code*/
    }

    public static void main(String[] args ) {
        try {
            /*add your code*/
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    /*
    * "This method should return the F1 score of the classifier when run over test partition".
    * */
    public double runQ5_2_f1score()  {
        double f1 = 0.9;
        return f1;
    }
}
