package com.dungeon_additions.da.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({ "com.dungeon_additions.da.asm" })
public class DAPlugin implements IFMLLoadingPlugin{


    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {
        try
        {
            if (GlobalProperties.get(GlobalProperties.Keys.INIT) == null) { MixinBootstrap.init(); }
            MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        }
        catch (Throwable t)
        { t.printStackTrace(); }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
