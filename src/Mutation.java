public class Mutation {
    private double multiplier;
    private String name;
    public Mutation(String name){
        this.name = name;
        if (name.equals("OBESE")) {
            multiplier = 300;  // Best mutation
        } else if (name.equals("GOLDEN")) {
            multiplier = 150;
        } else if (name.equals("NEON")) {
            multiplier = 100;
        } else if (name.equals("GHOSTLY")) {
            multiplier = 50;
        } else if (name.equals("DOUBLE-HEADED")) {
            multiplier = 40;
        } else if (name.equals("TRANSLUCENT")) {
            multiplier = 30;
        } else if (name.equals("MUTANT")) {
            multiplier = 25;
        } else if (name.equals("GLOWING")) {
            multiplier = 15;
        } else if (name.equals("ROTTING")) {
            multiplier = 0.3;
        } else if (name.equals("TOXIC")) {
            multiplier = 0.5;
        } else if (name.equals("MELTED")) {
            multiplier = 0.2;
        } else if (name.equals("PARASITIC")) {
            multiplier = 0.1;
        } else if (name.equals("BONELESS")) {
            multiplier = 0.25;
        } else if (name.equals("RABID")) {
            multiplier = 0.1;
        } else if (name.equals("BLIND")) {
            multiplier = 0.4;
        } else if (name.equals("FUNGAL")) {
            multiplier = 0.15;
        }
    }


    public double getMultiplier() {
        return multiplier;
    }

    public String getName() {
        return name;
    }

}
