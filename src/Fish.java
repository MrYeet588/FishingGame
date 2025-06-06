import javax.swing.*;

public class Fish{
    private Mutation mutation;
    private double weight;
    private String name;
    private int fishSpeciesRNG;
    private int fishMutationRNG;
    private int fishMutationChanceRNG;

    public Fish() {
        fishSpeciesRNG = (int) ((Math.random() * 3) - 1);
        fishMutationChanceRNG = (int) ((Math.random() * 5) - 1);

        if (fishMutationChanceRNG == 1){
            mutation = fishMutationLogic();
        }
        if (fishSpeciesRNG == 1) {
            name = "Tuna";
            weight = ((Math.random() * 681) - 4);
        } else if (fishSpeciesRNG == 2) {
            name = "Merluccid hake";
            weight = ((Math.random() * 16) - 4);
        } else if (fishSpeciesRNG == 3) {
            name = "Sergeant major";
            weight = ((Math.random() * (0.65 - 0.15 + 1)) - 0.15);
        }
    }

    public Mutation getMutation() {
        return mutation;
    }

    public double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public double getSellValue(){
        return weight * mutation.getMultiplier();
    }

    public Mutation fishMutationLogic(){
        fishMutationRNG = (int) ((Math.random() * 2) - 1);
        if (fishMutationRNG == 1) {
            return new Mutation("Rotting");
        } else {
            return new Mutation("Neon");
        }
    }
    public String fishInfo(){
        return mutation +" " + name + " that weighs " + weight + "lbs!";
    }
}
