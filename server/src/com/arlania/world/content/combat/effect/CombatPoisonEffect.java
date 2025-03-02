package com.arlania.world.content.combat.effect;

import com.arlania.engine.task.Task;
import com.arlania.model.CombatIcon;
import com.arlania.model.Hit;
import com.arlania.model.Hitmask;
import com.arlania.model.Item;
import com.arlania.world.entity.impl.Character;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link Task} implementation that handles the poisoning process.
 *
 * @author lare96
 */
public class CombatPoisonEffect extends Task {

    /**
     * The entity being inflicted with poison.
     */
    private final Character entity;

    /**
     * Create a new {@link CombatPoisonEffect}.
     *
     * @param entity the entity being inflicted with poison.
     */
    public CombatPoisonEffect(Character entity) {
        super(33, entity, false);
        this.entity = entity;
    }

    /**
     * Holds all of the different strengths of poisons.
     *
     * @author lare96
     */
    public enum PoisonType {
        MILD(50),
        BLOWPIPE(50),
        BLUDGEON(60),
        EXTRA(70),
        SUPER(120);

        /**
         * The starting damage for this poison type.
         */
        private final int damage;

        /**
         * Create a new {@link PoisonType}.
         *
         * @param damage the starting damage for this poison type.
         */
        PoisonType(int damage) {
            this.damage = damage;
        }

        /**
         * Gets the starting damage for this poison type.
         *
         * @return the starting damage for this poison type.
         */
        public int getDamage() {
            return damage;
        }
    }

    @Override
    public void execute() {

        // Stop the task if the entity is unregistered.
        if (!entity.isRegistered() || !entity.isPoisoned()) {
            this.stop();
            return;
        }

        if(entity.isPlayer() && entity.asPlayer().getPoisonImmunity() > 0) {
            this.stop();
            return;
        }

        // Deal the damage, then try and decrement the damage count.
        entity.dealDamage(new Hit(entity.getAndDecrementPoisonDamage(), Hitmask.DARK_GREEN, CombatIcon.NONE));
       /* if(entity.isPlayer()) {
        	((Player)entity).getPacketSender().sendInterfaceRemoval();
        }*/
    }

    /**
     * The small utility class that manages all of the combat poison data.
     *
     * @author lare96
     * @author Advocatus
     */
    public static final class CombatPoisonData {

        /**
         * The map of all of the different weapons that poison.
         */
        // Increase the capacity of the map as more elements are added.
        private static final Map<Integer, PoisonType> types = new HashMap<>(97);

        /**
         * Load all of the poison data.
         */
        public static void init() {
            types.put(13045, PoisonType.BLUDGEON);
            types.put(12926, PoisonType.BLOWPIPE);
            types.put(12927, PoisonType.BLOWPIPE);
            types.put(817, PoisonType.MILD);
            types.put(816, PoisonType.MILD);
            types.put(818, PoisonType.MILD);
            types.put(831, PoisonType.MILD);
            types.put(812, PoisonType.MILD);
            types.put(813, PoisonType.MILD);
            types.put(814, PoisonType.MILD);
            types.put(815, PoisonType.MILD);
            types.put(883, PoisonType.MILD);
            types.put(885, PoisonType.MILD);
            types.put(887, PoisonType.MILD);
            types.put(889, PoisonType.MILD);
            types.put(891, PoisonType.MILD);
            types.put(893, PoisonType.MILD);
            types.put(870, PoisonType.MILD);
            types.put(871, PoisonType.MILD);
            types.put(872, PoisonType.MILD);
            types.put(873, PoisonType.MILD);
            types.put(874, PoisonType.MILD);
            types.put(875, PoisonType.MILD);
            types.put(876, PoisonType.MILD);
            types.put(834, PoisonType.MILD);
            types.put(835, PoisonType.MILD);
            types.put(832, PoisonType.MILD);
            types.put(833, PoisonType.MILD);
            types.put(836, PoisonType.MILD);
            types.put(1221, PoisonType.MILD);
            types.put(1223, PoisonType.MILD);
            types.put(1219, PoisonType.MILD);
            types.put(1229, PoisonType.MILD);
            types.put(1231, PoisonType.MILD);
            types.put(1225, PoisonType.MILD);
            types.put(1227, PoisonType.MILD);
            types.put(1233, PoisonType.MILD);
            types.put(1253, PoisonType.MILD);
            types.put(1251, PoisonType.MILD);
            types.put(1263, PoisonType.MILD);
            types.put(1261, PoisonType.MILD);
            types.put(1259, PoisonType.MILD);
            types.put(1257, PoisonType.MILD);
            types.put(3094, PoisonType.MILD);

            types.put(5621, PoisonType.EXTRA);
            types.put(5620, PoisonType.EXTRA);
            types.put(5617, PoisonType.EXTRA);
            types.put(5616, PoisonType.EXTRA);
            types.put(5619, PoisonType.EXTRA);
            types.put(5618, PoisonType.EXTRA);
            types.put(5629, PoisonType.EXTRA);
            types.put(5628, PoisonType.EXTRA);
            types.put(5631, PoisonType.EXTRA);
            types.put(5630, PoisonType.EXTRA);
            types.put(5645, PoisonType.EXTRA);
            types.put(5644, PoisonType.EXTRA);
            types.put(5647, PoisonType.EXTRA);
            types.put(5646, PoisonType.EXTRA);
            types.put(5643, PoisonType.EXTRA);
            types.put(5642, PoisonType.EXTRA);
            types.put(5633, PoisonType.EXTRA);
            types.put(5632, PoisonType.EXTRA);
            types.put(5634, PoisonType.EXTRA);
            types.put(5660, PoisonType.EXTRA);
            types.put(5656, PoisonType.EXTRA);
            types.put(5657, PoisonType.EXTRA);
            types.put(5658, PoisonType.EXTRA);
            types.put(5659, PoisonType.EXTRA);
            types.put(5654, PoisonType.EXTRA);
            types.put(5655, PoisonType.EXTRA);
            types.put(5680, PoisonType.EXTRA);

            types.put(5623, PoisonType.SUPER);
            types.put(5622, PoisonType.SUPER);
            types.put(5625, PoisonType.SUPER);
            types.put(5624, PoisonType.SUPER);
            types.put(5627, PoisonType.SUPER);
            types.put(5626, PoisonType.SUPER);
            types.put(5698, PoisonType.SUPER);
            types.put(5730, PoisonType.SUPER);
            types.put(5641, PoisonType.SUPER);
            types.put(5640, PoisonType.SUPER);
            types.put(5637, PoisonType.SUPER);
            types.put(5636, PoisonType.SUPER);
            types.put(5639, PoisonType.SUPER);
            types.put(5638, PoisonType.SUPER);
            types.put(5635, PoisonType.SUPER);
            types.put(5661, PoisonType.SUPER);
            types.put(5662, PoisonType.SUPER);
            types.put(5663, PoisonType.SUPER);
            types.put(5652, PoisonType.SUPER);
            types.put(5653, PoisonType.SUPER);
            types.put(5648, PoisonType.SUPER);
            types.put(5649, PoisonType.SUPER);
            types.put(5650, PoisonType.SUPER);
            types.put(5651, PoisonType.SUPER);
            types.put(5667, PoisonType.SUPER);
            types.put(5666, PoisonType.SUPER);
            types.put(5665, PoisonType.SUPER);
            types.put(5664, PoisonType.SUPER);
        }

        /**
         * Gets the poison type of the specified item.
         *
         * @param item the item to get the poison type of.
         * @return the poison type of the specified item, or <code>null</code>
         * if the item is not able to poison the victim.
         */
        public static Optional<PoisonType> getPoisonType(Item item) {
            if (item == null || item.getId() < 1 || item.getAmount() < 1)
                return Optional.empty();
            return Optional.ofNullable(types.get(item.getId()));
        }

        /**
         * Default private constructor.
         */
        private CombatPoisonData() {
        }
    }
}
