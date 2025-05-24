package growthcraft.apples.world.disabling;

import growthcraft.apples.init.config.GrowthcraftApplesConfig;
import growthcraft.apples.shared.Reference;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WorldgenControl
{
    /*
     *  this class ensures that apple tree worldgen respects the master switch in the config file.
     *  that is - if apple module is disabled, the trees shouldn't generate in world.
     *
     *   if the module is disabled, we are going to register a tiny datapack with a single file overwriting add_apple_tree.json
     */


    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (GrowthcraftApplesConfig.getModuleEnabled() == false) {
            if (event.getPackType() == PackType.SERVER_DATA) {
                event.addRepositorySource((packConsumer) ->
                {
                    @SuppressWarnings("resource")
                    PackResources pack = new OurServerPack(PackType.SERVER_DATA, DYN_PACK_SERVER_FORMAT);

                    packConsumer.accept(Pack.create(
                            "growthcraft_apples_worldgen",
                            Component.literal("Growthcraft apples - built-in datapack"),
                            true,
                            s -> pack,
                            new Pack.Info(
                                    Component.literal(pack.packId()),
                                    DYN_PACK_SERVER_FORMAT,
                                    DYN_PACK_RESOURCE_FORMAT,
                                    FeatureFlagSet.of(),
                                    true
                            ),
                            PackType.SERVER_DATA,
                            Pack.Position.BOTTOM,
                            true,
                            PackSource.DEFAULT
                    ));
                });
            }
        }
    }

    private static final int DYN_PACK_SERVER_FORMAT = 9;
    private static final int DYN_PACK_RESOURCE_FORMAT = 8;

    private static class OurServerPack implements PackResources {
        private boolean isGenerated = false;

        private final Map<ResourceLocation, String> dataCache = new ConcurrentHashMap<>();
        private final PackType type;
        private final PackMetadataSection packMetadata;

        private OurServerPack(PackType type, int packFormat) {
            this.type = type;
            this.packMetadata = new PackMetadataSection(Component.literal(this.packId()), packFormat);
        }

        @Nullable
        @Override
        public IoSupplier<InputStream> getRootResource(String... pElements) {
            this.buildOnDemand();
            return null;
        }

        @Nullable
        @Override
        public IoSupplier<InputStream> getResource(PackType pPackType, ResourceLocation pLocation) {
            if (!pLocation.getNamespace().equals(Reference.MODID)) { return null; }
            if (!this.isGenerated) {
                this.buildOnDemand();
            }
            if (pPackType == this.type && this.dataCache.containsKey(pLocation)) {
                return supplierForPath(pLocation);
            }
            return null;
        }

        @Override
        public void listResources(PackType pPackType, String pNamespace, String pPath, ResourceOutput pResourceOutput) {
            if (!pNamespace.equals(Reference.MODID) || pPackType != this.type) { return; }
            if (!this.isGenerated) {
                this.buildOnDemand();
            }
            this.dataCache.keySet()
                          .stream()
                          .filter(loc -> loc.getNamespace().equals(pNamespace))
                          .filter(loc -> loc.getPath().startsWith(pPath))
                          .forEach(loc -> pResourceOutput.accept(loc, supplierForPath(loc)));
        }

        private void buildOnDemand() {
            if (!this.isGenerated) {
                CompletableFuture<HolderLookup.Provider> holderProvider = CompletableFuture.supplyAsync(
                        VanillaRegistries::createLookup,
                        Util.backgroundExecutor()
                );
                this.dataCache.put(new ResourceLocation(Reference.MODID, "forge/biome_modifier/add_apple_tree.json"), newContent);
				this.isGenerated = true; // yeah - all this for this one line above
            }
        }

        private IoSupplier<InputStream> supplierForPath(ResourceLocation loc) {
            return () -> new ByteArrayInputStream(this.dataCache.get(loc).getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public Set<String> getNamespaces(PackType pType) {
            if (pType != this.type) return Set.of();
            return fixedNamespaces;
        }
        private final Set<String> fixedNamespaces = Set.of(Reference.MODID);

        @Nullable
        @Override
        public <T> T getMetadataSection(MetadataSectionSerializer<T> pDeserializer) throws IOException {
            if (pDeserializer == PackMetadataSection.TYPE) {
                return (T) this.packMetadata;
            }
            return null;
        }

        @Override
        public String packId() {
            return Reference.MODID + ":pack1";
        }

        @Override
        public boolean isBuiltin() {
            return false; // not sure on that one
        }

        @Override
        public void close() {
        }

        @Override
        public boolean isHidden() {
            return true;
        }
        //////////////////////////////////
        private static final String newContent = "{\n    \"type\": \"forge:none\" \n}\n";
    }
}
