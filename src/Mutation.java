public class Mutation {
    private double multiplier;
    private String name;
    public Mutation(String name){
        this.name = name;
        if (name.equals("Rotting")) {
            multiplier = 0.30;
        } else if (name.equals("Neon")) {
            multiplier = 100;
        }
    }


    public double getMultiplier() {
        return multiplier;
    }
}
