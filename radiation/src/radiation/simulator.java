package radiation;

import java.util.ArrayList;

import edu.princeton.cs.algs4.ST;

public final class simulator {

  static private double increment = 1; // increment of time in hours
  static private double blockage = 0; // percent of gamma blocked
  static private int totalHours = 366; // total time

  static private double rads = 0; // total rads
  static private double hour = 0; // current hour
  static private double outside = 1000; // outside radiation (rads/hour)
  static private double prevtime = 1; // previous time where outside radiation was decreased

  static private final ST<String, Double> halvingThicknesses = new ST<String, Double>();
 
  /***
   * sets up the symbol table to have halving distance values.
   */
  private static void setup() {
    halvingThicknesses.put("LEAD", 0.4);
    halvingThicknesses.put("STEEL", 0.99);
    halvingThicknesses.put("CONCRETE", 2.4);
    halvingThicknesses.put("SAND", 3.6);
    halvingThicknesses.put("DIRT", 3.6);
    halvingThicknesses.put("WATER", 7.2);
    halvingThicknesses.put("WOOD", 11.0);
    halvingThicknesses.put("BOOKS", 7.0);
    halvingThicknesses.put("MAGAZINES", 7.0);
    halvingThicknesses.put("CINDER BLOCK", 4.0);
    halvingThicknesses.put("RED BRICK", 3.2);
    halvingThicknesses.put("BROKEN COAL", 5.0);
    halvingThicknesses.put("WET PEAT MOSS", 5.0);
    halvingThicknesses.put("SUGAR", 7.0);
    halvingThicknesses.put("NAVY OR SOY BEANS", 7.0);
    halvingThicknesses.put("BUTTER", 7.0);
    halvingThicknesses.put("SHELLED CORN", 7.0);
    halvingThicknesses.put("WHEAT", 7.0);
    halvingThicknesses.put("POTATOES", 7.0);
    halvingThicknesses.put("RICE", 7.0);
    halvingThicknesses.put("COFFEE BEANS", 12.0);
    halvingThicknesses.put("APPLES", 9.0);
  }

  /***
   * reduces outside radiation.
   */
  private static void decay() {
    if (hour >= prevtime * 7) {
      outside /= 10;
      prevtime = hour;
    }
  }

  /***
   * adds time and radiation to totals.
   */
  private static void addTime() {
    hour += increment;
    rads += (increment * outside) * (0.01 * (100 - blockage));
  }

  /***
   * returns if simulation should still be running.
   * 
   * @return if simulation should still be running
   */
  private static boolean isRunning() {
    return (hour <= totalHours);
  }

  /***
   * simulates fallout radiation absorbed and prints result.
   * 
   * @param shielding
   *          percent of gamma blocked
   * @param days
   *          total time
   * @param outsideRadiation
   *          outside radiation (rads/hour)
   */
  public static void simulate(double shielding, double days, double outsideRadiation) {

    blockage = shielding;
    totalHours = (int)(days * 24);
    outside = outsideRadiation;

    while (isRunning()) {
      decay();
      addTime();
    }
    System.out.println("Total radiation in " + totalHours / 24 + " days: " + rads + "rems");
    System.out.println(
        "Ambient radiation after " + totalHours / 24 + " days: " + outside + " rads per hour");
  }

  /***
   * returns percent of radiation blocked
   * @param material material in upper case letters
   * @param thickness thickness of material in inches
   * @return percent of radiation blocked
   */
  public static double shielding(String material, Double thickness) {
    setup();
    if (!halvingThicknesses.contains(material)) {
      throw new IllegalArgumentException();
    }
    double ht = halvingThicknesses.get(material); // halving thickness of input material
    return 100 - (((1 / ((thickness / ht) * 2)) * 100));
  }
  
  public static double shielding(ArrayList<String> material, ArrayList<Double> thickness) {
    setup();
    if (material.size() != thickness.size()) {
      throw new IllegalArgumentException();
    }
    double output = 0;
    for(int i = 0; i < material.size(); i++) {
      double totalRemaining = 100 - output;
      double currentRemaining = 100 - shielding(material.get(i), thickness.get(i));
      output = 100 - ((totalRemaining/100)*(currentRemaining/100)*100);
    }
    return output;
  }

  /***
   * simulates fallout radiation absorbed and prints result.
   * 
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    ArrayList<String> material = new ArrayList<String>();
    ArrayList<Double> thickness = new ArrayList<Double>();
    material.add("WOOD");
    thickness.add(10.0);
    
    material.add("CONCRETE");
    thickness.add(24.0);
    
    material.add("DIRT");
    thickness.add(24.0);
    
    simulator.simulate(shielding(material, thickness), 24, 1000);
  }

}
