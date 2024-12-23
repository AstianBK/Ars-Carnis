package com.TBK.ars_carnis.common.manager;

import com.TBK.ars_carnis.common.skill.PartAbstract;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Objects;

public class SkillAbstractInstance implements Comparable<SkillAbstractInstance> {
    public static final String SPELL_ID = "id";
    public static final String SPELL_LEVEL = "level";
    public static final String SPELL_LOCKED = "locked";
    public static final SkillAbstractInstance EMPTY = new SkillAbstractInstance(PartAbstract.NONE, 0, false);
    private MutableComponent displayName;
    protected final PartAbstract power;
    protected final int spellLevel;
    protected final boolean locked;

    private SkillAbstractInstance() throws Exception {
        throw new Exception("Cannot create empty power slots.");
    }

    public SkillAbstractInstance(PartAbstract spell, int level, boolean locked) {
        this.power = Objects.requireNonNull(spell);
        this.spellLevel = level;
        this.locked = locked;
    }

    public SkillAbstractInstance(PartAbstract spell, int level) {
        this(spell, level, false);
    }



    public PartAbstract getSkillAbstract() {
        return power == null ? PartAbstract.NONE : power;
    }

    public void write(FriendlyByteBuf buf){
        PartAbstract skill=this.getSkillAbstract();
        buf.writeUtf(skill.name);
        buf.writeInt(skill.cooldown);
        buf.writeInt(skill.castingDuration);
        buf.writeInt(skill.lauchTime);
        buf.writeBoolean(skill.instantUse);
        buf.writeBoolean(skill.isTransform);
        buf.writeBoolean(skill.isCasting);
        buf.writeBoolean(skill.isPassive);
        buf.writeInt(skill.costBloodBase);
        buf.writeInt(skill.duration);
        buf.writeInt(this.getLevel());
    }
    public SkillAbstractInstance read(FriendlyByteBuf buf){
        return new SkillAbstractInstance(new PartAbstract(buf),buf.readInt());
    }

    public int getLevel() {
        return spellLevel;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean canRemove() {
        return !locked;
    }

    public Component getDisplayName() {
        if (displayName == null) {
            displayName = Component.translatable("the_gifted."+ getSkillAbstract().name);
        }
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof SkillAbstractInstance other) {
            return this.power.equals(other.power) && this.spellLevel == other.spellLevel;
        }

        return false;
    }

    public int hashCode() {
        return 31 * this.power.hashCode() + this.spellLevel;
    }
    public int compareTo(SkillAbstractInstance other) {
        int i = this.power.name.compareTo(other.power.name);
        if (i == 0) {
            i = Integer.compare(this.spellLevel, other.spellLevel);
        }
        return i;
    }
}
