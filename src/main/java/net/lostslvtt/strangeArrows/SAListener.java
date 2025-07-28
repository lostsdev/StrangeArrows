package net.lostslvtt.strangeArrows;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class SAListener implements Listener {

    @EventHandler
    public void onSAuse(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        ItemStack bow = event.getBow();
        if (bow == null) return;
        ItemStack arrow = event.getConsumable();

        String bowType = bow
            .getItemMeta()
            .getPersistentDataContainer()
            .get(
                NamespacedKey.fromString("strangearrows"),
                PersistentDataType.STRING
            );
        String arrowType = arrow
            .getItemMeta()
            .getPersistentDataContainer()
            .get(
                NamespacedKey.fromString("strangearrows"),
                PersistentDataType.STRING
            );
        if (bowType != null) {
            event
                .getProjectile()
                .getPersistentDataContainer()
                .set(
                    NamespacedKey.fromString("strangearrows"),
                    PersistentDataType.STRING,
                    StrangeArrows.convertBowTypeToArrowType(bowType)
                );
        } else if (arrowType != null) {
            event
                .getProjectile()
                .getPersistentDataContainer()
                .set(
                    NamespacedKey.fromString("strangearrows"),
                    PersistentDataType.STRING,
                    arrowType
                );
        }
    }

    @EventHandler
    public void onSAhitEntity(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        if (event.getHitEntity() == null) return;

        String type = event
            .getEntity()
            .getPersistentDataContainer()
            .get(
                NamespacedKey.fromString("strangearrows"),
                PersistentDataType.STRING
            );
        if (type == null) return;
        handleArrowHit(
            type,
            event.getEntity().getShooter(),
            event.getHitEntity()
        );
        if (!event.getEntity().isDead()) event.getEntity().remove();
    }

    private void handleArrowHit(
        String type,
        ProjectileSource shooter,
        Entity shootedAt
    ) {
        Location toSpawnParticle = shootedAt.getLocation();
        if (type.equalsIgnoreCase("sneezearrow")) {
            toSpawnParticle.setY(toSpawnParticle.getY() + 1);
            shootedAt
                .getWorld()
                .spawnParticle(Particle.SNEEZE, toSpawnParticle, 10);
        } else if (type.equalsIgnoreCase("explosionarrow")) {
            toSpawnParticle.setY(toSpawnParticle.getY() + 1);
            shootedAt.getWorld().createExplosion(toSpawnParticle, 2, false);
        } else if (type.equalsIgnoreCase("fireexplosionarrow")) {
            toSpawnParticle.setY(toSpawnParticle.getY() + 1);
            shootedAt.getWorld().createExplosion(toSpawnParticle, 3, true);
        } else if (type.equalsIgnoreCase("waterarrow")) {
            shootedAt.setFireTicks(0);
            toSpawnParticle.setY(toSpawnParticle.getY() + 1);
            shootedAt
                .getWorld()
                .spawnParticle(Particle.CLOUD, toSpawnParticle, 5);
        } else if (type.equalsIgnoreCase("fatiguearrow")) {
            if (!(shootedAt instanceof Player)) return;
            Player shootedAtPlayer = (Player) shootedAt;
            shootedAtPlayer.addPotionEffect(
                new PotionEffect(PotionEffectType.SLOW_DIGGING, 180, 5)
            );
        } else if (type.equalsIgnoreCase("sculkarrow")) {
            if (!(shootedAt instanceof Player)) return;
            Player shootedAtPlayer = (Player) shootedAt;
            shootedAtPlayer.addPotionEffect(
                PotionEffectType.SLOW.createEffect(100, 4)
            );
            toSpawnParticle.setY(toSpawnParticle.getY() + 1);
            shootedAt
                .getWorld()
                .spawnParticle(Particle.SCULK_SOUL, toSpawnParticle, 10);
        } else if (type.equalsIgnoreCase("knockbackarrow")) {
            shootedAt.setVelocity(shootedAt.getVelocity().multiply(2));
            toSpawnParticle.setY(toSpawnParticle.getY() + 1);
            shootedAt
                .getWorld()
                .spawnParticle(Particle.SCULK_SOUL, toSpawnParticle, 10);
        }
    }
}
