package me.kyroclient.modules.client;

import me.kyroclient.KyroClient;
import me.kyroclient.events.PacketSentEvent;
import me.kyroclient.modules.Module;
import me.kyroclient.settings.StringSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomBrand extends Module {
    public StringSetting clientBrand = new StringSetting("Forge");
    public CustomBrand()
    {
        super("Custom Brand", Category.CLIENT);

        addSettings(clientBrand);
    }

    @Override
    public void assign()
    {
        KyroClient.customBrand = this;
    }
}
