package me.kyroclient.managers;

import me.kyroclient.KyroClient;
import me.kyroclient.commands.Command;
import me.kyroclient.modules.Module;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandManager {
    public static final String COMMAND_CLASS_PATH = "me.kyroclient.commands.impl";
    public static CopyOnWriteArrayList<Command> commands = new CopyOnWriteArrayList<>();
    public static void init()
    {
        Reflections reflections = new Reflections(COMMAND_CLASS_PATH);
        Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> clazz : commandClasses)
        {
            try {
                Command command = clazz.getDeclaredConstructor().newInstance();
                commands.add(command);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    public static boolean handle(String msg) throws Exception {
        if (!msg.startsWith(".")) return false;

        String[] array = msg.split(" ");
        String baseCommand = array[0].replace(".", "");

        for (Command command : commands)
        {
            for (String str : command.getNames())
            {
                if (str == baseCommand)
                {
                    command.execute(array);
                    return true;
                }
            }
        }

        return false;
    }
}
