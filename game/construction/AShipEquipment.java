package game.construction;

/**
 * Created by Kanto on 07.10.2016.
 */
public abstract class AShipEquipment extends CommonConstruction implements IShipEquipment {

    private int maxStrength;
    private int energyCost;
    private int costOfEquipment;
    private int shieldBonus;


    public AShipEquipment(int totalLife, String name, int maxStrength, int energyCost, int costOfEquipment, int shieldBonus) {
        super(totalLife, name);
        setCostOfEquipment(costOfEquipment);
        setMaxStrength(maxStrength);
        setEnergyCost(energyCost);
        setShieldBonus(shieldBonus);
    }

    public void setShieldBonus(int shieldBonus) {
        this.shieldBonus = shieldBonus;
    }

    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }

    @Override
    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }

    public void setCostOfEquipment(int costOfEquipment) {
        this.costOfEquipment = costOfEquipment;
    }

    public int getMaxStrength() {
        return maxStrength;
    }

    @Override
    public int getEnergyCost() {
        return energyCost;
    }

    public int getShieldBonus() {
        return shieldBonus;
    }

    public int getCostOfEquipment() {
        return costOfEquipment;
    }
}
