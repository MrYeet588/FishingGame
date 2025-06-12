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
        fishSpeciesRNG = (int) ((Math.random() * 20000) + 1);
        fishMutationChanceRNG = (int) ((Math.random() * 2) + 1);

        if (fishMutationChanceRNG == 1) {
            mutation = fishMutationLogic();
            hasMutation = true;
        } else {
            hasMutation = false;
        }
        if (fishSpeciesRNG == 1) {
            name = "LeFish James";
            weight = ((double) (int) ((((Math.random() * (698596 - 113 + 1)) + 113) * 100) + 0.5) / 100);
        } else if (fishSpeciesRNG <= 5000) {
            name = "Pebblefish";
            weight = ((double) (int) (((Math.random() * 0.3) + 0.05) * 100 + 0.5) / 100); // 0.05 - 0.35
        } else if (fishSpeciesRNG <= 9000) {
            name = "Trash Snapper";
            weight = ((double) (int) (((Math.random() * 4) + 0.5) * 100 + 0.5) / 100); // 0.5 - 4.5
        } else if (fishSpeciesRNG <= 12000) {
            name = "Soggy Boot";
            weight = ((double) (int) (((Math.random() * 10) + 5) * 100 + 0.5) / 100); // 5 - 15
        } else if (fishSpeciesRNG <= 14000) {
            name = "Tuna";
            weight = ((double) (int) ((((Math.random() * 681) + 4) * 100) + 0.5) / 100);
        } else if (fishSpeciesRNG <= 16000) {
            name = "Merluccid hake";
            weight = ((double) (int) ((((Math.random() * 16) + 4) * 100) + 0.5) / 100);
        } else if (fishSpeciesRNG <= 19000) {
            name = "Sergeant major";
            weight = ((double) (int) (((Math.random() * (0.65 - 0.15)) + 0.15) * 100 + 0.5) / 100);
        } else {
            name = "FEESH";
            weight = ((double) (int) (((Math.random() * (1000 - 0.000000001)) + 0.15) * 100 + 0.5) / 100);
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
        fishMutationRNG = Math.random() * 100; // 0 to 100

        if (fishMutationRNG < 0.5) {
            return new Mutation("OBESE");  // 0.5% chance - best mutation, very rare
        } else if (fishMutationRNG < 3) {
            return new Mutation("GOLDEN"); // 2.5%
        } else if (fishMutationRNG < 7) {
            return new Mutation("DOUBLE-HEADED"); // 4%
        } else if (fishMutationRNG < 12) {
            return new Mutation("GHOSTLY"); // 5%
        } else if (fishMutationRNG < 18) {
            return new Mutation("MUTANT"); // 6%
        } else if (fishMutationRNG < 28) {
            return new Mutation("TRANSLUCENT"); // 10%
        } else if (fishMutationRNG < 40) {
            return new Mutation("GLOWING"); // 12%
        } else if (fishMutationRNG < 60) {
            return new Mutation("NEON"); // 20%
        } else if (fishMutationRNG < 85) {
            return new Mutation("ROTTING"); // 25%
        } else {
            // The bad mutations share the remaining 15%
            double badMutationRNG = Math.random();
            if (badMutationRNG < 0.2) {
                return new Mutation("TOXIC");      // 3%
            } else if (badMutationRNG < 0.4) {
                return new Mutation("MELTED");     // 3%
            } else if (badMutationRNG < 0.6) {
                return new Mutation("PARASITIC");  // 3%
            } else if (badMutationRNG < 0.8) {
                return new Mutation("BONELESS");   // 3%
            } else {
                return new Mutation("RABID");      // 3%
            }
        }                     // 0.15%
    }

    public String fishInfo() {
        if (hasMutation) {
            return mutation.getName() + " " + name + " that weighs " + weight + " KGs!";
        } else {
            return name + " that weighs " + weight + " KGs!";
        }
    }
}
