import javax.swing.*;

public class Fish {
    private Mutation mutation;
    private double weight;
    private String name;
    private int fishSpeciesRNG;
    private double fishMutationRNG;
    private int fishMutationChanceRNG;
    private boolean hasMutation;

    public Fish() {
        fishSpeciesRNG = (int) ((Math.random() * 4) + 1);
        fishMutationChanceRNG = (int) ((Math.random() * 2) + 1);

        if (fishMutationChanceRNG == 1) {
            mutation = fishMutationLogic();
            hasMutation = true;
        } else {
            hasMutation = false;
        }
        if (fishSpeciesRNG == 1) {
            name = "Tuna";
            weight = ((double) (int) ((((Math.random() * 681) + 4) * 100) + 0.5) / 100);
        } else if (fishSpeciesRNG == 2) {
            name = "Merluccid hake";
            weight = ((double) (int) ((((Math.random() * 16) + 4) * 100) + 0.5) / 100);
        } else if (fishSpeciesRNG == 3) {
            name = "Sergeant major";
            weight = ((double) (int) (((Math.random() * (0.65 - 0.15 + 1)) + 0.15) * 100 + 0.5) / 100);
        } else if (fishSpeciesRNG == 4) {
            name = "FEESH";
            weight = ((double) (int) (((Math.random() * (1000 - 0.000000001 + 1)) + 0.15) * 100 + 0.5) / 100);
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

    // Existing method, kept for backward compatibility
    public double getSellValue() {
        return weight * (mutation != null ? mutation.getMultiplier() : 1);
    }

    // New method with same logic, explicit naming for selling price
    public double getSellPrice() {
        return getSellValue();
    }

    public Mutation fishMutationLogic() {
        fishMutationRNG = ((Math.random() * 101));
        if (fishMutationRNG >= 75) {
            return new Mutation("ROTTING");
        } else if (fishMutationRNG >= 0.005){
            return new Mutation("NEON");
        } else {
            return new Mutation("OBESE");
        }
    }

    public String fishInfo() {
        if (hasMutation) {
            return mutation.getName() + " " + name + " that weighs " + weight + " KGs!";
        } else {
            return name + " that weighs " + weight + " KGs!";
        }
    }
}
