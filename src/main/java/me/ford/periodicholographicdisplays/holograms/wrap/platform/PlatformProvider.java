package me.ford.periodicholographicdisplays.holograms.wrap.platform;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.plugin.DecentHologramsPlugin;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;
import me.ford.periodicholographicdisplays.holograms.Zombificator;
import me.ford.periodicholographicdisplays.holograms.wrap.command.CommandWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.command.ExecutorWrapper;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.DecentHologramsProvider;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.FancyHologramProvider;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HolographicDisplaysHologramProvider;

public class PlatformProvider {
    private static final List<String> SUPPORTED_PLATFORMS = Collections
            .unmodifiableList(Arrays.asList(HDPlatform.NAME, DHPlatform.NAME, FHPlatform.NAME));
    private final HologramPlatform platform;

    public PlatformProvider(JavaPlugin plugin) {
        platform = findPlatform(plugin);
        if (platform == null) {
            throw new NoPlatformException("Did not find a platform that provides holograms");
        }
    }

    public HologramPlatform getHologramProvider() {
        return platform;
    }

    public static List<String> getSupportedPlatformNames() {
        return SUPPORTED_PLATFORMS;
    }

    private static HologramPlatform findPlatform(JavaPlugin plugin) {
        try {
            Class.forName("me.filoghost.holographicdisplays.plugin.HolographicDisplays");
            if (!plugin.getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
                return null;
            }
            return HDPlatform.getHologramPlatform(plugin);
        } catch (ClassNotFoundException e) {
            // try DecentHolograms
        }
        try {
            Class.forName("eu.decentsoftware.holograms.plugin.DecentHologramsPlugin");
            return DHPlatform.getHologramPlatform(plugin);
        } catch (ClassNotFoundException e) {
            // try fancy holograms
        }
        try {
            Class.forName("de.oliver.fancyholograms.FancyHolograms");
            return FHPlatform.getHologramPlatform(plugin);
        } catch (ClassNotFoundException e) {
            // try something else in the future?
        }
        return null;
    }

    private static class HDPlatform extends AbstractHologramPlatform {
        private static final String NAME = "HolographicDisplays";
        private final HolographicDisplays plugin;
        private final HolographicDisplaysHologramProvider provider;

        private HDPlatform(HolographicDisplaysHologramProvider provider) {
            super(NAME);
            this.provider = provider;
            this.plugin = JavaPlugin.getPlugin(HolographicDisplays.class);
        }

        @Override
        public HologramProvider getHologramProvider() {
            return provider;
        }

        @Override
        public CommandWrapper getHologramCommand() {
            return new ExecutorWrapper(plugin.getCommand("holograms"));
        }

        private static HologramPlatform getHologramPlatform(JavaPlugin plugin) {
            HolographicDisplays hdPlugin = JavaPlugin.getPlugin(HolographicDisplays.class);
            HolographicDisplaysHologramProvider provider = new HolographicDisplaysHologramProvider(
                    getHoloManager(hdPlugin));
            return new HDPlatform(provider);
        }

        private static InternalHologramManager getHoloManager(HolographicDisplays hdPlugin) {
            InternalHologramManager man;
            try {
                Field field = hdPlugin.getClass().getDeclaredField("internalHologramManager");
                field.setAccessible(true);
                man = (InternalHologramManager) field.get(hdPlugin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return man;
        }

    }

    private static class DHPlatform extends AbstractHologramPlatform {
        private static final String NAME = "DecentHolograms";
        private static final Class<?> CRAFT_SERVER_CLASS;
        private static final ReflectMethod GET_COMMAND_MAP_METHOD;

        static {
            CRAFT_SERVER_CLASS = ReflectionUtil.getObcClass("CraftServer");
            GET_COMMAND_MAP_METHOD = new ReflectMethod(CRAFT_SERVER_CLASS, "getCommandMap");
        }
        private final DecentHologramsPlugin plugin;
        private final DecentHologramsProvider provider;

        private DHPlatform(DecentHologramsProvider provider) {
            super(NAME);
            this.provider = provider;
            this.plugin = JavaPlugin.getPlugin(DecentHologramsPlugin.class);
        }

        @Override
        public HologramProvider getHologramProvider() {
            return provider;
        }

        @Override
        public CommandWrapper getHologramCommand() {
            return new WrappedCommand(getMainCommand());
        }

        private Command getMainCommand() {
            SimpleCommandMap commandMap = GET_COMMAND_MAP_METHOD.invoke(plugin.getServer());
            return commandMap.getCommand("decentholograms:decentholograms");
        }

        private static HologramPlatform getHologramPlatform(JavaPlugin plugin) {
            DecentHologramsProvider provider = new DecentHologramsProvider();
            return new DHPlatform(provider);
        }

        private class WrappedCommand extends Command implements CommandWrapper {
            private final Command delegate;
            private Zombificator zombificator;

            private WrappedCommand(Command delegate) {
                super(delegate.getName());
                this.delegate = delegate;
                CommandManager.register(this);

            }

            @Override
            public void wrapWith(Zombificator zombificator) {
                this.zombificator = zombificator;
            }

            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                boolean response;
                try {
                    response = delegate.execute(sender, commandLabel, args);
                } catch (Exception e) {
                    response = false;
                    JavaPlugin.getProvidingPlugin(ExecutorWrapper.class).getLogger()
                            .severe("Problem while executing DecentHolograms command");
                    e.printStackTrace();
                }
                if (response && args.length > 1
                        && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove"))) {
                    zombificator.foundRemoved(args[1]);
                }
                return response;
            }

        }

    }

    private static class FHPlatform extends AbstractHologramPlatform {
        private static final String NAME = "FancyHolograms";
        private final FancyHologramsPlugin plugin;
        private final FancyHologramProvider provider;

        private FHPlatform() {
            super(NAME);
            JavaPlugin jp = (JavaPlugin) JavaPlugin.getProvidingPlugin(getClass()).getServer().getPluginManager().getPlugin(NAME);
            plugin = (FancyHologramsPlugin) jp;
            provider = new FancyHologramProvider(plugin);
        }

        @Override
        public HologramProvider getHologramProvider() {
            return provider;
        }

        @Override
        public CommandWrapper getHologramCommand() {
            return new ExecutorWrapper(plugin.getPlugin().getCommand("holograms"));
        }

        private static FHPlatform getHologramPlatform(JavaPlugin plugin) {
            return new FHPlatform();
        }

        public boolean requiresLongerLoadDelay() {
            return true;
        }

    }

}
