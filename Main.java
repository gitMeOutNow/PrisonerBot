import java.util.*;
import java.io.*;
class Main {
  public static void main(String[] args) {
    String coop = "COOPERATE";
    String def = "DEFECT";
    Bot[] botList = {
      new Cooperator(), 
      new Defector(), 
      new Random(), 
      new TitForTat(), 
      new TitForTatForgives(), 
      new GrimTrigger(), 
      new AverageValueCoop(), 
      new AverageValueDef()
      };
    for(int i = 0; i<botList.length-1; i++)
    {
      for(int j = i+1; j<botList.length; j++)
      {
        //i = bot 1, j = bot 2
        //DD 2.2
        //CD 0.3
        //DC 3.0
        //CC 1.1
        int iPoints = 0;
        int jPoints = 0;
        for(int turns = 0; turns < 1000; turns++)
        {
          String iDecision = botList[i].decide(turns);
          String jDecision = botList[j].decide(turns);
          if(iDecision.equals(def) && jDecision.equals(def))
          {
            iPoints+=2;
            jPoints+=2;
          }
          else if(iDecision.equals(def) && jDecision.equals(coop))
          {
            iPoints+=0;
            jPoints+=3;
          }
          else if(iDecision.equals(coop) && jDecision.equals(def))
          {
            iPoints+=3;
            jPoints+=0;
          }
          else if(iDecision.equals(coop) && jDecision.equals(coop))
          {
            iPoints+=1;
            jPoints+=1;
          }
          else System.out.println("ERROR 45");
          //Updates History
          botList[i].addHistory(iDecision, jDecision);
          botList[j].addHistory(jDecision, iDecision);
        }
        //End Match
        botList[i].addPoints(iPoints);
        botList[j].addPoints(jPoints);
        botList[i].clear();
        botList[j].clear();
      }
    }
    //Print
    for(int printBots = 0; printBots < botList.length; printBots++)
    {
      System.out.println(botList[printBots].toString());
    }
  }
}
class Bot {
  final String coop = "COOPERATE";
  final String def = "DEFECT";
  String name = "Bot";
  int points = 0;
  ArrayList<String> ownHistory = new ArrayList<String>();
  ArrayList<String> otherHistory = new ArrayList<String>();
  public Bot()
  {
  }
  public void clear()
  {
    ownHistory.clear();
    otherHistory.clear();
  }
  public void addPoints(int total)
  {
    points+=total;
  }
  public String decide(int turn)
  {
    return "";
  }
  public void addHistory(String own, String other)
  {
    ownHistory.add(own);
    otherHistory.add(other);
  }
  //TODO: Sorting
  public String toString()
  {
    return name + ": " + points;
  }
  
}
//Always cooperates
class Cooperator extends Bot
{
  public Cooperator()
  {
    super.name = "Cooperator";
  }
  public String decide(int turn)
  {
    return coop;
  }
}
//Always defects
class Defector extends Bot
{
  public Defector()
  {
    super.name = "Defector";
  }
  public String decide(int turn)
  {
    return def;
  }
}
//Random
class Random extends Bot
{
 public Random()
  {
    super.name = "Random";
  }
  public String decide(int turn)
  {
    if(Math.random() < 0.5) return def;
    return coop;
  }
}
//Retaliates, plays nice at first
class TitForTat extends Bot
{
  public TitForTat()
  {
    super.name = "TitForTat";
  }
  public String decide(int turn)
  {
    if(turn == 0) return coop;
    else return otherHistory.get(turn-1);
  }
}
//Retaliates, 10% chance to forgive
class TitForTatForgives extends Bot
{
  public TitForTatForgives()
  {
    super.name = "TitForTatForgives";
  }
  public String decide(int turn)
  {
     if(turn > 0 && otherHistory.get(turn-1).equals(def) && Math.random() > .1 ) return def;
     return coop;
  }
}
//If you defect, I will burn you
class GrimTrigger extends Bot
{
  public GrimTrigger()
  {
    super.name = "GrimTrigger";
  }
  public String decide(int turn)
  {
    if(otherHistory.contains(def)) return def;
    return coop;
  }
}
//If other bot defects more than coops, defect. Cooperate at first and at ties
class AverageValueCoop extends Bot
{
  public AverageValueCoop()
  {
    super.name = "AverageValueCoop";
  }
  public String decide(int turn)
  {
    if(Collections.frequency(otherHistory, def) > Collections.frequency(otherHistory, coop)) return def;
    return coop;
  }
}
//If other bot defects more than coop, defect. Defect at first and at ties
class AverageValueDef extends Bot
{
  public AverageValueDef()
  {
    super.name = "AverageValueDef";
  }
  public String decide(int turn)
  {
    if(Collections.frequency(otherHistory, def) >= Collections.frequency(otherHistory, coop)) return def;
    return coop;
  }
}
