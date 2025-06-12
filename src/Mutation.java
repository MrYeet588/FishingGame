public class Mutation {
    private double multiplier;
    private String name;
    public Mutation(String name){
        this.name = name;
        if (name.equals("ROTTING")) {
            multiplier = 0.30;
        } else if (name.equals("NEON")) {
            multiplier = 100;
        } else if (name.equals("OBESE")) {
            multiplier = 300;
        }
    }


    public double getMultiplier() {
        return multiplier;
    }

    public String getName() {
        return name;
    }

}
