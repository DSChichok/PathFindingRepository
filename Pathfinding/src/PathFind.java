import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 @author: Christopher Gervacio Chico
 @date:   03/27/2019
 Restrictions:
 -The Map on Map.txt must be a complete rectangle, meaning
  no two widths nor two heights at different points can be 
  different length.
 -Only one Start and one Finish may be used.  Having none
  or multiples can have unpredictable effects.
  
  This actually outputs multiple paths to the finish if any
  exist beyond the first one found.
  
  Edit Map.txt to your hearts content as long as it follows
  the restrictions.
  
  Map.txt key
  0 = Open path to go
  1 = Closed path to go
  2 = Start
  3 = Finish
 */

public class PathFind 
{
    private int     PathWidth;
    private int     PathHeight;
    private int[][] Map;
    private int     StartW;
    private int     StartH;
    private boolean PathFound;
    
    public PathFind() throws Exception
    {
        PathFindCommons();
        FindStartPoint();
    }
    
    //This constructor forces a Start Point instead of
    //the map deciding one for you
    public PathFind(int Starth, int Startw) throws Exception
    {   
        PathFindCommons();
        StartH = Starth;
        StartW = Startw;
    }
    
    private void PathFindCommons() throws Exception
    {
        //initialization
        PathFound  = false;
        PathWidth  = 0;
        PathHeight = 0;
        
        //Find out how big our map is
        File    file = new File("Map.txt");
        Scanner scan = new Scanner(file);
        String  Temp = "";
        while( scan.hasNext() )
        {
            Temp = scan.nextLine();
            PathHeight++;
        }
        PathWidth = Temp.length();
        scan.close();
        
        //Build our map
        Map = new int[PathHeight][PathWidth];
        
        File    file2 = new File("Map.txt");
        Scanner scan2 = new Scanner(file2);
        for( int i = 0; i < PathHeight; i++ )
        {
            Temp = scan2.nextLine();
            for( int j = 0; j < PathWidth; j++ )
            {
                Map[i][j] = Character.getNumericValue(Temp.charAt(j));
            }
        }
        scan2.close();
        //Map built
    }
    
    private void FindStartPoint()
    {
        boolean BreakI = false;
        for( int i = 0; i < PathHeight; i++ )
        {
            for( int j = 0; j < PathWidth; j++ )
            {
                if( Map[i][j] == 2 )
                {
                    StartH = i;
                    StartW = j;
                    BreakI = true;
                    break;
                }
            }
            if(BreakI)
            {
                break;
            }
        }
    }
    
    public void FindFinish()
    {
        List<Integer> DaPath = new ArrayList<Integer>(); 
        FinishFinder(StartH, StartW, Map, DaPath);
        
        if(!PathFound)
        {
            System.out.println( "No path to the finish found..." );
        }
    }
    
    public void FinishFinder(int i, int j, int[][] CurrentMap, List<Integer> CurrentPath)
    {
        //Is current state the finish?
        CurrentPath.add(j);
        CurrentPath.add(i);

        if( CurrentMap[i][j] == 3 )
        {
            //Finish Found, print out the results
            System.out.println("Finish Found!");
            DisplayFoundPath(CurrentPath);
            PathFound = true;
        }
        else
        {
            //Finish not found, keep going
            CurrentMap[i][j] = 1;

            //North
            if( i - 1 > -1 && CurrentMap[i - 1][j] != 1 )
            {
                int[][] CurrentMapN = CurrentMap;
                List<Integer> CurrentPathN = CurrentPath.stream().collect(Collectors.toList());
                FinishFinder( i - 1, j, CurrentMapN, CurrentPathN);
            }
            
            //South
            if( i + 1 < PathHeight && CurrentMap[i + 1][j] != 1 )
            {
                int[][] CurrentMapS = CurrentMap;
                List<Integer> CurrentPathS = CurrentPath.stream().collect(Collectors.toList());
                FinishFinder( i + 1, j, CurrentMapS, CurrentPathS);
            }
            
            //West
            if( j - 1 > -1 && CurrentMap[i][j - 1] != 1 )
            {
                int[][] CurrentMapW = CurrentMap;
                List<Integer> CurrentPathW = CurrentPath.stream().collect(Collectors.toList());
                FinishFinder( i, j - 1, CurrentMapW, CurrentPathW);
            }
            
            //East
            if( j + 1 < PathWidth && CurrentMap[i][j + 1] != 1 )
            {
                int[][] CurrentMapE = CurrentMap;
                List<Integer> CurrentPathE = CurrentPath.stream().collect(Collectors.toList());
                FinishFinder( i, j + 1, CurrentMapE, CurrentPathE);
               
            }
        }
    }
    
    public void DisplayFoundPath(List<Integer> ThePath)
    {
        boolean Doubling = false;
        for( int i = 0; i < ThePath.size(); i++ )
        {
            System.out.print( ThePath.get(i) );
            
            if(Doubling)
            {
                if(i < ThePath.size() - 1)
                {
                    System.out.print( " -> " );
                }
            }
            else
            {
                System.out.print( ", " );
            }
            Doubling = !Doubling;
        }
        System.out.println();
        System.out.println("Path Complete");
    }

    //This is meant to be a debugging type of function only
    //to display the condition of the map at a desired state
    //of peeking and execution
    public void ShowCurrentMap()
    {
        System.out.println("The Current Map State:");
        for( int i = 0; i < PathHeight; i++ )
        {
            for( int j = 0; j < PathWidth; j++ )
            {
                System.out.print(Map[i][j]);
            }
            System.out.println();
        }
        System.out.println("Map printout complete...");
    }
}