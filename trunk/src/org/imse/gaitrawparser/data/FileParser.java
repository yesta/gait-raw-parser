package org.imse.gaitrawparser.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.imse.gaitrawparser.data.PressurePoint.Foot;

public class FileParser
{
    public static List<PressurePoint> parseFile(String filename){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        ArrayList<PressurePoint> points = new ArrayList<PressurePoint>();
       
        String input = "";
        String[] temp = new String[7];
        
        try
        {
            while((input = reader.readLine()) != null){
                temp = input.split(",");
                PressurePoint p = new PressurePoint();
                p.setTime(Double.parseDouble(temp[0]));
                p.setX(Integer.parseInt(temp[1]));
                p.setY(Integer.parseInt(temp[2]));
                p.setPressure(Integer.parseInt(temp[3]));
                p.setObjectNumber(Integer.parseInt(temp[4]));
                if(Integer.parseInt(temp[5]) == 0){
                    p.setFoot(Foot.Left);
                }
                else{
                    p.setFoot(Foot.Right);
                }
                if(Integer.parseInt(temp[6]) == 0){
                    p.setPartOfFoot(true);
                }
                else{
                    p.setPartOfFoot(false);
                }
                points.add(p);
                
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        
        return points;
    }
    
    public static void main(String[] args){
        FileParser.parseFile("C:/GAITRITE39/Raw434.txt");
    }
}
