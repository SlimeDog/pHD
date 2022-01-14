package me.ford.periodicholographicdisplays.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameRule;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.StructureType;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class MockWorld implements World {
    private final String name;

    public MockWorld(String name) {
        this.name = name;
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
    public Block getBlockAt(int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getBlockAt(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getHighestBlockYAt(int x, int z, HeightMap heightMap) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHighestBlockYAt(Location location, HeightMap heightMap) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Block getHighestBlockAt(int x, int z, HeightMap heightMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getHighestBlockAt(Location location, HeightMap heightMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk getChunkAt(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk getChunkAt(Block block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Chunk[] getLoadedChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item dropItem(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean generateTree(Location location, TreeType type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Entity> getEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        return new ArrayList<>();
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Player> getPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        return new ArrayList<>();
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z,
            Predicate<Entity> filter) {
        return new ArrayList<>();
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        return new ArrayList<>();
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getFullTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasStorm() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getThunderDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks,
            Entity source) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Environment getEnvironment() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getSeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean getPVP() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ChunkGenerator getGenerator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getAllowAnimals() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getAllowMonsters() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Biome getBiome(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getTemperature(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getHumidity(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaxHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSeaLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Difficulty getDifficulty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canGenerateStructures() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getMonsterSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getGameRules() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getGameRuleValue(String rule) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getTicksPerAmbientSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isAutoSave() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public File getWorldFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WorldType getWorldType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTicksPerWaterSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void loadChunk(Chunk chunk) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadChunk(int x, int z) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location location, Vector direction, float speed, float spread,
            Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTime(long time) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setWeatherDuration(int duration) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setThundering(boolean thundering) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setThunderDuration(int duration) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTicksPerAmbientSpawns(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTicksPerWaterSpawns(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removePluginChunkTickets(Plugin plugin) {
        // TODO Auto-generated method stub

    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance,
            Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize,
            Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance,
            FluidCollisionMode fluidCollisionMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance,
            FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance,
            FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize,
            Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setFullTime(long time) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStorm(boolean hasStorm) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getWeatherDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isThundering() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPVP(boolean pvp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data, int radius) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBiome(int x, int y, int z, Biome bio) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAutoSave(boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isHardcore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setHardcore(boolean hardcore) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
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
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGameRule(String rule) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public WorldBorder getWorldBorder() {
        // TODO Auto-generated method stub
        return null;
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
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, double extra, T data, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra, T data, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius,
            boolean findUnexplored) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getViewDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Spigot spigot() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Raid> getRaids() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Biome getBiome(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBiome(Location location, Biome biome) {
        // TODO Auto-generated method stub

    }

    @Override
    public BlockState getBlockState(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockData getBlockData(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Material getType(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Material getType(int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBlockData(Location location, BlockData blockData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBlockData(int x, int y, int z, BlockData blockData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setType(Location location, Material material) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setType(int x, int y, int z, Material material) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType type, Consumer<BlockState> stateConsumer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType type, Predicate<BlockState> statePredicate) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type, boolean randomizeData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, boolean randomizeData, Consumer<T> function)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMinHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Item dropItem(Location location, ItemStack item, Consumer<Item> function) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item, Consumer<Item> function) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z, float angle) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long getGameTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isClearWeather() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setClearWeatherDuration(int duration) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getClearWeatherDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLogicalHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isNatural() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBedWorks() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasSkyLight() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasCeiling() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPiglinSafe() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasRaids() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUltraWarm() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int ticksPerAmbientSpawns) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setWaterUndergroundCreatureSpawnLimit(int limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setWaterAmbientSpawnLimit(int limit) {
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
    public int getSimulationDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

}