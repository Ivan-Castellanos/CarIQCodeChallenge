package org.cariq;

import org.cariq.dto.pet.Pet;

public class APIPets extends AbstractAPIPets<APIPets> {
    public APIPets(String directory) {
        super("pet/" + directory);
    }

    public APIPets() {
        super("pet");
    }

    public boolean arePetsSold(Pet[] pets) {
        for (Pet pet :
                pets) {
            if (!"sold".equals(pet.getStatus())) {
                System.out.println(pet.getStatus());
                return false;
            }
        }
        return true;
    }
}
