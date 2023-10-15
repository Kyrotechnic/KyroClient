package me.kyroclient;

import java.lang.instrument.Instrumentation;

public class AgentLoader {
    public static void premain(String agentArgs, Instrumentation instrumentation)
    {
        System.out.println("This is my java agent test!");
    }
}
