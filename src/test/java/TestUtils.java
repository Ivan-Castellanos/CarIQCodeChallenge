import org.cariq.dao.Pet;

public class TestUtils {
    protected boolean assertOver(Pet[] pets){
        for (Pet pet:
             pets) {
            if (!"sold".equals(pet.getStatus())){
                System.out.println(pet.getStatus());
                return false;
            }
        }
        return true;
    }
}
