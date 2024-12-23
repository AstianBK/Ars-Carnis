package com.TBK.ars_carnis.common.skill;

public abstract class ArmsAbstract extends PartAbstract {
    public Arm arm;
    public ArmsAbstract(String name, int duration, int castingDuration, int cooldown, int lauchTime, boolean instantUse, boolean isCasting) {
        super(name, duration, castingDuration, cooldown, lauchTime, instantUse, true, false, isCasting, false, 0);
    }

    public abstract Arm getArm();
    public abstract ArmsSpike setArm(Arm arm);

    public enum Arm{
        LEFT,
        RIGHT;
    }
}
