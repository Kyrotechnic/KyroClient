package me.kyroclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeyboardEvent extends Event
{
    public int key;
    public char aChar;

    public KeyboardEvent(final int key, final char aChar) {
        this.key = key;
        this.aChar = aChar;
    }
}
