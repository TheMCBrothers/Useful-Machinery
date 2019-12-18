package themcbros.usefulmachinery.energy;

import net.minecraftforge.energy.EnergyStorage;

public class MachineEnergyStorage extends EnergyStorage {

    public MachineEnergyStorage(int capacity) {
        super(capacity);
    }

    public MachineEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void modifyEnergyStored(int energy) {
        this.energy += energy;

        if(this.energy > capacity) this.energy = capacity;
        else if(this.energy < 0) this.energy = 0;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

}
