package com.cabinboy1031.wiki_tree;

import java.io.*;

public class LinkTreeFileWriter {
    BufferedWriter WriterBuffer = null;

    public LinkTreeFileWriter(String OutputPath){
        try {

            File OutputFile = new File(OutputPath);
            if(OutputFile.exists()){
                OutputFile.createNewFile();
            }
            Writer FileWriter = new FileWriter(OutputFile);
            WriterBuffer = new BufferedWriter(FileWriter);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeToFile(String Output){
        try {
            WriterBuffer.write(Output);
            WriterBuffer.newLine();
        }catch(IOException e){ }
    }

    public void close(){
        try{
            if(WriterBuffer != null) WriterBuffer.close();
        } catch(Exception ex){

        }
    }
}
