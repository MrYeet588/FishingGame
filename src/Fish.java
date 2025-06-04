public class Fish {
    private Mutation mutation;
    private double weight;
    private String name;
    private int fishSpeciesRNG;
    private int fishMutationRNG;

    public Fish () {
        fishSpeciesRNG = (int) ((Math.random() * 3) - 1);
        if (fishSpeciesRNG == 1) {
            name = "Crestfish";
        } else if (fishSpeciesRNG == 2) {
            name = "Merluccid hake";
        } else if (fishSpeciesRNG == 3) {
            name = "Sergeant major";
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
}
