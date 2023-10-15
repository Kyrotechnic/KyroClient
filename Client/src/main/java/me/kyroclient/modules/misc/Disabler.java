package me.kyroclient.modules.misc;

import me.kyroclient.KyroClient;
import me.kyroclient.modules.Module;
import me.kyroclient.notifications.Notification;

public class Disabler extends Module {
    public Disabler()
    {
        super("Disabler", Category.MISC);
    }

    @Override
    public void onEnable()
    {
        KyroClient.notificationManager.showNotification("Disabler is not added yet!", 3000, Notification.NotificationType.ERROR);
    }
}
