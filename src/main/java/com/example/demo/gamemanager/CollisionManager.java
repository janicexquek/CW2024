package com.example.demo.gamemanager;
import java.util.*;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.LevelParent;
import com.example.demo.plane.UserPlane;
import javafx.geometry.Bounds;
import javafx.scene.Group;

/**
 * Manages all collision-related logic in the game.
 * Handles collisions between various game entities such as planes and projectiles.
 */
public class CollisionManager {

    private final LevelParent levelParent;
    private final UserPlane user;
    private final List<ActiveActorDestructible> friendlyUnits;
    private final List<ActiveActorDestructible> enemyUnits;
    private final List<ActiveActorDestructible> userProjectiles;
    private final List<ActiveActorDestructible> enemyProjectiles;
    private final List<ActiveActorDestructible> allyProjectiles;
    private final double screenWidth;
    private final double screenHeight;
    private final Group root;

    /**
     * Constructs a new CollisionManager instance with the specified parameters.
     *
     * @param levelParent      The LevelParent instance.
     * @param user             The user's plane.
     * @param friendlyUnits    List of friendly units.
     * @param enemyUnits       List of enemy units.
     * @param userProjectiles  List of user projectiles.
     * @param enemyProjectiles List of enemy projectiles.
     * @param allyProjectiles  List of the Ally projectiles.
     * @param screenWidth      The width of the screen.
     * @param screenHeight     The height of the screen.
     * @param root             The root group of the scene.
     */
    public CollisionManager(
            LevelParent levelParent,
            UserPlane user,
            List<ActiveActorDestructible> friendlyUnits,
            List<ActiveActorDestructible> enemyUnits,
            List<ActiveActorDestructible> userProjectiles,
            List<ActiveActorDestructible> enemyProjectiles,
            List<ActiveActorDestructible> allyProjectiles,
            double screenWidth,
            double screenHeight,
            Group root) {
        this.levelParent = levelParent;
        this.user = user;
        this.friendlyUnits = friendlyUnits;
        this.enemyUnits = enemyUnits;
        this.userProjectiles = userProjectiles;
        this.enemyProjectiles = enemyProjectiles;
        this.allyProjectiles = allyProjectiles;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.root = root;
    }

    /**
     * Handles collisions between friendly units and enemy planes.
     * If a collision is detected, applies damage to both friendly and enemy units.
     * Marks the units as destroyed if applicable.
     */
    public void handlePlaneCollisions() {
        if (levelParent.isGameOver()) return;
        // Create defensive copies to prevent ConcurrentModificationException
        List<ActiveActorDestructible> friendlyUnitsCopy = new ArrayList<>(friendlyUnits);
        List<ActiveActorDestructible> enemyUnitsCopy = new ArrayList<>(enemyUnits);

        for (ActiveActorDestructible friendly : friendlyUnitsCopy) {
            for (ActiveActorDestructible enemy : enemyUnitsCopy) {
                if (friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    friendly.takeDamage();
                    enemy.takeDamage();
                    if (friendly.isDestroyed()) {
                        friendly.setDestroyedBy(ActiveActorDestructible.DestroyedBy.COLLISION_WITH_USER);
                    }
                    if (enemy.isDestroyed()) {
                        enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.COLLISION_WITH_USER);
                    }
                }
            }
        }
    }

    /**
     * Handles collisions between projectiles.
     * Iterates through all user and the Ally projectiles and checks for intersections with enemy projectiles.
     * If a collision is detected, applies damage to both projectiles involved.
     */
    public void handleProjectileCollisions() {
        // User projectiles vs enemy projectiles
        Iterator<ActiveActorDestructible> userProjectileIterator = userProjectiles.iterator();
        while (userProjectileIterator.hasNext()) {
            ActiveActorDestructible userProjectile = userProjectileIterator.next();
            Iterator<ActiveActorDestructible> enemyProjectileIterator = enemyProjectiles.iterator();
            while (enemyProjectileIterator.hasNext()) {
                ActiveActorDestructible enemyProjectile = enemyProjectileIterator.next();
                if (userProjectile.getBoundsInParent().intersects(enemyProjectile.getBoundsInParent())) {
                    userProjectile.takeDamage();
                    enemyProjectile.takeDamage();
                    break;
                }
            }
        }
        // Ally projectiles vs enemy projectiles
        Iterator<ActiveActorDestructible> allyProjectileIterator = allyProjectiles.iterator();
        while (allyProjectileIterator.hasNext()) {
            ActiveActorDestructible allyProjectile = allyProjectileIterator.next();
            Iterator<ActiveActorDestructible> enemyProjectileIterator = enemyProjectiles.iterator();
            while (enemyProjectileIterator.hasNext()) {
                ActiveActorDestructible enemyProjectile = enemyProjectileIterator.next();
                if (allyProjectile.getBoundsInParent().intersects(enemyProjectile.getBoundsInParent())) {
                    allyProjectile.takeDamage();
                    enemyProjectile.takeDamage();
                    break;
                }
            }
        }
    }

    /**
     * Handles collisions between user projectiles and enemy units.
     * Iterates through all user projectiles and checks for intersections with enemy units.
     * If a collision is detected, applies damage to the enemy unit and the user projectile.
     * If the enemy is destroyed, increments the user's kill count.
     */
    public void handleUserProjectileCollisions() {
        Iterator<ActiveActorDestructible> projectileIterator = userProjectiles.iterator();
        while (projectileIterator.hasNext()) {
            ActiveActorDestructible projectile = projectileIterator.next();
            Iterator<ActiveActorDestructible> enemyIterator = enemyUnits.iterator();
            while (enemyIterator.hasNext()) {
                ActiveActorDestructible enemy = enemyIterator.next();
                if (projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    enemy.takeDamage();
                    projectile.takeDamage();
                    if (enemy.isDestroyed()) {
                        enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.USER_PROJECTILE);
                        user.incrementKillCount();
                    }
                    break;
                }
            }
        }
    }

    /**
     * Handles collisions between enemy projectiles and the user's plane.
     * Iterates through all enemy projectiles and checks for intersections with the user's plane.
     * If a collision is detected, applies damage to the user's plane and destroys the enemy projectile.
     */
    public void handleEnemyProjectileCollisions() {
        Iterator<ActiveActorDestructible> projectileIterator = enemyProjectiles.iterator();
        while (projectileIterator.hasNext()) {
            ActiveActorDestructible projectile = projectileIterator.next();
            if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
                user.takeDamageFromProjectile();
                projectile.takeDamage();
                projectileIterator.remove();
                root.getChildren().remove(projectile);
            }
        }
    }

    /**
     * Handles enemy planes penetrating the player's defenses.
     * If an enemy penetrates the defenses, the player takes damage and the enemy is destroyed.
     */
    public void handleEnemyPenetration() {
        if (levelParent.isGameOver()) return;
        for (ActiveActorDestructible enemy : enemyUnits) {
            if (enemyHasPenetratedDefenses(enemy)) {
                user.takeDamageFromPenetration();
                enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.PENETRATION);
                enemy.destroy();
            }
        }
    }

    /**
     * Handles collisions between enemy projectiles and friendly units (allies).
     * Iterates through all enemy projectiles and checks for intersections with friendly units.
     * If a collision is detected, applies damage to the friendly unit and destroys the enemy projectile.
     */

    public void handleEnemyProjectileCollisionsWithAlly() {
        Iterator<ActiveActorDestructible> enemyProjectileIterator = enemyProjectiles.iterator();
        while (enemyProjectileIterator.hasNext()) {
            ActiveActorDestructible enemyProjectile = enemyProjectileIterator.next();
            for (ActiveActorDestructible friendly : friendlyUnits) {
                if (friendly.isDestroyed()) continue;
                if (enemyProjectile.getBoundsInParent().intersects(friendly.getBoundsInParent())) {
                    friendly.takeDamage();
                    enemyProjectile.takeDamage();
                    break;
                }
            }
        }
    }

    /**
     * Handles collisions between the Ally projectiles and enemy units.
     * Iterates through all the Ally projectiles and checks for intersections with enemy units.
     * If a collision is detected, applies damage to the enemy unit and the ally projectile.
     * If the enemy is destroyed, increments the user's kill count.
     */
    public void handleAllyProjectileCollisions() {
        Iterator<ActiveActorDestructible> allyProjectileIterator = allyProjectiles.iterator();
        while (allyProjectileIterator.hasNext()) {
            ActiveActorDestructible allyProjectile = allyProjectileIterator.next();
            Iterator<ActiveActorDestructible> enemyIterator = enemyUnits.iterator();
            while (enemyIterator.hasNext()) {
                ActiveActorDestructible enemy = enemyIterator.next();
                if (allyProjectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    enemy.takeDamage();
                    allyProjectile.takeDamage();
                    if (enemy.isDestroyed()) {
                        enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.USER_PROJECTILE);
                        user.incrementKillCount();
                    }
                    break;
                }
            }
        }
    }

    /**
     * Checks if any projectiles in the user, enemy, or ally projectile lists are out of bounds.
     * If a projectile is out of bounds, it is destroyed.
     */
    public void checkProjectilesOutOfBounds() {
        checkProjectilesOutOfBounds(userProjectiles);
        checkProjectilesOutOfBounds(enemyProjectiles);
        checkProjectilesOutOfBounds(allyProjectiles);
    }
    /**
     * Checks if projectiles in the given list are out of bounds.
     * If a projectile is out of bounds, it is destroyed.
     *
     * @param projectiles The list of projectiles to check.
     */
    private void checkProjectilesOutOfBounds(List<ActiveActorDestructible> projectiles) {
        for (ActiveActorDestructible projectile : projectiles) {
            if (isOutOfBounds(projectile)) {
                projectile.destroy();
            }
        }
    }
    /**
     * Determines if an actor is out of bounds.
     *
     * @param actor The actor to check.
     * @return True if the actor is out of bounds, false otherwise.
     */
    private boolean isOutOfBounds(ActiveActorDestructible actor) {
        Bounds bounds = actor.localToScene(actor.getBoundsInLocal());
        double minX = bounds.getMinX();
        double maxX = bounds.getMaxX();
        double minY = bounds.getMinY();
        double maxY = bounds.getMaxY();

        return maxX < 0 || minX > screenWidth || maxY < 0 || minY > screenHeight;
    }

    /**
     * Checks if an enemy has penetrated the player's defenses.
     *
     * @param enemy The enemy to check.
     * @return True if the enemy has penetrated, false otherwise.
     */
    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth;
    }
}
