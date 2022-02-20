package me.ford.periodicholographicdisplays.mock;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class MockPlayer extends MockOPCommandSender implements Player {
    private final String name;
    private final UUID id;

    public MockPlayer(String name) {
        super(null);
        this.name = name;
        id = UUID.randomUUID();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PlayerInventory getInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Inventory getEnderChest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MainHand getMainHand() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InventoryView getOpenInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void openInventory(InventoryView inventory) {
        // TODO Auto-generated method stub

    }

    @Override
    public InventoryView openMerchant(Villager trader, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void closeInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public ItemStack getItemInHand() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setItemInHand(ItemStack item) {
        // TODO Auto-generated method stub

    }

    @Override
    public ItemStack getItemOnCursor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasCooldown(Material material) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getCooldown(Material material) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getSleepTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Location getBedSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean sleep(Location location, boolean force) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getBedLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameMode getGameMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGameMode(GameMode mode) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isBlocking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHandRaised() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getExpToLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Entity getShoulderEntityLeft() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setShoulderEntityLeft(Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public Entity getShoulderEntityRight() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setShoulderEntityRight(Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getEyeLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getTargetBlockExact(int maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRemainingAir() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRemainingAir(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getMaximumAir() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaximumAir(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getMaximumNoDamageTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getLastDamage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getNoDamageTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public Player getKiller() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void attack(Entity arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getEyeHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        // TODO Auto-generated method stub

    }

    @Override
    public EntityEquipment getEquipment() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getCanPickupItems() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLeashed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGliding() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setGliding(boolean gliding) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSwimming() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSwimming(boolean swimming) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isRiptiding() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSleeping() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAI(boolean ai) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasAI() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setCollidable(boolean collidable) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCollidable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T memoryValue) {
        // TODO Auto-generated method stub

    }

    @Override
    public void swingMainHand() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swingOffHand() {
        // TODO Auto-generated method stub

    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void damage(double amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public void damage(double amount, Entity source) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setHealth(double health) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getAbsorptionAmount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getMaxHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaxHealth(double health) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetMaxHealth() {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setVelocity(Vector velocity) {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector getVelocity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public BoundingBox getBoundingBox() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isOnGround() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public World getWorld() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean teleport(Location location) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(Entity destination) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(Entity destination, TeleportCause cause) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getEntityId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getFireTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaxFireTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFireTicks(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDead() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPersistent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {
        // TODO Auto-generated method stub

    }

    @Override
    public Entity getPassenger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Entity> getPassengers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean eject() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public float getFallDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFallDistance(float distance) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return id;
    }

    @Override
    public int getTicksLived() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTicksLived(int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playEffect(EntityEffect type) {
        // TODO Auto-generated method stub

    }

    @Override
    public EntityType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isInsideVehicle() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entity getVehicle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCustomNameVisible() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setGlowing(boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isGlowing() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setInvulnerable(boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isInvulnerable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSilent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSilent(boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasGravity() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setGravity(boolean gravity) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getPortalCooldown() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> getScoreboardTags() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockFace getFacing() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Pose getPose() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCustomName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile,
            Vector velocity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isConversing() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void acceptConversationInput(String input) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic arg0, Material arg1, int arg2)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic arg0, EntityType arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public Player getPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getFirstPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLastPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getStatistic(Statistic arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void incrementStatistic(Statistic arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic arg0, Material arg1, int arg2)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic arg0, EntityType arg1, int arg2)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isOnline() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBanned() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStatistic(Statistic arg0, Material arg1, int arg2)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStatistic(Statistic arg0, EntityType arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setWhitelisted(boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDisplayName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPlayerListName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPlayerListName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPlayerListHeader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPlayerListFooter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPlayerListHeader(String header) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPlayerListFooter(String footer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCompassTarget(Location loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getCompassTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InetSocketAddress getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendRawMessage(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void kickPlayer(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void chat(String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean performCommand(String command) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSneaking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSneaking(boolean sneak) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSprinting() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSprinting(boolean sprinting) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSleepingIgnored() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category,
            float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category,
            float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(Sound sound) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(String sound) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(Sound sound, SoundCategory category) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(String sound, SoundCategory category) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlockChange(Location loc, BlockData block) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMap(MapView map) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getPlayerTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getPlayerTimeOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetPlayerTime() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        // TODO Auto-generated method stub

    }

    @Override
    public WeatherType getPlayerWeather() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void resetPlayerWeather() {
        // TODO Auto-generated method stub

    }

    @Override
    public void giveExp(int amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public void giveExpLevels(int amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getExp() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setExp(float exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLevel(int level) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getTotalExperience() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTotalExperience(int exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendExperienceChange(float progress) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendExperienceChange(float progress, int level) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getExhaustion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setExhaustion(float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getSaturation() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setSaturation(float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getFoodLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFoodLevel(int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getAllowFlight() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAllowFlight(boolean flight) {
        // TODO Auto-generated method stub

    }

    @Override
    public void hidePlayer(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPlayer(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canSee(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFlying() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setFlying(boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public float getFlySpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getWalkSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTexturePack(String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        // TODO Auto-generated method stub

    }

    @Override
    public Scoreboard getScoreboard() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isHealthScaled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public double getHealthScale() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Entity getSpectatorTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendTitle(String title, String subtitle) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetTitle() {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count,
            T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX,
            double offsetY, double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX,
            double offsetY, double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX,
            double offsetY, double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX,
            double offsetY, double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getClientViewDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateCommands() {
        // TODO Auto-generated method stub

    }

    @Override
    public void openBook(ItemStack book) {
        // TODO Auto-generated method stub

    }

    @Override
    public org.bukkit.entity.Player.Spigot spigot() {
        // TODO Auto-generated method stub
        return new MockSpigot();
    }

    public class MockSpigot extends Player.Spigot {

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {

        }

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {

        }

        /**
         * Sends the component to the specified screen position of this player
         *
         * @param position  the screen position
         * @param component the components to send
         */
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position,
                net.md_5.bungee.api.chat.BaseComponent component) {

        }

        /**
         * Sends an array of components as a single message to the specified screen
         * position of this player
         *
         * @param position   the screen position
         * @param components the components to send
         */
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position,
                net.md_5.bungee.api.chat.BaseComponent... components) {

        }

    }

    @Override
    public float getAttackCooldown() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ItemStack getItemInUse() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasDiscoveredRecipe(NamespacedKey recipe) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean dropItem(boolean dropAll) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getSaturatedRegenRate() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setSaturatedRegenRate(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getUnsaturatedRegenRate() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setUnsaturatedRegenRate(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getStarvationRate() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setStarvationRate(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getArrowCooldown() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setArrowCooldown(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getArrowsInBody() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setArrowsInBody(int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isClimbing() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<UUID> getCollidableExemptions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityCategory getCategory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setInvisible(boolean invisible) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isInvisible() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isInWater() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setVisualFire(boolean fire) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isVisualFire() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getFreezeTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaxFreezeTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFreezeTicks(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isFrozen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendMessage(UUID sender, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(UUID sender, String... messages) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendRawMessage(UUID sender, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Entity entity, Sound sound, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Entity entity, Sound sound, SoundCategory category, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopAllSounds() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean breakBlock(Block block) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendBlockDamage(Location loc, float progress) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendEquipmentChange(LivingEntity entity, EquipmentSlot slot, ItemStack item) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor, boolean hasGlowingText)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void hideEntity(Plugin plugin, Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showEntity(Plugin plugin, Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canSee(Entity entity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setResourcePack(String url, byte[] hash, String prompt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(String url, byte[] hash, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(String url, byte[] hash, String prompt, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getPing() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void openSign(Sign sign) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showDemoScreen() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isAllowingServerListings() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public GameMode getPreviousGameMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SpawnCategory getSpawnCategory() {
        // TODO Auto-generated method stub
        return null;
    }

}